package com.github.pauloheck.mongo.dto;

import java.util.List;


public class TaskDto extends SuperDto {


    private String     task;

    private List<WorkDto> works;


    public TaskDto() {
        super();
    }


    public TaskDto(String sgObjeto, String task, List<WorkDto> works) {
        super();
        super.setSgObjeto(sgObjeto);
        this.task = task;
        this.works = works;
    }


    public String getTask() {
        return task;
    }


    public void setTask(String task) {
        this.task = task;
    }


    public List<WorkDto> getWorks() {
        return works;
    }


    public void setWorks(List<WorkDto> works) {
        this.works = works;
    }


    @Override
    public String toString() {
        return "Task [sgObjeto=" + super.getSgObjeto() + ", task=" + task + ", work=" + works + "]";
    }

}
