package br.com.heck.tutorial.mongo.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.com.heck.tutorial.mongo.MappingMongoEventListener;
import br.com.heck.tutorial.mongo.MongoMapping;

import com.mongodb.MongoClient;


@Configuration
public class AppConfig {


    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient("localhost");
    }


    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {

        return new MongoTemplate(mongoClient(), "test");
    }


    @Bean
    public MongoMapping mongoMapping() throws Exception {
        return new MongoMapping("mongoMapping.xml");
    }


    @Bean
    public MappingMongoEventListener mappingMongoEventListener() {
        return new MappingMongoEventListener();
    }

}
