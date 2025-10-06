package com.hokhanh.auth.client.user;

import org.springframework.stereotype.Service;

import com.hokhanh.auth.request.signup.BaseSignupInput;
import com.hokhanh.common.user.request.CreateOrUpdateUserInput;

@Service
public class UserClientMapper {

	public CreateOrUpdateUserInput toCreateOrUpdateUserInput(BaseSignupInput baseSignup) {
		return new CreateOrUpdateUserInput(
			baseSignup.email(),
			baseSignup.phoneNumber(),
			baseSignup.gender(),
			baseSignup.firstName(),
			baseSignup.lastName(),
			baseSignup.fullName(),
			baseSignup.dateOfBirth()
		);
	}
}
