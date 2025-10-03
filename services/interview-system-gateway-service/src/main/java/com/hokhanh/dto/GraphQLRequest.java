package com.hokhanh.dto;

import java.util.Map;

public record GraphQLRequest(String query, Map<String, Object> variables
) {

}