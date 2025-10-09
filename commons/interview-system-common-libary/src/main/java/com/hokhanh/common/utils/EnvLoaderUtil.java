package com.hokhanh.common.utils;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvLoaderUtil {
	private static final Dotenv DOT_ENV = Dotenv.configure().directory("../../").load();

//	private static String get(String key) {
//		return DOT_ENV.get(key);
//	}

	private static String get(String key) {
		String value = System.getenv(key);
		if (value != null) {
			return value;
		}

		return DOT_ENV.get(key);
	}

	public static String getJwtSecretKey() {
		return get("JWT_SECRET_KEY");
	}

	public static Long getJwtAccessTokenExpiration() {
		return Long.parseLong(get("JWT_ACCESS_TOKEN_EXPIRATION"));
	}

	public static Long getJwtRefreshTokenExpiration() {
		return Long.parseLong(get("JWT_REFRESH_TOKEN_EXPIRATION"));
	}

	private EnvLoaderUtil() {
	}
}