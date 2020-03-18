package com.kakaopay.payment.common;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EncryptionUtilsTest {

    @Test
    public void givenString_whenEncrypt_thenEncryptString() throws Exception {

        String text = "카드정보|유효기간|cvc";
        String expected = "hIhEZ/hN/HOuzDY/FTbiE4oHQFmaJPxQtZrzl0tEQPk=";

        String actual = EncryptionUtils.encrypt(text);

        assertThat(actual, is(expected));
    }

    @Test
    public void givenEncryptString_whenDecrypt_thenString() throws Exception {

        String encryptString = "hIhEZ/hN/HOuzDY/FTbiE4oHQFmaJPxQtZrzl0tEQPk=";
        String expected = "카드정보|유효기간|cvc";

        String actual = EncryptionUtils.decrypt(encryptString);

        assertThat(actual, is(expected));
    }

}