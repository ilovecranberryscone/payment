package com.payment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	
	//좌로 정렬 20글자
	@Id
	@GeneratedValue(generator = "managementNumber")
	@GenericGenerator(name = "managementNumber",
		strategy = "com.payment.util.ManagementNumberGenerator")
	private String managementNumber;
	//좌로정렬 기능 구분 값
	@NotNull(message="구분 값이 null 입니다.")
	private String type;
	//좌로 정렬 10~20자리(최대) 카드번호
	@NotNull(message="카드 번호가 null 입니다.")
	private String encryptedCardNumber;
	//우로 정렬 앞자리 0 길이 2 취소시에는 일시불 00
	@NotNull(message="할부개월이 null 입니다.")
	@Size(min=2, max=2, message="할부개월은 2글자여야 합니다.")
	@Pattern(regexp="^[0-9]+$", message="할부개월은 숫자여야 합니다.")
	private String installmentMonth;
	//좌로 정렬 숫자4자리 월2자리 년도2자리
	@NotNull(message="유효기간이 null 입니다.")
	private String encryptedValidityPeriod;
	@NotNull(message="cvc가 null 입니다.")
	private String encryptedCvc;
	//우로정렬 10자리, 결제는 100원이상이어야 하며 취소는 결제 금액보다 작아야함
	@Min(value=1, message="1원이상의 금액이어야 합니다.")
	private int price;
	//우로 정렬 10자리, 부가가치세, 거래금액보다 작아야함
	@NotNull(message="부가세가 null 입니다.")
	private int vat;
	//전체 20자, 원거래관리번호
	private String originalManagementNumber;
	//전체 300자, 카드번호, 유효기간, cvc 데이터를 안전하게 암호화
	@NotNull(message="암호화된 카드 정보가 null 입니다.")
	private String encryptCardInfomation;
	private String createDate;
	
}