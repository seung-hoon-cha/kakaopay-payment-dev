package com.kakaopay.payment.repository;

import com.kakaopay.payment.common.PaymentType;
import com.kakaopay.payment.model.CardInfo;
import com.kakaopay.payment.model.Payment;
import com.kakaopay.payment.model.PaymentCardInfo;
import com.kakaopay.payment.model.converter.EncryptCardInfoConverter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(EncryptCardInfoConverter.class)
class PaymentReqRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void givenManagementNumber_whenFindByLatestPayment_thenCancel() {

        String managementNumber = "KtwnKfUPiirkl96YMPsM";
        String cancelManagementNumber = "R1KRNGTuHMsW4vaiE3Hy";

        PaymentCardInfo paymentCardInfo = PaymentCardInfo.builder()
                .paymentID(managementNumber)
                .cardInfo(CardInfo.builder()
                        .cardNumber("0123456789")
                        .validity("0325")
                        .cvc("777")
                        .build())
                .build();

        Payment payment = Payment.builder()
                .managementNumber(managementNumber)
                .paymentCardInfo(paymentCardInfo)
                .paymentType(PaymentType.PAYMENT)
                .installmentMonth(0)
                .amount(10000L)
                .valueAddedTax(91)
                .build();

        Payment cancel = Payment.builder()
                .managementNumber(cancelManagementNumber)
                .paymentCardInfo(paymentCardInfo)
                .originManagementNumber(managementNumber)
                .paymentType(PaymentType.PAYMENT)
                .installmentMonth(0)
                .amount(10000L)
                .valueAddedTax(91)
                .build();

        this.paymentRepository.save(payment);
        this.paymentRepository.save(cancel);

        Payment actual = this.paymentRepository.findByLatestPayment(managementNumber);

        assertThat(actual.getManagementNumber(), is(cancelManagementNumber));
    }
}