package com.kakaopay.payment.common;

import com.kakaopay.payment.model.CardCompany;
import com.kakaopay.payment.model.CardInfo;
import com.kakaopay.payment.model.Payment;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.GeneralSecurityException;

@Slf4j
public class TelegramFormatter {

    public enum DataType {
        // 숫자 / 우측으로 정렬, 빈 자리 공백, ex) 4자리 숫자 : 3 -> "___3"
        NUMBER("NUMBER", "%ds"),
        // 숫자 / 우측으로 정렬, 빈 자리 0, ex) 4자리 숫자(0) : 3 -> "0003"
        NUMBER_0("NUMBER_O", "0%dd"),
        // 숫자 / 좌측으로 정렬, 빈 자리 공백, ex) 4자리 숫자(L) : 3 -> "3___"
        NUMBER_L("NUMBER_L", "-%ds"),
        //        // 숫자 / 우측으로 정렬, 빈 자리 공백, ex) 4자리 숫자 : 3 -> "___3"
//        NUMBER("NUMBER", "%dd"),
//        // 숫자 / 우측으로 정렬, 빈 자리 0, ex) 4자리 숫자(0) : 3 -> "0003"
//        NUMBER_0("NUMBER_O", "0%dd"),
//        // 숫자 / 좌측으로 정렬, 빈 자리 공백, ex) 4자리 숫자(L) : 3 -> "3___"
//        NUMBER_L("NUMBER_L", "-%dd"),
        // 문자 / 좌측으로 정렬, 빈 자리 공백, ex) 10자리 문자 : HOMEWORK -> "HOMEWORK__"
        TEXT("TEXT", "-%ds");

        @Getter
        private String name;
        @Getter
        private String alignFormat;

        DataType(String name, String alignFormat) {
            this.name = name;
            this.alignFormat = alignFormat;
        }
    }

    public enum Item {
        // 데이터 길이 / 숫자 / 4 / "데이터 길이"를 제외한 총 길이
        DATA_LENGTH("DATA_LENGTH", 4, DataType.NUMBER),
        // 데이터 구분 / 문자 / 10 / 기능 구분값, 승인(PAYMENT), 취소(CANCEL)
        DATA_CLASSIFICATION("DATA_CLASSIFICATION", 10, DataType.TEXT),
        // 관리번호 / 문자 / 20 / unique id(20자리)
        MANAGEMENT_NUMBER("MANAGEMENT_NUMBER", 20, DataType.TEXT),

        // 카드 번호 / 숫자(L) / 20 / 카드번호 / 최소 10자리, 최대 20자리
        CARD_NUMBER("CARD_NUMBER", 20, DataType.NUMBER_L),
        // 할부 개월 수 / 숫 자 (0) / 2/ 일시불인 경우 "00", 2개월인 경 우는 "02"로 저장 / 일시불, 2개월 ~ 12개월, 취소시에는 일시불 "00"로 저장
        INSTALLMENT_MONTH("INSTALLMENT_MONTH", 2, DataType.NUMBER_0),
        // 카드 유효 기간 / 숫 자 (L) / 4 / 카드 유효기간 / 월(2자리), 년도(2자리) ex) 0125 -> 2025년 1월까지
        VALIDITY("VALIDITY", 4, DataType.NUMBER_L),
        // cvc / 숫자(L) / 3 / 카드 cvc 데이터 /
        CVC("CVC", 3, DataType.NUMBER_L),
        // 거래 금액 / 숫자 / 10 / 결제/취소 금액 / 결제 : 100원 이상, 취소 : 결제 금액보다 작아 야함
        AMOUNT("AMOUNT", 10, DataType.NUMBER),
        // 부가 가치 세 / 숫 자 (0) / 10 / 결제/취소 금액의 부가세 / 거래금액보다는 작아야한다. 취소의 경우, 원 거 래 금액의 부가가치세와 총 취소금액의 부가가 치세의 합과 같아야 한다.
        VALUE_ADDED_TAX("VALUE_ADDED_TAX", 10, DataType.NUMBER_0),
        // 원거 래관 리번 호 / 문 자 / 20 / 취소시에만 결제 관리번호 저장 / 결제시에는 공백
        ORIGIN_MANAGEMENT_NUMBER("ORIGIN_MANAGEMENT_NUMBER", 20, DataType.TEXT),
        // 암호 화된 카드 정보 / 문자 / 300 / 카드번호, 유호기 간, cvc 데이터를 안전하게 암호화 / 암/복호화 방식 자유롭게 선택
        ENCRYPT_CART_INFO("ENCRYPT_CART_INFO", 300, DataType.TEXT),
        // 예비 필드 / 문자 / 47 / /
        ADDITIONAL_FIELD("EXTRA_FIELD", 47, DataType.TEXT);

        @Getter
        private String name;
        @Getter
        private int length;
        private String dataFormat;

        Item(String name, int length, DataType dataType) {
            this.name = name;
            this.length = length;
            this.dataFormat = "%" + String.format(dataType.getAlignFormat(), length);
        }

        public String format(int data) {
            return String.format(this.dataFormat, data);
        }
//
//        public String format(long data) {
//            return String.format(this.dataFormat, data);
//        }
//
        public String format(String data) {
            return String.format(this.dataFormat, data);
        }
    }

    public static CardCompany format(Payment payment) throws GeneralSecurityException {
        StringBuilder telegramBuffer = new StringBuilder();
        telegramBuffer.append(Item.DATA_CLASSIFICATION.format(payment.getPaymentType().name()));
        telegramBuffer.append(Item.MANAGEMENT_NUMBER.format(payment.getManagementNumber()));
        CardInfo cardInfo = payment.getPaymentCardInfo().getCardInfo();
        telegramBuffer.append(Item.CARD_NUMBER.format(cardInfo.getCardNumber()));
        telegramBuffer.append(Item.INSTALLMENT_MONTH.format(payment.getInstallmentMonth()));
        telegramBuffer.append(Item.VALIDITY.format(cardInfo.getValidity()));
        telegramBuffer.append(Item.CVC.format(cardInfo.getCvc()));
        telegramBuffer.append(Item.AMOUNT.format(String.valueOf(payment.getAmount())));
        telegramBuffer.append(Item.VALUE_ADDED_TAX.format(payment.getValueAddedTax()));
        telegramBuffer.append(Item.ORIGIN_MANAGEMENT_NUMBER.format(payment.getOriginManagementNumber()));
        telegramBuffer.append(Item.ENCRYPT_CART_INFO.format(cardInfo.toEncryptString()));
        telegramBuffer.append(Item.ADDITIONAL_FIELD.format(payment.getAdditionalField()));

        String telegramLength = Item.DATA_LENGTH.format(String.valueOf(telegramBuffer.length()));

        log.debug("[TELEGRAM] {}", telegramBuffer.toString());
        return new CardCompany(payment.getManagementNumber(), telegramLength, telegramBuffer.toString());
    }
}
