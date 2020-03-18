package com.kakaopay.payment.controller;

import com.kakaopay.payment.dto.PaymentDto;
import com.kakaopay.payment.service.PaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ApiOperation(value = "결제 API")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/payments", consumes = "application/json; charset=UTF-8")
    public PaymentDto.TransactionRes createPayment(@RequestBody @Valid PaymentDto.PaymentReq paymentReq) throws Exception {
        log.debug("[PaymentReq] {}", paymentReq.toString());
        return this.paymentService.payProcess(paymentReq);
    }

    @ApiOperation(value = "결제 취소 API")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/payments/cancel", consumes = "application/json; charset=UTF-8")
    public PaymentDto.TransactionRes updatePayment(@RequestBody @Valid PaymentDto.CancelReq cancelReq) throws Exception {
        log.debug("[CancelReq] {}", cancelReq.toString());
        return this.paymentService.cancelProcess(cancelReq);
    }

    @ApiOperation(value = "결제 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "managementNumber", value = "결제 관리 번호", required = true, dataType = "string", paramType = "path")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/payments/{managementNumber}", produces = "application/json; charset=UTF-8")
    public PaymentDto.PaymentRes getPayment(@PathVariable String managementNumber) throws Exception {
        log.debug("[GET] {}", managementNumber);
        return this.paymentService.findByManagementNumber(managementNumber);
    }
}
