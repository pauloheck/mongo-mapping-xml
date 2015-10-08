package com.github.pauloheck.mongo;

import java.util.Map;

import com.github.pauloheck.mongo.MongoMapping.Reference;


public interface MongoMappingInterface {


    public String getId();


    public Map<String, Reference> getMongoReference();
}
