package com.kakaopay.payment.dto;

import com.kakaopay.payment.common.PaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

public class PaymentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentReq {
        //- 카드번호 ( 10~16 숫자)
        @ApiModelProperty(notes = "카드번호", required = true)
        @NotBlank(message = "required card number")
        @Pattern(regexp = "[0-9]{10,16}", message = "10~16자리의 숫자만 입력가능합니다")
        private String cardNumber;

        //- 유효기간(4자리 숫자, mmyy)
        @ApiModelProperty(notes = "유효기간", required = true)
        @NotBlank(message = "required validity")
        @Pattern(regexp = "[0-9]{4}", message = "4자리의 숫자만 입력가능합니다")
        private String validity;

        //- cvs(3자리 숫자)
        @ApiModelProperty(notes = "cvc", required = true)
        @NotBlank(message = "required cvc")
        @Pattern(regexp = "[0-9]{3}", message = "3자리의 숫자만 입력가능합니다")
        private String cvc;

        //- 할부개월수: 0(일시불), 1~12
        @ApiModelProperty(notes = "할부개월수", required = true)
        @Min(0)
        @Max(12)
        @NotNull
        private Integer installmentMonth;

        //- 결재금액 ( 100월 이상, 10억 이하, 숫자)
        @ApiModelProperty(notes = "결재금액", required = true)
        @Min(100)
        @Max(1000000000)
        @NotNull
        private Long amount;

        //- 부가가치세 ( optional )
        @ApiModelProperty(notes = "부가가치세", required = false)
        private Integer valueAddedTax;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelReq {

        @NotBlank(message = "required management number")
        @Pattern(regexp = "[0-9a-zA-Z]{20}", message = "관리 번호 입력 해주세요")
        private String managementNumber;
        //- 결재금액 ( 100월 이상, 10억 이하, 숫자)
        @Min(100)
        @Max(1000000000)
        @NotNull
        private Long amount;

        //- 부가가치세 ( optional )
        private Integer valueAddedTax;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionRes {
        private String managementNumber;
        private String additionalInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentRes {
        //- 관리번호 ( UUID, 20자리 )
        private String managementNumber;

        private String cardNumber;
        private String validity;
        private String cvc;

        private PaymentType paymentType;

        private Long amount;
        //- 추가 필드 ( optional )
        private String additionalInfo;
    }
}
