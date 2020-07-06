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

}
