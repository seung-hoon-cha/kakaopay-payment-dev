package com.kakaopay.payment.exception;

public class CancelAmountException extends RuntimeException {

    private static final String MESSAGE = "결제 취소 금액(부가가치세)이 남은 결제 금액(부가가치세) 보다 작아야 합니다.";

    public CancelAmountException() {
        super(MESSAGE);
    }
    public CancelAmountException(String message) {
        super(message);
    }

    public CancelAmountException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
