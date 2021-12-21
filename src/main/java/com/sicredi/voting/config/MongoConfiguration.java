package com.sicredi.voting.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.sicredi.voting.infrastructure.associate.AssociateRepository;
import com.sicredi.voting.infrastructure.session.SessionRepository;
import com.sicredi.voting.infrastructure.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = {AssociateRepository.class, SubjectRepository.class, SessionRepository.class})
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${mongo.database.name}")
    private String databaseName;

    @Value("${mongo.url}")
    private String mongoUrl;

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoUrl);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }
}
