package com.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.payment.service.PaymentService;
import com.payment.vo.CancelRequest;
import com.payment.vo.PaymentRequest;
import com.payment.vo.PaymentResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class CancelUnitTest {
	@Autowired
	private PaymentService paymentService;
	
	
	@Test
	void cancelTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		
		Thread.sleep(1000);
		
		String managementNumber = pr.getManagementNumber();
		CancelRequest cancelRequest = CancelRequest
				.builder()
				.managementNumber(managementNumber)
				.price(1000)
				.build();
		pr = paymentService.cancelPayment(cancelRequest);
		assertEquals(0, pr.getRemainingAmount());
		
	}
	
	@Test //잔고가 없는 상태에서 취소
	void cancelNoBalanceTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		
		Thread.sleep(1000);
		String managementNumber = pr.getManagementNumber();
		CancelRequest cancelRequest = CancelRequest
				.builder()
				.managementNumber(managementNumber)
				.price(10000)
				.build();
		pr = paymentService.cancelPayment(cancelRequest);
		assertEquals(0, pr.getRemainingAmount());
		
	}
	
	@Test //부가세가 취소 금액보다 큰 경우
	void vatTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		String managementNumber = pr.getManagementNumber();
		Thread.sleep(1000);
		
		CancelRequest cancelRequest = CancelRequest
				.builder()
				.managementNumber(managementNumber)
				.price(1000)
				.vat("10000000")
				.build();
		pr = paymentService.cancelPayment(cancelRequest);
		assertEquals(0, pr.getRemainingAmount());
		
	}

	@Test //부분 취소
	void partitalCancelTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		String managementNumber = pr.getManagementNumber();
		Thread.sleep(1000);
		
		CancelRequest cancelRequest = CancelRequest
				.builder()
				.managementNumber(managementNumber)
				.price(500)
				.build();
		pr = paymentService.cancelPayment(cancelRequest);
		assertEquals(500, pr.getRemainingAmount());
		
		Thread.sleep(1000);
		cancelRequest = CancelRequest
				.builder()
				.managementNumber(managementNumber)
				.price(500)
				.build();
		pr = paymentService.cancelPayment(cancelRequest);
		assertEquals(0, pr.getRemainingAmount());
		
	}
}
