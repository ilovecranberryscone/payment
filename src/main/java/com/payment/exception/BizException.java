package com.payment.exception;

public class BizException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BizException(){
		super();
	}
	public BizException(String message){
		super(message);
	}
}
