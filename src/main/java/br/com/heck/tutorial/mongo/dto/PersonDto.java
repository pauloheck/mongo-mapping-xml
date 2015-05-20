package br.com.heck.tutorial.mongo.dto;

import java.util.List;


public class PersonDto extends SuperDto {


    private String     name;

    private int        age;

    private List<TaskDto> tasks;


    public PersonDto() {
        super();
    }


    public PersonDto(String sgObjeto, String name, int age, List<TaskDto> tasks) {
        super();
        super.setSgObjeto(sgObjeto);
        this.name = name;
        this.age = age;
        this.tasks = tasks;
    }


    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    public List<TaskDto> getTasks() {
        return tasks;
    }


    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "Person [sgObjeto=" + super.getSgObjeto() + ", name=" + name + ", age=" + age + ", tasks=" + tasks + "]";
    }

    
}
