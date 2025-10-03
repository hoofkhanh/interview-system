package com.hokhanh.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoaderUtil {
	private static final Dotenv DOT_ENV = Dotenv.configure().directory("../../").load();

	private static String get(String key) {
		return DOT_ENV.get(key);
	}

	public static String getJwtSecretKey() {
		return get("JWT_SECRET_KEY");
	}
	
	public static Long getJwtExpiration() {
		return Long.parseLong(get("JWT_EXPIRATION"));
	}
	
	public static Long getJwtRefreshTokenExpiration() {
		return Long.parseLong(get("JWT_REFRESH_TOKEN_EXPIRATION"));
	}
	
	private EnvLoaderUtil() {
	}

}
