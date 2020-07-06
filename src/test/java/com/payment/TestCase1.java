package com.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.json.JSONObject;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class TestCase1 {
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
				.validityPeriod("0522").cvc("527").price(11000).vat("1000").build();
		MvcResult result = mvc.perform(post("/payment/pay").content(objectMapper.writeValueAsBytes(paymentRequest))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();
		String content = result.getResponse().getContentAsString();
		JSONObject rr = new JSONObject(content);
		JSONObject pr = rr.optJSONObject("data");
		String managementNumber = pr.getString("managementNumber");
		System.out.println("(1번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		Thread.sleep(1000);
		
		//두번째 테스트
		CancelRequest cancelRequest = CancelRequest.builder().price(1100).vat("100").managementNumber(pr.getString("managementNumber")).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(2번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		Thread.sleep(1000);
		//세번째테스트
		//결제 상태인 금액을 보여줘야함
		cancelRequest = CancelRequest.builder().price(3300).managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(3번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		Thread.sleep(1000);
		//네번째 테스트
		cancelRequest = CancelRequest.builder().price(7000).managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(4번째 테스트) "+rr);
		
		Thread.sleep(1000);
		//다섯번째테스트
		cancelRequest = CancelRequest.builder().price(6600).vat("700").managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(5번째 테스트) " + rr);
		
		Thread.sleep(1000);
		//여섯번째 테스트
		cancelRequest = CancelRequest.builder().price(6600).vat("600").managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(6번째 테스트) 남은 잔고: " + pr.getInt("remainingAmount") + " 남은 부가가치세 : " + pr.getInt("remainingVat"));
		
		Thread.sleep(1000);
		//일곱번째테스트
		cancelRequest = CancelRequest.builder().price(100).managementNumber(managementNumber).build();
		result = mvc.perform(delete("/payment/cancel").content(objectMapper.writeValueAsBytes(cancelRequest)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();
		content = result.getResponse().getContentAsString();
		rr = new JSONObject(content);
		pr = rr.optJSONObject("data");
		System.out.println("(7번째 테스트) "+ rr);
		
	}

}
