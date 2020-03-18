package com.kakaopay.payment.service;

import com.kakaopay.payment.common.ManagementNumberGenerator;
import com.kakaopay.payment.common.PaymentType;
import com.kakaopay.payment.dto.PaymentDto;
import com.kakaopay.payment.model.CardInfo;
import com.kakaopay.payment.model.Payment;
import com.kakaopay.payment.model.PaymentCardInfo;
import com.kakaopay.payment.repository.PaymentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaymentServiceTest {

//    @Autowired
//    private PaymentService paymentService;
//
//    PaymentRepository paymentRepository = mock(PaymentRepository.class);
//
//    // mock을 통해 원한는 값 리턴이 안됨....
//    @Test
//    public void givenManagementNumber_whenFindByManagementNumber_thenPaymentRes() {
//
//        String managementNumber = ManagementNumberGenerator.generate();
//        String originManagementNumber = ManagementNumberGenerator.generate();
//        Payment response = Payment.builder()
//                .managementNumber(managementNumber)
//                .originManagementNumber(originManagementNumber)
//                .paymentType(PaymentType.CANCEL)
//                .installmentMonth(0)
//                .amount(10000L)
//                .valueAddedTax(1000)
//                .paymentCardInfo(PaymentCardInfo.builder()
//                        .cardInfo(CardInfo.builder()
//                                .cardNumber("0123456789")
//                                .validity("0325")
//                                .cvc("777")
//                                .build())
//                        .paymentID(originManagementNumber)
//                        .build())
//                .createTimeAt(LocalDateTime.now())
//                .additionalField("")
//                .transferSuccess(true)
//                .build();
//
//
//        when(paymentRepository.findByLatestPayment(managementNumber)).thenReturn(response);
//
//        PaymentDto.PaymentRes actual = this.paymentService.findByManagementNumber(managementNumber);
//
//        assertThat(actual.getAmount(), is(10000L));
//        assertThat(actual.getManagementNumber(), is(managementNumber));
//        assertThat(actual.getValidity(), is("0325"));
//    }
}