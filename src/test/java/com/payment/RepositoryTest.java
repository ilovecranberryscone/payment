package com.payment;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.payment.entity.CardHistory;
import com.payment.entity.Payment;
import com.payment.repository.CardHistoryRepository;
import com.payment.repository.PaymentRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=PaymentApplication.class)
@AutoConfigureMockMvc
public class RepositoryTest {
	
	@Autowired
	private PaymentRepository paymentDao;
	
	@Autowired
	private CardHistoryRepository cardHistDao;
	
	
	@Test //paymentDao insert test
	void paymentDaoTest() throws Exception {
		Payment payment = Payment.builder().type("PAYMENT").encryptedCardNumber("t8Q6LVe8qZHIXb9qmYcZyIi7BaSXt75CKCPpnGpwJJTDJUNjsnjaaxAwO7AqWks+nt7zlA==")
				.installmentMonth("03").encryptedValidityPeriod("6I7AJk3KpYOwFCQhY797AfogJA2+pTIyRqwpsm9+bHzKTT9g/DeYN2hfzpsM/fxlHPOGQA==")
				.encryptedCvc("YzmVJAoc/tpvaMxKf8MgNUKmRPo9a/oe1Bc1SUsJhdKBwzckKzPEmEI6J3pNc0tSI2TDDg==").price(1000).vat(100)
				.encryptCardInfomation("038+RfDBJIUVuesE/49cI00UixUJAJheqPRC2o7joBUDZP+Pd4Xh8Dc6GkfMVBX9368g7RXHIVqWRKnwqtwymryi1Ng=").createDate(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())).build();
		
		payment = paymentDao.save(payment);
	}
	
	@Test 
	void cardHistDaoTest() throws Exception {
		CardHistory cardHist = CardHistory.builder().cardInterfaceString("1234567891010encrypt").build();
		cardHist = cardHistDao.save(cardHist);
	}
	

}
