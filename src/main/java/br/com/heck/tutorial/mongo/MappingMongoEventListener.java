package br.com.heck.tutorial.mongo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.util.ReflectionUtils;

import br.com.heck.tutorial.mongo.MongoMapping.Reference;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * Classe de MappingMongoEventListener utilizada para realizar as conversões
 * necessarias para salvar os documentos.
 * 
 * @since 1.0
 * @version 1.0
 * @author Paulo Heck
 *
 */
public class MappingMongoEventListener extends AbstractMongoEventListener<Object> {


    private static final String   ID    = "_id";

    private static final String   PONTO = ".";

    @Autowired
    private MongoTemplate         mongoPersistence;

    @Autowired
    private MongoMappingInterface mongoMapping;


    @Override
    public void onBeforeSave(final Object source, final DBObject dbo) {

        DbIdMapFieldCallback callback = new DbIdMapFieldCallback(source, mongoMapping);

        ReflectionUtils.doWithFields(source.getClass(), callback);
        dbo.removeField(ID);
        dbo.put(ID, callback.getIdMap());

        ReflectionUtils.doWithFields(source.getClass(), new DbRefFieldCallback(source, mongoMapping, dbo));
    }


    @Override
    public void onBeforeConvert(final Object source) {
        ReflectionUtils.doWithFields(source.getClass(), new CascadeReflectionUtils(source, mongoMapping));
    }

    /**
     * Classe CascadeReflectionUtils utilizada para identificar relacionamento
     * com necessidade de salva sub documentos.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     */
    private class CascadeReflectionUtils implements ReflectionUtils.FieldCallback {


        Object                source;

        MongoMappingInterface mongoMapping;


        /**
         *
         * Construtor utilizado para informar source e o mongoMapping.
         * 
         * @since 1.0
         * @version 1.0
         * @author Paulo Heck
         *
         * @param source
         * @param mongoMapping
         */
        public CascadeReflectionUtils(Object source, MongoMappingInterface mongoMapping) {

            this.source = source;
            this.mongoMapping = mongoMapping;
        }


        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

            ReflectionUtils.makeAccessible(field);
            String clazz = source.getClass().getName();

            Map<String, MongoMapping.Reference> mongoRefMapping = mongoMapping.getMongoReference();

            if (mongoRefMapping.containsKey(clazz + PONTO + field.getName())) {

                Object object2 = field.get(source);
                if (object2 != null) {
                    for (Object object : (List<?>) object2) {
                        try {
                            mongoPersistence.save(object);

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Classe DbRefFieldCallback utilizada para identificar relacionamento com
     * necessidade de realizar o relacionamento $ref e $id com base nos
     * mapeamentos.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     */
    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {


        private static final String    ID_REF = "$id";

        private static final String    REF    = "$ref";

        private final Object           source;

        private final DBObject         dbo;

        private Map<String, Reference> mongoReference;

        private MongoMappingInterface  mongoMapping;


        /**
         *
         * Construtor utilizado para informar os parametros necessarios para
         * operação.
         * 
         * @since 1.0
         * @version 1.0
         * @author Paulo Heck
         *
         * @param source
         * @param mongoMapping
         * @param dbo
         */
        public DbRefFieldCallback(Object source, MongoMappingInterface mongoMapping, DBObject dbo) {

            super();
            this.source = source;
            this.dbo = dbo;
            this.mongoMapping = mongoMapping;
            mongoReference = mongoMapping.getMongoReference();
        }


        @SuppressWarnings("unchecked")
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

            ReflectionUtils.makeAccessible(field);
            final String clazz = source.getClass().getName();

            if (this.mongoReference.containsKey(clazz + PONTO + field.getName())) {
                List<DBObject> dbObjectDbRefs = new ArrayList<DBObject>();
                MongoMapping.Reference mongoRefMap = this.mongoReference.get(clazz + PONTO + field.getName());

                if (dbo.containsField(field.getName())) {

                    for (DBObject dbObject : (List<DBObject>) dbo.get(field.getName())) {
					
                        DBObject ref = new BasicDBObject();
						
						ref.put(REF, mongoRefMap.getType());
                        dbObjectDbRefs.add(ref);
						
                        if (mongoMapping.getId() != null) {
                            ref.put(ID_REF, dbObject.get(mongoMapping.getId()));
                        }
                        else {
                            ref.put(ID_REF, dbObject.get(ID));
                        }
              
                    }
                    dbo.removeField(field.getName());
                    dbo.put(field.getName(), dbObjectDbRefs);
                }
            }

        }
    }

    /**
     * Classe DbIdMapFieldCallback realizar extração do mappeando do ID.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     */
    private static class DbIdMapFieldCallback implements ReflectionUtils.FieldCallback {


        private final Object                source;

        private final MongoMappingInterface mongoMapping;

        private String                      idMap;


        /**
         * Construtor da classe DbIdMapFieldCallback.
         * 
         * @since 1.0
         * @version 1.0
         * @author Paulo Heck
         *
         * @param source
         * @param mongoMapping
         */
        public DbIdMapFieldCallback(Object source, MongoMappingInterface mongoMapping) {

            super();
            this.source = source;
            this.mongoMapping = mongoMapping;
        }


        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

            ReflectionUtils.makeAccessible(field);

            if (mongoMapping.getId() != null) {

                if (mongoMapping.getId().equals(field.getName())) {
                    field.get(source);
                    idMap = (String) field.get(source);
                }
            }
        }


        public String getIdMap() {
            return idMap;
        }
    }
}
