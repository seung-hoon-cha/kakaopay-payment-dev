package com.kakaopay.payment.service;

import com.kakaopay.payment.common.ManagementNumberGenerator;
import com.kakaopay.payment.component.TelegramTransfer;
import com.kakaopay.payment.dto.PaymentDto;
import com.kakaopay.payment.exception.CancelAmountException;
import com.kakaopay.payment.model.CardInfo;
import com.kakaopay.payment.model.Payment;
import com.kakaopay.payment.model.PaymentCardInfo;
import com.kakaopay.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PaymentService {

    private static final float VALUE_ADDED_TAX_RATE = 11F;
    private static final Integer DEFAULT_INSTALLMENT_MONTH = 0;

    private PaymentRepository paymentRepository;
    private TelegramTransfer telegramTransfer;
    private ModelMapper modelMapper;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, TelegramTransfer telegramTransfer, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.telegramTransfer = telegramTransfer;
        this.modelMapper = modelMapper;
    }

    public PaymentDto.PaymentRes findByManagementNumber(String managementNumber) {
        Payment payment = paymentRepository.findByLatestPayment(managementNumber);
        return (PaymentDto.PaymentRes) convert(payment, PaymentDto.PaymentRes.class);
    }

    //    @Transactional
    public PaymentDto.TransactionRes payProcess(PaymentDto.PaymentReq paymentReq) throws Exception {

        Payment payment = (Payment) convert(paymentReq, Payment.class);
        payment.setManagementNumber(ManagementNumberGenerator.generate());
        payment.setPaymentCardInfo(PaymentCardInfo.builder()
                .paymentID(payment.getManagementNumber())
                .cardInfo(CardInfo.builder()
                        .cardNumber(paymentReq.getCardNumber())
                        .validity(paymentReq.getValidity())
                        .cvc(paymentReq.getCvc())
                        .build())
                .build());

        // refactor
        payment.setValueAddedTax(calculateValueAddedTax(payment.getAmount(), payment.getValueAddedTax()));
        if (this.telegramTransfer.transfer(payment)) {
            payment.setTransferSuccess(true);
        }
        return PaymentDto.TransactionRes.builder()
                .managementNumber(this.paymentRepository.save(payment).getManagementNumber())
                .additionalInfo(StringUtils.EMPTY)
                .build();
    }

    public PaymentDto.TransactionRes cancelProcess(PaymentDto.CancelReq cancelReq) throws CancelAmountException {
        Payment cancel = (Payment) convert(cancelReq, Payment.class);
        setAdditionalPaymentInfo(cancel);

        if (this.telegramTransfer.transfer(cancel)) {
            cancel.setTransferSuccess(true);
        }
        return PaymentDto.TransactionRes.builder()
                .managementNumber(this.paymentRepository.save(cancel).getManagementNumber())
                .additionalInfo(StringUtils.EMPTY)
                .build();
    }

    private void setAdditionalPaymentInfo(Payment currentPay) throws CancelAmountException {

        Payment originPayment = this.paymentRepository.findByManagementNumber(currentPay.getOriginManagementNumber());
        if (originPayment == null) {
            throw new CancelAmountException("존재 하지 않는 관리 번호 입니다.");
        }
        List<Payment> cancelPayments = this.paymentRepository.findAllByOriginManagementNumber(currentPay.getOriginManagementNumber());

        Long totalAmount = cancelPayments.stream().mapToLong(Payment::getAmount).sum();
        Integer totalTax = cancelPayments.stream().mapToInt(Payment::getValueAddedTax).sum();
        Long amount = currentPay.getAmount();

        long remainAmount = originPayment.getAmount() - totalAmount - amount;
        int remainTax = originPayment.getValueAddedTax() - totalTax;
        int currentTax = currentPay.getValueAddedTax() == null ? Math.round(currentPay.getAmount() / VALUE_ADDED_TAX_RATE) : currentPay.getValueAddedTax();

        currentPay.setManagementNumber(ManagementNumberGenerator.generate());
        currentPay.setPaymentCardInfo(originPayment.getPaymentCardInfo());
        currentPay.setInstallmentMonth(DEFAULT_INSTALLMENT_MONTH);

        if (remainAmount < 0 || ((remainTax - currentTax) < 0 && currentPay.getValueAddedTax() != null)) {
            throw new CancelAmountException();
        }
        // 취소 금액은 맞으나 부가세가 남았을 경우
        if (remainAmount == 0 && (remainTax - currentTax) != 0 && currentPay.getValueAddedTax() != null) {
            throw new CancelAmountException();
        }

        if (remainAmount == 0 && currentPay.getValueAddedTax() == null) {
            currentPay.setValueAddedTax(remainTax);
        }
        currentPay.setValueAddedTax(currentTax);
    }

    private int calculateValueAddedTax(Long amount, Integer valueAddedTax) throws CancelAmountException {
        if (valueAddedTax != null && amount > valueAddedTax) {
            return valueAddedTax;
        }
        if (valueAddedTax != null && amount < valueAddedTax) {
            throw new CancelAmountException();
        }
        return Math.round(amount / VALUE_ADDED_TAX_RATE);
    }

    public PaymentDto.PaymentRes findByLatestPayment(String managementNumber) {
        Payment payment = paymentRepository.findByLatestPayment(managementNumber);
        return (PaymentDto.PaymentRes) convert(payment, PaymentDto.PaymentRes.class);
    }

    // (선택) 부분 취소 확인을 위해 사용
    public Payment findByRemainPayment(String managementNumber) {

        Payment originPayment = this.paymentRepository.findByManagementNumber(managementNumber);
        List<Payment> cancelPayments = this.paymentRepository.findAllByOriginManagementNumber(managementNumber);

        Long totalAmount = cancelPayments.stream().mapToLong(Payment::getAmount).sum();
        Integer totalTax = cancelPayments.stream().mapToInt(Payment::getValueAddedTax).sum();

        return Payment.builder()
                .amount(originPayment.getAmount() - totalAmount)
                .valueAddedTax(originPayment.getValueAddedTax() - totalTax)
                .build();
    }

    private Object convert(Object object, Class<?> type) {
        return modelMapper.map(object, type);
    }
}
