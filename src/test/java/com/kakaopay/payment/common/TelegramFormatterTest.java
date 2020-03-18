package com.kakaopay.payment.common;

import com.kakaopay.payment.model.CardInfo;
import com.kakaopay.payment.model.Payment;
import com.kakaopay.payment.model.PaymentCardInfo;
import org.junit.Test;

import java.security.GeneralSecurityException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TelegramFormatterTest {

    @Test
    public void givenPayment_whenFormat_thenString() throws GeneralSecurityException {
        String managementNUmber = ManagementNumberGenerator.generate();
        String originManagementNUmber = ManagementNumberGenerator.generate();

        String expected = " 446CANCEL    d0tllTwk0y9mcynDhWeJ1234567890          000325777     100000000000110Iir1ylNvVaXawIfHWztA8mndwktx97VZVQQbKykLQy9AJXGBZV0ZDlFzrePKmWk=                                                                                                                                                                                                                                                                                                               ";

        Payment payment = Payment.builder()
                .managementNumber(managementNUmber)
                .originManagementNumber(originManagementNUmber)
                .amount(10000L)
                .valueAddedTax(110)
                .installmentMonth(0)
                .paymentType(PaymentType.CANCEL)
                .paymentCardInfo(PaymentCardInfo.builder()
                        .paymentID(originManagementNUmber)
                        .cardInfo(CardInfo.builder()
                                .cardNumber("1234567890")
                                .validity("0325")
                                .cvc("777")
                                .build())
                        .build())
                .build();

        String actual = TelegramFormatter.format(payment).getTelegram();

        assertThat(actual.length(), is(450));
    }

}