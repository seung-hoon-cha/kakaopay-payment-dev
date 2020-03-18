package com.kakaopay.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.payment.common.PaymentType;
import com.kakaopay.payment.configuration.PaymentConfiguration;
import com.kakaopay.payment.dto.PaymentDto;
import com.kakaopay.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(PaymentConfiguration.class)
@WebMvcTest(PaymentController.class)
class PaymentReqControllerTest {

    private static final String PATH = "/api/v1";

    private static final String MANAGEMENT_NUMBER_PAY = "KtwnKfUPiirkl96YMPsM";
    private static final String MANAGEMENT_NUMBER_CANCEL = "R1KRNGTuHMsW4vaiE3Hy";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentService paymentService;

    // 결제
    @Test
    public void givenValidPaymentDto_whenCreatePayment_thenSuccess() throws Exception {

        PaymentDto.PaymentReq paymentReq = PaymentDto.PaymentReq.builder()
                .cardNumber("0123456789")
                .validity("0325")
                .cvc("777")
                .installmentMonth(0)
                .amount(10000L)
                .build();

        when(paymentService.payProcess(any())).thenReturn(PaymentDto.TransactionRes.builder()
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .build());

        this.mockMvc.perform(
                post(String.format("%s/%s", PATH, "payments"))
                        .content(asJsonString(paymentReq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void givenInvalidCvc_whenCreatePayment_thenBadRequest() throws Exception {

        PaymentDto.PaymentReq paymentReq = PaymentDto.PaymentReq.builder()
                .cardNumber("0123456789")
                .validity("0325")
                .cvc("7773")
                .installmentMonth(0)
                .amount(10000L)
                .build();

        when(paymentService.payProcess(any())).thenReturn(PaymentDto.TransactionRes.builder()
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .build());

        this.mockMvc.perform(
                post(String.format("%s/%s", PATH, "payments"))
                        .content(asJsonString(paymentReq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidCardNumber_whenCreatePayment_thenBadRequest() throws Exception {

        PaymentDto.PaymentReq paymentReq = PaymentDto.PaymentReq.builder()
                .cardNumber("012345678")
                .validity("0325")
                .cvc("7773")
                .installmentMonth(0)
                .amount(10000L)
                .build();

        when(paymentService.payProcess(any())).thenReturn(PaymentDto.TransactionRes.builder()
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .build());

        this.mockMvc.perform(
                post(String.format("%s/%s", PATH, "payments"))
                        .content(asJsonString(paymentReq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidity_whenCreatePayment_thenBadRequest() throws Exception {

        PaymentDto.PaymentReq paymentReq = PaymentDto.PaymentReq.builder()
                .cardNumber("0123456789")
                .validity("032025")
                .cvc("777")
                .installmentMonth(0)
                .amount(10000L)
                .build();

        when(paymentService.payProcess(any())).thenReturn(PaymentDto.TransactionRes.builder()
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .build());

        this.mockMvc.perform(
                post(String.format("%s/%s", PATH, "payments"))
                        .content(asJsonString(paymentReq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCancelDto_whenUpdatePayment_thenSuccess() throws Exception {

        PaymentDto.CancelReq cancelReq = PaymentDto.CancelReq.builder()
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .amount(10000L)
                .build();

        when(paymentService.cancelProcess(any())).thenReturn(PaymentDto.TransactionRes.builder()
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .build());

        this.mockMvc.perform(
                put(String.format("%s/%s", PATH, "payments/cancel"))
                        .content(asJsonString(cancelReq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenManagementNumber_whenGetPayment_thenResponseDtoPayment() throws Exception {

        PaymentDto.PaymentRes response = PaymentDto.PaymentRes.builder()
                .cardNumber("0123456789")
                .validity("0325")
                .cvc("777")
                .paymentType(PaymentType.PAYMENT)
                .amount(10000L)
                .managementNumber(MANAGEMENT_NUMBER_PAY)
                .build();

        when(paymentService.findByManagementNumber(any())).thenReturn(response);

        this.mockMvc.perform(
                get(String.format("%s/%s/%s", PATH, "payments", MANAGEMENT_NUMBER_PAY)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

