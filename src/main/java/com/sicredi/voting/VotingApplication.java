package com.sicredi.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoReactiveAutoConfiguration.class})
@SpringBootApplication
public class VotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
    }
}
