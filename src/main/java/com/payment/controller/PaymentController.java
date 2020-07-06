package com.payment.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.service.PaymentService;
import com.payment.vo.CancelRequest;
import com.payment.vo.PaymentInfomationResponse;
import com.payment.vo.PaymentRequest;
import com.payment.vo.PaymentResponse;
import com.payment.vo.ResultResponse;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;
	
	
	 @PostMapping("/pay")
	 public ResultResponse pay(@RequestBody @Valid PaymentRequest request, Errors errors) throws Exception{
	 	
		if (errors.hasErrors()) {
			throw new Exception(errors.getFieldError().getDefaultMessage());
		}
	   	PaymentResponse pr = paymentService.insertPayment(request);
	   	
	   	return ResultResponse.builder().errCode("S0").errMsg("").data(pr).build();
	}
	 
	 @DeleteMapping("/cancel")
	 public ResultResponse cancel(@RequestBody @Valid CancelRequest request, Errors errors) throws Exception{
		if (errors.hasErrors()) {
			throw new Exception(errors.getFieldError().getDefaultMessage());
		}
	   	PaymentResponse pr = paymentService.cancelPayment(request);
	   	return ResultResponse.builder().errCode("S0").errMsg("").data(pr).build();
	}
	 
	@GetMapping("/history/{managementId}")
	public ResultResponse retrieve(@PathVariable String managementId) throws Exception{
		
		PaymentInfomationResponse pr = paymentService.retrievePayment(managementId);
	   	return ResultResponse.builder().errCode("S0").errMsg("").data(pr).build();
	}
	    
}
