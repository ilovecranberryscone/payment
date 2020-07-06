package com.payment.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payment.entity.CardHistory;
import com.payment.entity.Payment;
import com.payment.exception.BizException;
import com.payment.repository.CardHistoryRepository;
import com.payment.repository.PaymentRepository;
import com.payment.util.CryptoUtil;
import com.payment.util.MaskingUtil;
import com.payment.util.StringUtil;
import com.payment.vo.CancelRequest;
import com.payment.vo.PaymentInfomationResponse;
import com.payment.vo.PaymentRequest;
import com.payment.vo.PaymentResponse;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository paymentDao;
	@Autowired
	private CardHistoryRepository cardHistDao;
	
	@Transactional
	public PaymentResponse insertPayment(PaymentRequest request) throws Exception{
		if(request.getVat() != null && (Integer.parseInt(request.getVat()) > request.getPrice())) {
			throw new BizException("부가세가 원 금액보다 큽니다.");
		}
		if(request.getVat() == null || request.getVat() == "" ) {
			request.setVat(Math.round(request.getPrice()/11) + "" );
		}
		String encryptCardNum = CryptoUtil.encryptAES256(request.getCardNumber());
		String encryptValidityPeriod = CryptoUtil.encryptAES256(request.getValidityPeriod());
		String encryptCvc = CryptoUtil.encryptAES256(request.getCvc());
		String encryptString = CryptoUtil.encryptAES256(request.getCardNumber()+ "|" + 
		request.getValidityPeriod() +"|" + request.getCvc());
		
		//TODO DB에 집어넣고 관리 번호 회신 -> 완료
		Payment payment = Payment.builder().type("PAYMENT").encryptedCardNumber(encryptCardNum)
				.installmentMonth(request.getInstallmentMonth()).encryptedValidityPeriod(encryptValidityPeriod)
				.encryptedCvc(encryptCvc).price(request.getPrice()).vat(Integer.parseInt(request.getVat()))
				.encryptCardInfomation(encryptString).createDate(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())).build();
		
		if(checkDuplication(payment)) {
			throw new BizException("중복된 거래 요청이므로 종료합니다.");
		}
		payment = paymentDao.save(payment);
		
		String result = makePaymentString(payment,request.getCardNumber(), request.getValidityPeriod(),request.getCvc());
		
		//TODO 결과값을 결제 DB에 넣음
		CardHistory cardHist = CardHistory.builder().cardInterfaceString(result).build();
		cardHist = cardHistDao.save(cardHist);
		return PaymentResponse.builder().resultString(result).managementNumber(payment.getManagementNumber()).remainingAmount(payment.getPrice()).remainingVat(payment.getVat()).build();
		
	}
	
	//부분취소도 되게끔 구현
	@Transactional
	public PaymentResponse cancelPayment(CancelRequest request) throws Exception{
		//DB에서 관리번호로 기존 결제 데이터 가져오기
		Payment payment = paymentDao.findById(request.getManagementNumber()).orElseThrow(() -> new NoSuchElementException("결제 데이터가 존재하지 않습니다."));
		//원래관리번호로 취소한 값을 전체를 sum해서 가져오기
		List<Payment> cancelList = paymentDao.findAllByOriginalManagementNumber(request.getManagementNumber());
		
		//취소한 값들을 SUM한 값과 request로 온 값을 더해서 그 값이 처음 결제한 값보다 크면 에러로 리턴
		int cancelPriceSum = 0;
		int cancelVatSum = 0;
		int originalPayAmount = payment.getPrice();
		int originalVat = payment.getVat();
		for(Payment pay : cancelList) {
			cancelPriceSum += pay.getPrice();
			cancelVatSum += pay.getVat();
		}
		cancelPriceSum += request.getPrice();
		if(request.getVat() == null || "".equals(request.getVat() )) {
			int vat = 0;
			if(originalPayAmount == cancelPriceSum) {
				vat = originalVat - cancelVatSum;
			}
			else {
				vat = Math.round(request.getPrice()/11);
			}
			request.setVat(vat + "");
			cancelVatSum += vat;
		} else {
			cancelVatSum += Integer.parseInt(request.getVat());
		}
		if(originalPayAmount < cancelPriceSum) {
			throw new BizException("취소 금액이 결제 금액보다 큽니다.");
		}
		if(originalVat < cancelVatSum) {
			throw new BizException("부가세 계산에 오류가 있습니다.");
		}
		if(originalPayAmount == cancelPriceSum && originalVat != cancelVatSum) {
			throw new BizException("부가세 계산에 오류가 있습니다.");
		}
	
		//취소 데이터 insert
		Payment cancelData = Payment.builder().originalManagementNumber(request.getManagementNumber()).price(request.getPrice()).vat(Integer.parseInt(request.getVat())).type("CANCEL").encryptedCardNumber(payment.getEncryptedCardNumber())
				.installmentMonth("00").encryptedValidityPeriod(payment.getEncryptedValidityPeriod()).encryptedCvc(payment.getEncryptedCvc())
				.encryptCardInfomation(payment.getEncryptCardInfomation()).createDate(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())).build();
		if(checkDuplication(cancelData)) {
			throw new BizException("중복된 거래 요청이므로 종료합니다.");
		}
		payment = paymentDao.save(cancelData);
		//카드 취소용 string 생성
		String result = makeCancelString(cancelData,CryptoUtil.decryptAES256(cancelData.getEncryptedCardNumber())
				,CryptoUtil.decryptAES256(cancelData.getEncryptedValidityPeriod()),CryptoUtil.decryptAES256(cancelData.getEncryptedCvc()));
		CardHistory cardHist = CardHistory.builder().cardInterfaceString(result).build();
		cardHist = cardHistDao.save(cardHist);
		
		return PaymentResponse.builder().resultString(result).managementNumber(request.getManagementNumber()).remainingAmount(originalPayAmount-cancelPriceSum)
				.remainingVat(originalVat-cancelVatSum).build();
		
	}
	
	public PaymentInfomationResponse retrievePayment(String managementNumber) throws Exception{
		Payment payment = paymentDao.findById(managementNumber).orElseThrow(() -> new NoSuchElementException("조회 데이터가 존재하지 않습니다."));
		String cardNumber = CryptoUtil.decryptAES256(payment.getEncryptedCardNumber());
		cardNumber = MaskingUtil.maskCard(cardNumber);
		
		
		return PaymentInfomationResponse.builder().type(payment.getType()).managementNumber(managementNumber)
				.maskCardNumber(cardNumber).installmentMonth(payment.getInstallmentMonth())
				.validityPeriod(payment.getEncryptedValidityPeriod()).cvc(payment.getEncryptedCvc())
				.price(payment.getPrice()).vat(payment.getVat()).build();
	}
	
	private boolean checkDuplication(Payment payment) {
		if(paymentDao.findAllByEncryptedCardNumberAndCreateDate(payment.getEncryptedCardNumber(), payment.getCreateDate()).size() > 0 ) return true;
		return false;
	}
	
	//private String makePaymentString(PaymentRequest request, String managementNumber, String EncryptedCardInfomation) throws Exception{
	private String makePaymentString(Payment payment, String cardNumber, String validityPeriod, String cvc) throws Exception{
		String resultString ="";
		resultString += StringUtil.makeRightAlign("446", " ", 4);
		resultString += StringUtil.makeLeftAlign("PAYMENT", " ", 10);
		resultString += StringUtil.makeRightAlign(payment.getManagementNumber(), " ", 20);
		resultString += StringUtil.makeLeftAlign(cardNumber +"", " ", 20);
		resultString += StringUtil.makeRightAlign(payment.getInstallmentMonth() +"", "0", 2);
		resultString += StringUtil.makeLeftAlign(validityPeriod, " ", 4);
		resultString += StringUtil.makeLeftAlign(cvc +"", " ", 3);
		resultString += StringUtil.makeRightAlign(payment.getPrice() + "", " ", 10);
		resultString += StringUtil.makeRightAlign(payment.getVat() +"", "0", 10);
		resultString += StringUtil.makeLeftAlign("", " ", 20);
		resultString += StringUtil.makeLeftAlign(payment.getEncryptCardInfomation(), " ", 300);
		resultString += StringUtil.makeLeftAlign("", " ", 47);
		return resultString;
	}
	
	private String makeCancelString(Payment cancelPayment, String cardNumber, String validityPeriod, String cvc) throws Exception {
		String resultString = "";
		resultString += StringUtil.makeRightAlign("446", " ", 4);
		resultString += StringUtil.makeLeftAlign("CANCEL", " ", 10);
		resultString += StringUtil.makeRightAlign(cancelPayment.getManagementNumber(), " ", 20);
		resultString += StringUtil.makeLeftAlign(cardNumber +"", " ", 20);
		resultString += StringUtil.makeRightAlign(cancelPayment.getInstallmentMonth(), "0", 2);
		resultString += StringUtil.makeLeftAlign(validityPeriod, " ", 4);
		resultString += StringUtil.makeLeftAlign(cvc +"", " ", 3);
		resultString += StringUtil.makeRightAlign(cancelPayment.getPrice() + "", " ", 10);
		resultString += StringUtil.makeRightAlign(cancelPayment.getVat() +"", "0", 10);
		resultString += StringUtil.makeLeftAlign(cancelPayment.getOriginalManagementNumber(), " ", 20);
		resultString += StringUtil.makeLeftAlign(cancelPayment.getEncryptCardInfomation(), " ", 300);
		resultString += StringUtil.makeLeftAlign("", " ", 47);
		return resultString;
	}

}
