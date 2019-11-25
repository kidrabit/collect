package com.pcn.manager.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletFilterConfig {
	
    @Bean
    public FilterRegistrationBean<Filter> siteMeshFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<Filter>();
        filter.setFilter(new SiteMeshFilter());
        return filter;
    }
}
