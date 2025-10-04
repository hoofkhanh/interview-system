package com.hokhanh.common.graphql;

import java.util.List;
import java.util.Map;

public record GraphQLResponse(Map<String, Object> data, List<GraphQLError> errors) {

}