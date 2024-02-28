package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "config")
@SpringBootApplication
@EntityScan("entity")
@PropertySource("classpath:application.properties")
public class App
{
    public static void main( String[] args )
    {

        SpringApplication.run(App.class);
    }
}
