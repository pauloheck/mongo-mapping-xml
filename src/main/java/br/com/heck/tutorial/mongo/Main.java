package br.com.heck.tutorial.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.com.heck.tutorial.mongo.config.AppConfig;
import br.com.heck.tutorial.mongo.dto.PersonDto;
import br.com.heck.tutorial.mongo.dto.TaskDto;
import br.com.heck.tutorial.mongo.dto.WorkDto;

import com.fasterxml.jackson.core.JsonProcessingException;


public class Main {


    @SuppressWarnings("resource")
    public static void main(String[] args) throws JsonProcessingException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        MongoTemplate mongoTemplate = ctx.getBean("mongoTemplate", MongoTemplate.class);
        List<TaskDto> tasks = new ArrayList<TaskDto>();
        List<WorkDto> works = new ArrayList<WorkDto>();
        WorkDto work = new WorkDto("31", "Work 1");
        works.add(work);
        tasks.add(new TaskDto("21", "task 1", works));
        tasks.add(new TaskDto("22", "task 2", works));
        PersonDto person = new PersonDto("1", "Heck", 34111, tasks);
        mongoTemplate.save(person);


        // List<PersonDto> persons = mongoTemplate.findAll(PersonDto.class);

    }
}
