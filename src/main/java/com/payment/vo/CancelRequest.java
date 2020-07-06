package com.payment.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder	
public class CancelRequest {
	@NotNull(message="관리자 번호가 null 입니다.")
	@Pattern(regexp="^[0-9]+$", message="관리자 번호는 숫자여야 합니다.")
	private String managementNumber;
	@Min(value=1, message="1원이상의 금액이어야 합니다.")
	private int price;
	@Pattern(regexp="^[0-9]+$", message="부가세는 숫자여야 합니다.")
	private String vat;

}
