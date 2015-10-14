package com.github.pauloheck.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.github.pauloheck.mongo.config.AppConfig;
import com.github.pauloheck.mongo.dto.PersonDto;
import com.github.pauloheck.mongo.dto.TaskDto;
import com.github.pauloheck.mongo.dto.WorkDto;

import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class MappingTest {

  @Test
  public void testConfigMapping() throws Exception {

    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MongoTemplate mongoTemplate = ctx.getBean("mongoTemplate", MongoTemplate.class);
    List<TaskDto> tasks = new ArrayList<TaskDto>();
    List<WorkDto> works = new ArrayList<WorkDto>();
    WorkDto work = new WorkDto("31", "Work 1");
    works.add(work);
    tasks.add(new TaskDto("21", "task 1", works));
    tasks.add(new TaskDto("22", "task 2", works));
    PersonDto person = new PersonDto("1", "Heck", 34111, tasks);

    //TODO: Mock it to test without database dependecy.
    //mongoTemplate.save(person);
    //List<PersonDto> persons = mongoTemplate.findAll(PersonDto.class);
    //System.out.println(persons);

    assertEquals(person, person);
  }
}
