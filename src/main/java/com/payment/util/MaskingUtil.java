package com.payment.util;

public class MaskingUtil {

	public static String maskCard(String cardNumber) {
		String resultString = cardNumber.substring(0,6);
		for(int idx = 0; idx < cardNumber.substring(6,cardNumber.length()-3).length(); idx++){
			resultString += "*";
		}
		return resultString + cardNumber.substring(cardNumber.length()-3, cardNumber.length());
	}
}
