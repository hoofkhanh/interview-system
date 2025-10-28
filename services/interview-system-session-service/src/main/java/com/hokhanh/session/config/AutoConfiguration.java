package com.hokhanh.session.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hokhanh.common.config.RestTemplateConfiguration;
import com.hokhanh.common.graphql.GraphQLHeaderContextInterceptor;

@Configuration
@Import({GraphQLHeaderContextInterceptor.class, RestTemplateConfiguration.class})
public class AutoConfiguration {

}