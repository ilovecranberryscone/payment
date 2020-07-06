package com.payment.util;

public class StringUtil {

	public static String makeRightAlign(String resultString, String pattern, int length) throws Exception {
		int originalLength = resultString.length();
		if(pattern != null && pattern.length() != 1) {
			throw new Exception("Length of pattern is not 1");
		}
		for(int index = 0; index < length - originalLength ;index++) {
			resultString = pattern + resultString;
		}
		if(resultString.length() != length) {
			throw new Exception("Length of result is not equal to expected value");
		}
		return resultString;
	}
	
	public static String makeLeftAlign(String resultString, String pattern, int length) throws Exception {
		int originalLength = resultString.length();
		if(pattern != null && pattern.length() != 1) {
			throw new Exception("Length of pattern is not 1");
		}
		for(int index = 0; index < length - originalLength ;index++) {
			resultString = resultString + pattern;
		}
		if(resultString.length() != length) {
			throw new Exception("Length of result is not equal to expected value");
		}
		return resultString;
	}
}
