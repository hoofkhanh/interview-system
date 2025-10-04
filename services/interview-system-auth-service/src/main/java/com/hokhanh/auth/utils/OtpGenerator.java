package com.hokhanh.auth.utils;

import java.security.SecureRandom;


public final class OtpGenerator {

	private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);   
    }
}
