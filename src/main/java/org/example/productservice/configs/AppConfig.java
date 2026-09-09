package org.example.productservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//@Configuration marks a class as a Spring configuration class. Spring scans it and treats it as a source of bean definitions.
@Configuration
public class AppConfig {
    // @Bean marks a method inside a @Configuration class whose return  value should be managed by Spring as an object in the
    // application context. For example, in AppConfig, a RestTemplate   created in a @Bean method becomes a reusable Spring-managed
    // instance you can inject anywhere with @Autowired or constructor  injection.
    @Bean
//    RestTemplate is a Spring client for calling other HTTP APIs from     your Java app. You use it when your service needs to send GET,       POST, PUT, DELETE, etc. requests to another service and read the     response.
    public RestTemplate createRestBeanTemplate(){
       return new RestTemplate();
    }
}
