
package com.payment.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequest {
	//좌로 정렬 10~20자리(최대) 카드번호
	@NotNull(message="카드 번호가 null 입니다.")
	@Size(min=10, max=20, message="카드 번호가 10~20 사이여야 합니다.")
	@Pattern(regexp="^[0-9]+$", message="카드 번호는 숫자로 이루어져야 합니다.")
	private String cardNumber;
	//우로 정렬 앞자리 0 길이 2 취소시에는 일시불 00
	@NotNull(message="할부개월이 null 입니다.")
	@Size(min=2, max=2, message="할부개월은 2글자여야 합니다.")
	@Pattern(regexp="^[0-9]+$", message="할부개월은 숫자여야 합니다.")
	private String installmentMonth;
	//좌로 정렬 숫자4자리 월2자리 년도2자리
	@NotNull(message="유효기간이 null 입니다.")
	@Size(min=4, max=4, message="유효기간은 4글자여야 합니다.")
	@Pattern(regexp="^[0-9]+$", message="유효기간은 숫자여야 합니다.")
	private String validityPeriod;
	@NotNull
	@Size(min=3, max=3, message="cvc 번호는 3글자여야 합니다.")
	@Pattern(regexp="^[0-9]+$", message="cvc 번호는 숫자여야 합니다.")
	private String cvc;
	//우로정렬 10자리, 결제는 100원이상이어야 하며 취소는 결제 금액보다 작아야함
	@Min(1)
	private int price;
	//우로 정렬 10자리, 부가가치세, 거래금액보다 작아야함
	@Pattern(regexp="^[0-9]+$", message="부가세는 숫자여야 합니다.")
	private String vat;
}
