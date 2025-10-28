package com.hokhanh.auth.mapper;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hokhanh.auth.model.Auth;
import com.hokhanh.auth.model.Role;
import com.hokhanh.auth.request.signup.SignupInterviewerInput;
import com.hokhanh.auth.response.common.BaseAuthPayload;
import com.hokhanh.auth.response.common.RolePayload;
import com.hokhanh.auth.response.refreshToken.RefreshTokenPayload;
import com.hokhanh.auth.response.signin.SigninPayload;
import com.hokhanh.auth.response.signup.SignupCandidatePayload;
import com.hokhanh.auth.response.signup.SignupInterviewerPayload;
import com.hokhanh.auth.response.signup.VerifyInterviewerSignupOtpPayload;
import com.hokhanh.common.user.response.CreateOrUpdateUserPayload;

@Service
public class AuthMapper {

	public SignupInterviewerPayload toSignupInterviewerPayload(LocalDateTime otpSentAt, Long otpExpirationMinutes) {
		return new SignupInterviewerPayload(otpSentAt,otpExpirationMinutes);
	}
	
	public Auth toAuth(Role role, UUID userId, SignupInterviewerInput input) {
		return Auth.builder()
				.role(role)
				.userId(userId)
				.username(input != null? input.username(): null)
				.password(input != null? input.password(): null)
				.build();
	}
	
	public VerifyInterviewerSignupOtpPayload toVerifyInterviewerSignupOtpPayload(Auth auth, String accessToken, String refreshToken, CreateOrUpdateUserPayload payload) {
		return new VerifyInterviewerSignupOtpPayload(
			toBaseAuthPayload(auth, payload),
			accessToken,
			refreshToken
		);
	}
	
	public SignupCandidatePayload toSignupCandidatePayload(Auth auth, String accessToken, String refreshToken, CreateOrUpdateUserPayload payload) {
		return new SignupCandidatePayload(
			toBaseAuthPayload(auth, payload),
			accessToken,
			refreshToken
		);
	}
	
	private BaseAuthPayload toBaseAuthPayload(Auth auth, CreateOrUpdateUserPayload payload) {
	    return new BaseAuthPayload(
	        auth.getId(),
	        new RolePayload(auth.getRole().getId(), auth.getRole().getName()),
	        auth.getUserId(),
	        payload.baseUser().email(),
	        payload.baseUser().fullName()
	    );
	}

	public SigninPayload toSigninPayload(String accessToken, String refreshToken) {
		return new SigninPayload(accessToken, refreshToken);
	}

	public RefreshTokenPayload toRefreshTokenPayload(String accessToken) {
		return new RefreshTokenPayload(accessToken);
	}
}
