package com.kakaopay.payment.common;

import com.kakaopay.payment.model.PaymentCardInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CommonTest {

    @Test
    public void UUIDTest() {
        UUID uuid = UUID.randomUUID();
        String s = Long.toString(uuid.getMostSignificantBits(), 94);
        System.out.println(s);
    }

    @Test
    public void UUIDTest1() {
//        RandomStringUtils randomStringUtils = new RandomStringUtils();
        String s = RandomStringUtils.randomAlphanumeric(20);
        System.out.println(s);
        String s1 = RandomStringUtils.randomAlphanumeric(20);
        System.out.println(s1);
    }

    @Test
    public void maskTest() {
//        String ccNumber = "123232323767";
        String ccNumber = "1232323237671000";
        int repeatCount = ccNumber.length() - 6 - 3;
        String msk = StringUtils.overlay(ccNumber, StringUtils.repeat("X", repeatCount), 6, ccNumber.length() - 3);
        System.out.println(ccNumber);
        System.out.println(msk);
    }

    @Test
    public void formatterTest() {
        TelegramFormatter.Item dataLength = TelegramFormatter.Item.DATA_LENGTH;

        System.out.println(dataLength.format("0"));
    }

    @Test
    public void tmpTest() {
        PaymentCardInfo cardInfo = PaymentCardInfo.builder().paymentID("12345678901234567890").build();
        System.out.println(cardInfo.getPaymentID());
    }
}
