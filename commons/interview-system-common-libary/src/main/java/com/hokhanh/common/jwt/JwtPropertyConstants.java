package com.hokhanh.common.jwt;

import com.hokhanh.common.utils.EnvLoaderUtil;

public final class JwtPropertyConstants {
	public static final String SECRET_KEY = EnvLoaderUtil.getJwtSecretKey();
	public static final Long ACCESS_TOKEN_EXPIRATION = EnvLoaderUtil.getJwtAccessTokenExpiration();;
	public static final Long REFRESH_TOKEN_EXPIRATION = EnvLoaderUtil.getJwtRefreshTokenExpiration();
}
