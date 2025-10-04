package com.hokhanh.common.graphql;

import java.util.Map;

public record GraphQLRequest(String query, Map<String, Object> variables
) {

}