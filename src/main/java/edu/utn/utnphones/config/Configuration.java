package edu.utn.utnphones.config;

import edu.utn.utnphones.session.SessionBackOfficeFilter;
import edu.utn.utnphones.session.SessionFilter;
import edu.utn.utnphones.session.SessionInfrastructureFilter;
import edu.utn.utnphones.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
@EnableScheduling
public class Configuration {

    @Autowired
    SessionBackOfficeFilter sessionBackOfficeFilter;

    @Autowired
    SessionInfrastructureFilter sessionInfrastructureFilter;

    @Autowired
    SessionFilter sessionUserFilter;

    @Bean
    public RestUtils getRestUtil(){
        return new RestUtils();
    }

    @Bean
    public FilterRegistrationBean userFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionUserFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean backOfficeFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionBackOfficeFilter);
        registration.addUrlPatterns("/backoffice/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean infrastructureFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionInfrastructureFilter);
        registration.addUrlPatterns("/infrastructure/*");
        return registration;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(
                        RequestHandlerSelectors
                                .basePackage("edu.utn.utnphones.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}

