package com.hokhanh.question.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hokhanh.common.graphql.GraphQLHeaderContextInterceptor;

@Configuration
@Import({GraphQLHeaderContextInterceptor.class})
public class AutoConfiguration {

}