package com.hokhanh.user.response;

import java.time.LocalDate;
import java.util.UUID;

public record BaseUserPayload(
	UUID id,
	String email,
	String phoneNumber,
	Boolean gender,
	String firstName,
	String lastName,
	String fullName,
	LocalDate dateOfBirth
) {

}
