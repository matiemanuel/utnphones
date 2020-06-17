package edu.utn.utnphones.config;

import edu.utn.utnphones.session.SessionBackOfficeFilter;
import edu.utn.utnphones.session.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@EnableScheduling
public class Configuration {


    @Autowired
    SessionBackOfficeFilter sessionBackOfficeFilter;

    @Autowired
    SessionFilter sessionUserFilter;


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
}
