package com.kakaopay.payment.configuration;

import com.kakaopay.payment.common.PaymentType;
import com.kakaopay.payment.dto.PaymentDto;
import com.kakaopay.payment.model.Payment;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfiguration {

    @Bean(name = "modelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(generateRequestPaymentDtoMapping());
        modelMapper.addMappings(generateRequestCancelDtoMapping());
        modelMapper.addMappings(generateResponseDtoMapping());
        return modelMapper;
    }

    private PropertyMap<PaymentDto.PaymentReq, Payment> generateRequestPaymentDtoMapping() {
        return new PropertyMap<PaymentDto.PaymentReq, Payment>() {
            protected void configure() {
                map().setPaymentType(PaymentType.PAYMENT);
            }
        };
    }

    private PropertyMap<PaymentDto.CancelReq, Payment> generateRequestCancelDtoMapping() {
        return new PropertyMap<PaymentDto.CancelReq, Payment>() {
            protected void configure() {
                map().setPaymentType(PaymentType.CANCEL);
                map().setOriginManagementNumber(source.getManagementNumber());
                map().setManagementNumber(StringUtils.EMPTY);
            }
        };
    }

    private PropertyMap<Payment, PaymentDto.PaymentRes> generateResponseDtoMapping() {
        return new PropertyMap<Payment, PaymentDto.PaymentRes>() {
            protected void configure() {
                map().setCardNumber(source.getPaymentCardInfo().getCardInfo().getMaskingCardNumber());
                map().setValidity(source.getPaymentCardInfo().getCardInfo().getValidity());
                map().setCvc(source.getPaymentCardInfo().getCardInfo().getCvc());
            }
        };
    }
}
