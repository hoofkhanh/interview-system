package com.hokhanh.session.client.question;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hokhanh.common.graphql.GraphQLRequest;
import com.hokhanh.common.graphql.GraphQLResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionClient {
	
	private final RestTemplate restTemplate;
	
	@Value("${question-service.base-url}")
	private String questionBaseUrl;

	@Value("${spring.graphql.http.path}")
	private String graphqlPath;
	

	public UUID creatorIdByIdInternal(String id) {
	    String url = questionBaseUrl + graphqlPath;

	    String query = """
	            query($id: ID!) {
	                creatorIdByIdInternal(id: $id)
	            }
	            """;

	    Map<String, Object> variables = Map.of("id", id);

	    GraphQLRequest request = new GraphQLRequest(query, variables);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<GraphQLRequest> requestEntity = new HttpEntity<>(request, headers);

	    ResponseEntity<GraphQLResponse> response = restTemplate.postForEntity(
	            url,
	            requestEntity,
	            GraphQLResponse.class
	    );

	    if (response.getBody() == null) {
	        throw new RuntimeException("Empty GraphQL response");
	    }

	    if (response.getBody().errors() != null && !response.getBody().errors().isEmpty()) {
	        String errorMsg = response.getBody().errors().get(0).message();
	        throw new RuntimeException("GraphQL Error: " + errorMsg);
	    }

	    Object value = response.getBody().data().get("creatorIdByIdInternal");
	   

	    // Chuyển đổi sang UUID
	    return value != null ? UUID.fromString(value.toString()): null;
	}


}
