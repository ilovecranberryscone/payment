package com.payment.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private String managementNumber;
    private String resultString;
	private int remainingAmount;
	private int remainingVat;
}
