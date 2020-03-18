package com.kakaopay.payment.model;

import com.kakaopay.payment.common.EncryptionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.security.GeneralSecurityException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {

    private String cardNumber;
    private String validity;
    private String cvc;

    public String getMaskingCardNumber() {
        int repeatCount = cardNumber.length() - 6 - 3;
        return StringUtils.overlay(cardNumber, StringUtils.repeat("X", repeatCount), 6, cardNumber.length() - 3);
    }

    public String toEncryptString() throws GeneralSecurityException {
        return EncryptionUtils.encrypt(toString());
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s", cardNumber, validity, cvc);
    }
}
