package com.payment.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ResultResponse {
    private String errCode;
    private String errMsg;
    private Object data;

}
