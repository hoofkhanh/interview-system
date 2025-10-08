package com.hokhanh.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hokhanh.auth.client.user.UserClient;
import com.hokhanh.auth.client.user.UserClientMapper;
import com.hokhanh.auth.constants.AuthenticationConstants;
import com.hokhanh.auth.email.AuthEmailService;
import com.hokhanh.auth.jwt.JwtService;
import com.hokhanh.auth.mapper.AuthMapper;
import com.hokhanh.auth.model.Auth;
import com.hokhanh.auth.model.Role;
import com.hokhanh.auth.redis.JwtTokenCacheService;
import com.hokhanh.auth.redis.SignupInterviewCacheService;
import com.hokhanh.auth.redis.SignupInterviewInputAndOtp;
import com.hokhanh.auth.repository.AuthRepository;
import com.hokhanh.auth.repository.RoleRepository;
import com.hokhanh.auth.request.signin.SigninInput;
import com.hokhanh.auth.request.signup.SignupCandidateInput;
import com.hokhanh.auth.request.signup.SignupInterviewerInput;
import com.hokhanh.auth.request.signup.VerifyInterviewerSignupOtpInput;
import com.hokhanh.auth.response.common.BaseApiPayload;
import com.hokhanh.auth.response.logout.LogoutApiPayload;
import com.hokhanh.auth.response.refreshToken.RefreshTokenApiPayload;
import com.hokhanh.auth.response.refreshToken.RefreshTokenApiStatusType;
import com.hokhanh.auth.response.signin.SigninApiPayload;
import com.hokhanh.auth.response.signin.SigninApiStatusType;
import com.hokhanh.auth.response.signup.SignupApiStatusType;
import com.hokhanh.auth.response.signup.SignupCandidateApiPayload;
import com.hokhanh.auth.response.signup.SignupInterviewerApiPayload;
import com.hokhanh.auth.response.signup.VerifyInterviewerSignupOtpApiPayload;
import com.hokhanh.auth.response.signup.VerifyOtpApiStatusType;
import com.hokhanh.auth.utils.OtpGenerator;
import com.hokhanh.common.auth.RoleConstants;
import com.hokhanh.common.jwt.JwtPropertyConstants;
import com.hokhanh.common.user.response.CreateOrUpdateUserPayload;
import com.hokhanh.common.user.response.UserByEmailPayload;

import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtTokenCacheService jwtTokenCacheService;
	
	private final UserClient userClient;
	
	private final RoleRepository roleRepo;
	
	private final SignupInterviewCacheService signupInterviewCacheService;
	
	private final AuthEmailService authEmailService;
	
	private final AuthMapper authMapper;
	
	private final UserClientMapper userClientMapper;
	
	private final AuthRepository authRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtService jwtService;
	

	public boolean isTokenBlocked(String accessToken) {
		return jwtTokenCacheService.isTokenInBlacklist(accessToken);
	}

	public SignupInterviewerApiPayload signupInterviewer(SignupInterviewerInput input) {
		UserByEmailPayload user =  userClient.userByEmailInternal(input.baseSignup().email());
		if(user != null && user.baseUser() != null) {
			return new SignupInterviewerApiPayload(
				new BaseApiPayload(false, "Email existing"),
				SignupApiStatusType.EMAIL_EXISTS,
				null
			);
		}
		
		boolean username = authRepo.existsByUsername(input.username());
		if(username == true ) {
			return new SignupInterviewerApiPayload(
				new BaseApiPayload(false, "Username existing"),
				SignupApiStatusType.USERNAME_EXISTS,
				null
			);
		}
		
		Role role = roleRepo.findById(input.baseSignup().roleId()).orElse(null);
		if(role == null || !role.getName().equals(RoleConstants.INTERVIEWER_ROLE)) {
			return new SignupInterviewerApiPayload(
				new BaseApiPayload(false, "Role not existing"),
				SignupApiStatusType.ROLE_NOT_EXISTS,
				null
			);
		}
		
		String otp = OtpGenerator.generateOtp();
		long otpExpirationMinutes = 5;
		Duration ttl = Duration.ofMinutes(otpExpirationMinutes);
		signupInterviewCacheService.cacheSignupInterviewerInputAndOtp(input, otp, ttl);
		
		authEmailService.sendInterviewerSignupOtpToEmail(input.baseSignup().email(), otp, otpExpirationMinutes);
		
		return new SignupInterviewerApiPayload(
				new BaseApiPayload(true, "Signup successfully"),
				null,
				authMapper.toSignupInterviewerPayload(LocalDateTime.now(), otpExpirationMinutes)
		);
	}

	public VerifyInterviewerSignupOtpApiPayload verifyInterviewerSignupOtp(VerifyInterviewerSignupOtpInput input, GraphQLContext context) {
		SignupInterviewInputAndOtp signupInterviewInputAndOtp = signupInterviewCacheService.getSignupInterviewerInputAndOtp(input.email());
		if(signupInterviewInputAndOtp == null) {
			return new VerifyInterviewerSignupOtpApiPayload(
				new BaseApiPayload(false, "Otp expired"),
				VerifyOtpApiStatusType.OTP_EXPIRED,
				null
			);
		}else if(input.otp().equals(signupInterviewInputAndOtp.otp()) == false) {
			return new VerifyInterviewerSignupOtpApiPayload(
				new BaseApiPayload(false, "Otp invalid"),
				VerifyOtpApiStatusType.OTP_INVALID,
				null
			);
		}
		
		signupInterviewCacheService.deleteSignupInterviewerInputAndOtp(input.email());
		
		CreateOrUpdateUserPayload payload = userClient.createOrUpdateUserInternal(
				userClientMapper.toCreateOrUpdateUserInput(signupInterviewInputAndOtp.signupInterviewInput().baseSignup()));
		
		Role role = roleRepo.findById(signupInterviewInputAndOtp.signupInterviewInput().baseSignup().roleId()).orElse(null);
		
		Auth auth = authMapper.toAuth(role, payload.baseUser().id(), signupInterviewInputAndOtp.signupInterviewInput());
		auth.setPassword(passwordEncoder.encode(auth.getPassword()));
		auth = authRepo.save(auth);
		
		String accessToken = generateAccessAndRefreshTokenAndSetCookie(auth, context);
		
		return new VerifyInterviewerSignupOtpApiPayload(
			new BaseApiPayload(true, "Verify otp successfully"),
			null,
			authMapper.toVerifyInterviewerSignupOtpPayload(auth, accessToken, payload)
		);
	}

	public SignupCandidateApiPayload signupCandidate(SignupCandidateInput input, GraphQLContext context) {
		UserByEmailPayload user = userClient.userByEmailInternal(input.baseSignup().email());
		String roleName = null;
		
		if(user.baseUser() != null) {
			roleName = authRepo.findRoleNameByUserId(user.baseUser().id()).orElse(null);
		}
		
		if(user.baseUser() != null && roleName.equals(RoleConstants.CANDIDATE_ROLE)) {
			CreateOrUpdateUserPayload payload = userClient.createOrUpdateUserInternal(
					userClientMapper.toCreateOrUpdateUserInput(input.baseSignup()));
			
			Auth auth = authRepo.findByUserId(user.baseUser().id());
			
			String accessToken = generateAccessAndRefreshTokenAndSetCookie(auth, context);
			
			return new SignupCandidateApiPayload(
				new BaseApiPayload(true, "Update (Signup successfully)"),
				null,
				authMapper.toSignupCandidatePayload(auth, accessToken, payload)
			);
		}else if(user.baseUser() != null && !roleName.equals(RoleConstants.CANDIDATE_ROLE)) {
			return new SignupCandidateApiPayload(
				new BaseApiPayload(false, "Can't update (Role not existing)"),
				SignupApiStatusType.ROLE_NOT_EXISTS,
				null
			);
		}
		
		Role role = roleRepo.findById(input.baseSignup().roleId()).orElse(null);
		if(role == null || !role.getName().equals(RoleConstants.CANDIDATE_ROLE)) {
			return new SignupCandidateApiPayload(
				new BaseApiPayload(false, "Role not existing"),
				SignupApiStatusType.ROLE_NOT_EXISTS,
				null
			);
		}
		
		CreateOrUpdateUserPayload payload = userClient.createOrUpdateUserInternal(
				userClientMapper.toCreateOrUpdateUserInput(input.baseSignup()));
		
		Auth auth = authRepo.save(authMapper.toAuth(role, payload.baseUser().id(), null));
		
		String accessToken = generateAccessAndRefreshTokenAndSetCookie(auth, context);
		
		return new SignupCandidateApiPayload(
			new BaseApiPayload(true, "Signup successfully"),
			null,
			authMapper.toSignupCandidatePayload(auth, accessToken, payload)
		);
	}
	
	public SigninApiPayload signin(SigninInput input, GraphQLContext context) {
		Auth auth = authRepo.findByUsername(input.username());
		if(auth == null) {
			return new SigninApiPayload(
				new BaseApiPayload(false, "Username not found"),
				SigninApiStatusType.USERNAME_NOT_FOUND,
				null
			);
		}else if(passwordEncoder.matches(input.password(), auth.getPassword()) == false) {
			return new SigninApiPayload(
				new BaseApiPayload(false, "Password invalid"),
				SigninApiStatusType.PASSWORD_INVALID,
				null
			);
		}
		
		String accessToken = generateAccessAndRefreshTokenAndSetCookie(auth, context);
		
		return new SigninApiPayload(
			new BaseApiPayload(true, "Signin successfully"),
			null,
			authMapper.toSigninPayload(accessToken)
		);
	}
	
	private String generateAccessAndRefreshTokenAndSetCookie(Auth auth, GraphQLContext context) {
		String accessToken = jwtService.generateAccessToken(auth.getId(), auth.getUserId(), auth.getRole().getName());
		String refreshToken = jwtService.generateRefreshToken(auth.getId());
		
		jwtTokenCacheService.cacheRefreshToken(refreshToken, Duration.ofMillis(JwtPropertyConstants.REFRESH_TOKEN_EXPIRATION));
		
		context.put(AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken); // set cookie for refreshToken in GraphQLContextInterceptor class
		context.put(AuthenticationConstants.SET_COOKIE_CONTEXT_KEY, true); // flag to set cookie in GraphQLContextInterceptor class
		
		return accessToken;
	}

	public LogoutApiPayload logout(String accessToken, String refreshToken, GraphQLContext context) {
		jwtTokenCacheService.deleteRefreshToken(refreshToken);
		
		Long seconds = getRemainingSeconds(accessToken);
		if (seconds > 0) {
			jwtTokenCacheService.cacheAccessTokenToBlacklist(accessToken, Duration.ofSeconds(seconds));
		}
		
		context.put(AuthenticationConstants.REMOVE_COOKIE_CONTEXT_KEY, true);
		
		return new LogoutApiPayload(new BaseApiPayload(true, "Logout successfully"));
	}
	
	public RefreshTokenApiPayload refreshToken(String refreshToken, GraphQLContext context) {
		String rtFromRedis = jwtTokenCacheService.getCachedRefreshToken(refreshToken);
		if(rtFromRedis == null || !jwtService.isRefreshTokenType(rtFromRedis)) {
			return new RefreshTokenApiPayload(
				new BaseApiPayload(false, "Refresh token invalid (1)"),
				RefreshTokenApiStatusType.REFRESH_TOKEN_INVALID,
				null
			);
		}
		
		Long seconds = getRemainingSeconds(rtFromRedis);
		if (seconds <= 0) {
			return new RefreshTokenApiPayload(
				new BaseApiPayload(false, "Refresh token invalid (2)"),
				RefreshTokenApiStatusType.REFRESH_TOKEN_INVALID,
				null
			);
		}
		
		String authIdStr = jwtService.extractSubject(rtFromRedis);
		UUID authId = authIdStr != null ? UUID.fromString(authIdStr) : UUID.fromString("");
		Auth auth = authRepo.findById(authId).orElse(null);
		if(auth == null) {
			return new RefreshTokenApiPayload(
				new BaseApiPayload(false, "Refresh token invalid (3)"),
				RefreshTokenApiStatusType.REFRESH_TOKEN_INVALID,
				null
			);
		}
		
		String accessToken = jwtService.generateAccessToken(auth.getId(), auth.getUserId(), auth.getRole().getName());
		
		return new RefreshTokenApiPayload(
			new BaseApiPayload(true, "Refresh token successfully"),
			null,
			authMapper.toRefreshTokenPayload(accessToken)
		);
	}
	
	private Long getRemainingSeconds(String token) {
		Date expiration = jwtService.extractExpiration(token);
		long currentMillis = System.currentTimeMillis();
		long expirationMillis = expiration.getTime();
		return (expirationMillis - currentMillis) / 1000;
	}

}
