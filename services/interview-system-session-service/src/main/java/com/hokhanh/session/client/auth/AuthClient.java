package com.hokhanh.session.client.auth;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hokhanh.common.graphql.GraphQLRequest;
import com.hokhanh.common.graphql.GraphQLResponse;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class AuthClient {

	private final RestTemplate restTemplate;

	@Value("${auth-service.base-url}")
	private String authBaseUrl;

	@Value("${spring.graphql.http.path}")
	private String graphqlPath;

	private final ObjectMapper mapper;
	
	public List<BaseAuthPayload> authsByUserId(List<String> userIds) {
	    String url = authBaseUrl + graphqlPath;

	    String query = """
	            query($userIds: [ID!]!) {
	                authsByUserId(userIds: $userIds) {
	    		        id
	                    userId
	                    role {
	                        id
	                        name
	                    }
	                }
	            }
	        """;

	    Map<String, Object> variables = Map.of("userIds", userIds);
	    GraphQLRequest request = new GraphQLRequest(query, variables);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<GraphQLRequest> requestEntity = new HttpEntity<>(request, headers);

	    ResponseEntity<GraphQLResponse> response =
	            restTemplate.postForEntity(url, requestEntity, GraphQLResponse.class);

	    if (response.getBody() == null) {
	        throw new RuntimeException("Empty GraphQL response from auth service");
	    }

	    if (response.getBody().errors() != null && !response.getBody().errors().isEmpty()) {
	        String errorMsg = response.getBody().errors().get(0).message();
	        throw new RuntimeException("GraphQL Error (auth): " + errorMsg);
	    }

	    Object value = response.getBody().data().get("authsByUserId");
	    if (value == null) {
	        return List.of();
	    }

	    return mapper.convertValue(
	            value,
	            new TypeReference<List<BaseAuthPayload>>() {}
	    );
	}

}
