package com.cybersource.demo.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:test_cybs.properties")
public class PropConfiguration {
    @Primary
    @Bean
    public Properties merchantProperties() throws IOException {
        Properties props = new Properties();
        props.load(PropConfiguration.class.getClassLoader().getResourceAsStream("test_cybs.properties"));
        return props;
    }
}
