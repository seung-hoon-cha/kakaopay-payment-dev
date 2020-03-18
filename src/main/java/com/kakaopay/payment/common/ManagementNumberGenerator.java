package com.kakaopay.payment.common;

import org.apache.commons.lang3.RandomStringUtils;

public class ManagementNumberGenerator {

    public static final int MAX_ID_LENGTH = 20;

    public static String generate() {
        return RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH);
    }
}
