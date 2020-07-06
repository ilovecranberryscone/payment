package com.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.payment.service.PaymentService;
import com.payment.vo.PaymentRequest;
import com.payment.vo.PaymentResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class PayUnitTest {
	
	@Autowired
	private PaymentService paymentService;
	
	@Test
	void payTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		assertEquals(1000, pr.getRemainingAmount());
	}
	
	@Test  //카드번호 validation
	void cardNumberValidationTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333444444444444444444444")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
	}
	
	@Test  //할부개월 validation
	void installmentMonthIntegerValidationTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("11112222333344444")
				.installmentMonth("테스트")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
	}

	
	@Test  //중복결제
	void paymentDuplicateTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("11112222333344444")
				.installmentMonth("테스트")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333444444444")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		pr = paymentService.insertPayment(paymentRequest);
		
	}
	
	@Test  //부가세가 결제 금액 보다 큰 경우
	void vatTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("11112222333344444")
				.installmentMonth("테스트")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.vat("100000")
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);

	}
}
