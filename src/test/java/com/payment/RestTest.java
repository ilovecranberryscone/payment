package com.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.vo.PaymentRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class RestTest {
	@Autowired
	private MockMvc mvc;
	
	@Test
	void payControllerTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		PaymentRequest paymentRequest = PaymentRequest
				.builder().cardNumber("11122223333").installmentMonth("03")
				.validityPeriod("0522").cvc("527").price(10000).build();
		mvc.perform(post("/payment/pay").content(objectMapper.writeValueAsBytes(paymentRequest))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

}
