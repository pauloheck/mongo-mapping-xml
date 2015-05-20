package br.com.heck.tutorial.mongo;

import java.util.Map;

import br.com.heck.tutorial.mongo.MongoMapping.Reference;


public interface MongoMappingInterface {


    public String getId();


    public Map<String, Reference> getMongoReference();
}
