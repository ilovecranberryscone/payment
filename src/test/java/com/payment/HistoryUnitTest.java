package com.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.payment.service.PaymentService;
import com.payment.vo.PaymentInfomationResponse;
import com.payment.vo.PaymentRequest;
import com.payment.vo.PaymentResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class HistoryUnitTest {
	@Autowired
	private PaymentService paymentService;
	
	@Test
	void HistoryTest() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.cardNumber("111122223333")
				.installmentMonth("03")
				.validityPeriod("0522")
				.cvc("527")
				.price(1000)
				.build();
		PaymentResponse pr = paymentService.insertPayment(paymentRequest);
		String mNumber = pr.getManagementNumber();
		PaymentInfomationResponse pir = paymentService.retrievePayment(mNumber);
		assertEquals(mNumber, pir.getManagementNumber());
	}

	
	@Test //없는 번호 조회
	void NoManagementNumberTest() throws Exception {
		String mNumber = "20200707111222333334";
		PaymentInfomationResponse pir = paymentService.retrievePayment(mNumber);
	}
}
