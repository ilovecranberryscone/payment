package com.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.vo.CancelRequest;
import com.payment.vo.PaymentRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.json.JSONObject;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class TestCase2 {
	@Autowired
	private MockMvc mvc;
	@Autowired 
	private WebApplicationContext ctx;

	@BeforeEach() 
	public void setup() { 
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx) 
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가 
				.alwaysDo(print()) .build(); 
	}

	@Test
	void payControllerTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		PaymentRequest paymentRequest = PaymentRequest
				.builder().cardNumber("111122223333").installmentMonth("03")
				.validityPeriod("0522").cvc("527").price(20000).vat("909").build();
		MvcResult result = mvc.perform(post("/payment/pay").content(objectMapper.writeValueAsBytes(paymentRequest))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();
		String content = result.getResponse().getContentAsString();
		JSONObject rr = new JSONObject(content);
		JSONObject pr = rr.optJSONObject("data");
		String managementNumber = pr.getString("managementNumber");
		System.out.println("(1번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		Thread.sleep(1000);
		
		//두번째 테스트
		CancelRequest cancelRequest = CancelRequest.builder().price(10000).vat("0").managementNumber(pr.getString("managementNumber")).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(2번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		Thread.sleep(1000);
		//세번째테스트
		cancelRequest = CancelRequest.builder().price(10000).vat("0").managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		System.out.println("(3번째 테스트) " + rr);
		
		Thread.sleep(1000);
		//네번째 테스트
		cancelRequest = CancelRequest.builder().price(10000).vat("909").managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(4번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		
	}
}
