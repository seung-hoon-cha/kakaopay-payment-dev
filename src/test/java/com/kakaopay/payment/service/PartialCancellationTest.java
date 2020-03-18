package com.kakaopay.payment.service;

import com.kakaopay.payment.dto.PaymentDto;
import com.kakaopay.payment.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PartialCancellationTest {

    private static final String CARD_NUMBER = "1098762345";
    private static final String VALIDITY = "0320";
    private static final String CVC = "777";

    @Autowired
    private PaymentService paymentService;
    private String originManagementNumber;

//    @Test
//    public void partialCancelCase1() throws Exception {
//
//        // 결제 / 11,000 / 1,000 / 성공 / 11,000 / 1,000 / 11,000(1,000)원 결제 성공
//        Payment actual = pay(11000L, 1000);
//        assertThat(actual.getAmount(), is(11000L));
//        assertThat(actual.getValueAddedTax(), is(1000));
//
//        // 부분 취소 / 1,100 / 100 / 성공 / 9,900 /  900 / 1,100(100)원 취소 성공
//        actual = cancel(1100L, 100);
//        assertThat(actual.getAmount(), is(9900L));
//        assertThat(actual.getValueAddedTax(), is(900));
//
//        // 부분 취소 /3,300 / null / 성공 / 6,600 / 600 / 3,300원 취소 성공
//        actual = cancel(3300L, null);
//        assertThat(actual.getAmount(), is(6600L));
//        assertThat(actual.getValueAddedTax(), is(600));
//
//        // 부분 취소 / 7,000 / null / 실패 / 6,600 / 600 / 7,000원 취소하려 했으나 남은 결제금액 보다 커서 실패
//        actual = cancel(7000L, null);
//        assertThat(actual.getAmount(), is(6600L));
//        assertThat(actual.getValueAddedTax(), is(600));
//
//        // 부분 취소 / 6,600 /  700 / 실패 / 6,600 / 600 / 6,600(700)원 취소하려 했으나 남은 부 가가치세보다 취소요청 부가가치세가 커 서 실패
//        actual = cancel(6600L, 700);
//        assertThat(actual.getAmount(), is(6600L));
//        assertThat(actual.getValueAddedTax(), is(600));
//
//        // 부분취소 / 6,600 / 600 / 성공 / 0 / 0 / 6,600(600)원 성공
//        actual = cancel(6600L, 600);
//        assertThat(actual.getAmount(), is(0L));
//        assertThat(actual.getValueAddedTax(), is(0));
//
//        // 부분 취소 / 100 / null / 실패 / 0 / 0 / 100원 취소하려했으나 남은 결제금액이 없어서 실패
//        actual = cancel(100L, null);
//        assertThat(actual.getAmount(), is(0L));
//        assertThat(actual.getValueAddedTax(), is(0));
//    }

//    @Test
//    public void partialCancelCase2() throws Exception {
//
//        // 결제 / 20,000 / 909 / 성공 / 20,000 / 909 / 20,000(909)원 결제 성공
//        Payment actual = pay(20000L, 909);
//        assertThat(actual.getAmount(), is(20000L));
//        assertThat(actual.getValueAddedTax(), is(909));
//
//        // 부분 취소 / 10,000 / 0 / 성공 /. 10,000 / 909 / 10,000(0)원 취소 성공
//        actual = cancel(10000L, 0);
//        assertThat(actual.getAmount(), is(10000L));
//        assertThat(actual.getValueAddedTax(), is(909));
//
//        // 부분 취소 / 10,000 / 0 / 실패 /. 10,000 / 909 / 10,000(0)원 취소하려했으나 남은 부가 가치세 금액(909)이 더 크므로 실패
//        actual = cancel(10000L, 0);
//        assertThat(actual.getAmount(), is(10000L));
//        assertThat(actual.getValueAddedTax(), is(909));
//
//        // 부분 취소 / 10,000 /  909 / 성공 /. 0 /. 0 / 10,000(909)원 취소 성공
//        actual = cancel(10000L, 909);
//        assertThat(actual.getAmount(), is(0L));
//        assertThat(actual.getValueAddedTax(), is(0));
//    }

//    @Test
//    public void partialCancelCase3() throws Exception {
//        // 결제 / 20,000 / null / 성공 / 20,000 / 1,818 / 20,000원 결제 성공, 부가가치세 (1,818) 자동계산
//        Payment actual = pay(20000L, null);
//        assertThat(actual.getAmount(), is(20000L));
//        assertThat(actual.getValueAddedTax(), is(1818));
//
//        // 부분 취소 / 10,000 / 1,000 / 성공 /. 10,000 / 818 / 10,000(1,000)원 취소 성공
//        actual = cancel(10000L, 1000);
//        assertThat(actual.getAmount(), is(10000L));
//        assertThat(actual.getValueAddedTax(), is(818));
//
//        // 부분 취소 / 10,000 / 909 / 실패 /. 10,000 / 818 / 10,000(909)원 취소하려했으나 남은 부가가치세가 더 작으므로 실패
//        actual = cancel(10000L, 909);
//        assertThat(actual.getAmount(), is(10000L));
//        assertThat(actual.getValueAddedTax(), is(818));
//
//        // 부분 취소 / 10,000 /  null / 성공 /. 0 /. 0 / 10,000원 취소, 남은 부가가치세는 818원으로 자동계산되어 성공
//        actual = cancel(10000L, null);
//        assertThat(actual.getAmount(), is(0L));
//        assertThat(actual.getValueAddedTax(), is(0));
//    }

//    private Payment pay(Long amount, Integer tax) throws Exception {
//        PaymentDto.PaymentReq paymentReq = PaymentDto.PaymentReq.builder()
//                .amount(amount).valueAddedTax(tax).installmentMonth(0)
//                .cardNumber(CARD_NUMBER).validity(VALIDITY).cvc(CVC)
//                .build();
//        PaymentDto.TransactionRes response = this.paymentService.payProcess(paymentReq);
//        this.originManagementNumber = response.getManagementNumber();
//        return this.paymentService.findByRemainPayment(this.originManagementNumber);
//    }
//
//    private Payment cancel(Long amount, Integer tax) {
//        PaymentDto.CancelReq cancelReq = PaymentDto.CancelReq.builder()
//                .managementNumber(this.originManagementNumber)
//                .amount(amount).valueAddedTax(tax)
//                .build();
//        try {
//            this.paymentService.cancelProcess(cancelReq);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return this.paymentService.findByRemainPayment(this.originManagementNumber);
//    }
}
