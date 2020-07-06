package com.payment.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentInfomationResponse {
	//좌로정렬 기능 구분 값
	private String type;
	//좌로 정렬 20글자
	private String managementNumber;
	//좌로 정렬 10~20자리(최대) 카드번호
	@NotNull
	@Size(min=10, max=20)
	private String maskCardNumber;
	//우로 정렬 앞자리 0 길이 2 취소시에는 일시불 00
	@NotNull
	@Size(min=2, max=2)
	private String installmentMonth;
	//좌로 정렬 숫자4자리 월2자리 년도2자리
	@NotNull
	@Size(min=4, max=4)
	private String validityPeriod;
	@NotNull
	@Size(min=3, max=3)
	private String cvc;
	//우로정렬 10자리, 결제는 100원이상이어야 하며 취소는 결제 금액보다 작아야함
	@Min(1)
	private int price;
	//우로 정렬 10자리, 부가가치세, 거래금액보다 작아야함
	@NotNull
	private int vat;
	
}
