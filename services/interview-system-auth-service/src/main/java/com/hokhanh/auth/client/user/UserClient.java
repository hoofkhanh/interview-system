package com.hokhanh.auth.client.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hokhanh.common.graphql.GraphQLRequest;
import com.hokhanh.common.graphql.GraphQLResponse;
import com.hokhanh.common.user.request.CreateOrUpdateUserInput;
import com.hokhanh.common.user.response.CreateOrUpdateUserPayload;
import com.hokhanh.common.user.response.UserByEmailPayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

	private final RestTemplate restTemplate;

	@Value("${user-service.base-url}")
	private String userBaseUrl;

	@Value("${spring.graphql.http.path}")
	private String graphqlPath;

	private final ObjectMapper mapper;

	public UserByEmailPayload userByEmailInternal(String email) {
		String url = userBaseUrl + graphqlPath;

		String query = """
				query($email: String!) {
				    userByEmailInternal(email: $email) {
				        baseUser {
				            id
				            email
				            phoneNumber
				            gender
				            firstName
				            lastName
				            fullName
				            dateOfBirth
				        }
				    }
				}
				""";

		Map<String, Object> variables = Map.of("email", email);
		
		GraphQLRequest request = new GraphQLRequest(query, variables);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GraphQLRequest> requestEntity = new HttpEntity<>(request, headers);

		ResponseEntity<GraphQLResponse> response = restTemplate.postForEntity(url, requestEntity,
				GraphQLResponse.class);

		if (response.getBody() == null) {
			throw new RuntimeException("Empty GraphQL response");
		}

		if (response.getBody().errors() != null && !response.getBody().errors().isEmpty()) {
			String errorMsg = response.getBody().errors().get(0).message();
			throw new RuntimeException("GraphQL Error: " + errorMsg);
		}

		Object value = response.getBody().data().get("userByEmailInternal");
		if (value == null) {
			throw new RuntimeException("No data returned for userByEmailInternal");
		}

		return mapper.convertValue(value, UserByEmailPayload.class);
	}
	
	public CreateOrUpdateUserPayload createOrUpdateUserInternal(CreateOrUpdateUserInput input) {
		String url = userBaseUrl + graphqlPath;

		String mutation = """
		        mutation($input: CreateOrUpdateUserInput!) {
		            createOrUpdateUserInternal(input: $input) {
		                baseUser {
		                    id
		                    email
		                    phoneNumber
		                    gender
		                    firstName
		                    lastName
		                    fullName
		                    dateOfBirth
		                }
		            }
		        }
		    """;

		Map<String, Object> variables = Map.of(
			    "input", mapper.convertValue(input, Map.class)
			);
		
		GraphQLRequest request = new GraphQLRequest(mutation, variables);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GraphQLRequest> requestEntity = new HttpEntity<>(request, headers);

		ResponseEntity<GraphQLResponse> response = restTemplate.postForEntity(url, requestEntity,
				GraphQLResponse.class);

		if (response.getBody() == null) {
			throw new RuntimeException("Empty GraphQL response");
		}

		if (response.getBody().errors() != null && !response.getBody().errors().isEmpty()) {
			String errorMsg = response.getBody().errors().get(0).message();
			throw new RuntimeException("GraphQL Error: " + errorMsg);
		}

		Object value = response.getBody().data().get("createOrUpdateUserInternal");
		if (value == null) {
			throw new RuntimeException("No data returned for createOrUpdateUserInternal");
		}

		return mapper.convertValue(value, CreateOrUpdateUserPayload.class);
	}

}
