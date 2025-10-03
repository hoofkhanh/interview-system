package com.hokhanh.user.handler;

import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GraphQLValidationExceptionHandler {

	@GraphQlExceptionHandler(ConstraintViolationException.class)
    public GraphQLError handleConstraintViolationException(ConstraintViolationException ex) {
        List<Map<String, String>> violations = ex.getConstraintViolations().stream()
            .map(violation -> {
                String field = violation.getPropertyPath().toString(); 
                String message = violation.getMessage();
                return Map.of("field", field, "message", message);
            })
            .toList();

        return GraphqlErrorBuilder.newError()
            .message("Validation failed")
            .extensions(
            		Map.of("constraintViolations", violations))
            .build();
    }
}
