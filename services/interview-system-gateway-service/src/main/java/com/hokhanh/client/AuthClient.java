package com.hokhanh.client;

import org.springframework.stereotype.Component;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.hokhanh.common.graphql.GraphQLRequest;
import com.hokhanh.common.graphql.GraphQLResponse;

import reactor.core.publisher.Mono;

@Component
public class AuthClient {

	@Autowired
	private WebClient webClient;

	@Value("${spring.graphql.http.path}")
	private String graphqlPath;

	public Mono<Boolean> isTokenBlockedInternal(String accessToken) {
		String query = """
				    query($accessToken: String!) {
				        isTokenBlockedInternal(accessToken: $accessToken)
				    }
				""";

		Map<String, Object> variables = Map.of("accessToken", accessToken);
		GraphQLRequest request = new GraphQLRequest(query, variables);

		return webClient.post().uri(graphqlPath).bodyValue(request).retrieve().bodyToMono(GraphQLResponse.class)
				.flatMap(response -> {
					// Kiểm tra lỗi từ GraphQL
					if (response.errors() != null && !response.errors().isEmpty()) {
						String errorMsg = response.errors().get(0).message();
						return Mono.error(new RuntimeException("GraphQL Error: " + errorMsg));
					}

					Object value = response.data().get("isTokenBlockedInternal");
					if (value instanceof Boolean boolValue) {
						return Mono.just(boolValue);
					} else {
						return Mono.error(new RuntimeException("Unexpected response format"));
					}
				});
	}
}
