package com.kakaopay.payment.exception;

public class TelegramTransferException extends RuntimeException {

    private static final String MESSAGE = "카드사 전문 생성 또는 전송 실패";

    public TelegramTransferException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
