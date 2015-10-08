package com.github.pauloheck.mongo.dto;

public class WorkDto extends SuperDto {


    private String work;


    public WorkDto() {
        super();
    }


    public String getWork() {
        return work;
    }


    public WorkDto(String sgObjeto, String work) {
        super();
        super.setSgObjeto(sgObjeto);

        this.work = work;
    }


    public void setWork(String work) {
        this.work = work;
    }


    @Override
    public String toString() {
        return "Work [sgObjeto=" + super.getSgObjeto() + ", work=" + work + "]";
    }

}
