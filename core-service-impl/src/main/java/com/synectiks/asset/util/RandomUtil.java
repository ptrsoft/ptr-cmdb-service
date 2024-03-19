package com.synectiks.asset.util;

import java.util.Random;

public class RandomUtil {

//	public static Integer getRandom() {
//		Random r = new Random();
//		int low = 0; // it is inclusive
//		int high = 101; //it is exclusive
//		Integer result = r.nextInt(high-low) + low;
//		return result;
//	}
	
	public static Float getRandom() {
		Random r = new Random();
		int low = 0; // it is inclusive
		int high = 101; //it is exclusive
		Float result = getRandom(low, high) ;
		return result;
	}
	
//	public static Integer getRandom(int low, int high) {
//		Random r = new Random();
//		high = high + 1; //it is exclusive
//		Integer result = r.nextInt(high-low) + low;
//		return result;
//	}
	
	public static Float getRandom(int low, int high) {
		Random r = new Random();
		float result = low + (high - low) * r.nextFloat();
		return result;
	}

	public static Long getRandomLong(int low, int high) {
		Random r = new Random();
		return new Long(r.nextInt(high)+low);
	}

	public static String randomAlphabeticString() {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 5;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	
	public static void main(String a[]) {
//		Random rd = new Random(); // creating Random object
//		int rangeMin = 98;
//		int rangeMax = 100;
//		Float randomValue = rangeMin + (rangeMax - rangeMin) * rd.nextFloat();
//	    System.out.println(randomValue);
//		String input = "897373451";
//		String result = input.replaceAll("^\"|\"$", "");
//		System.out.println(input);
//		System.out.println(result);
		int min = 1000;
		int max = 5000;
		Long p = getRandomLong(min, max);
		System.out.println("new value::::: "+p);
		double x = 23651-21423;
		double y = x/23651;
		double per = y*100;
		System.out.println("percentage ::::: "+per);
	}
}
