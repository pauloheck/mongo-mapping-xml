package br.com.heck.tutorial.mongo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;




/**
 * Classe MongoMapping utilizada realizar o load dos dados XML necessarias para
 * salvar os documentos.
 * 
 * @since 1.0
 * @version 1.0
 * @author Paulo Heck
 *
 */
@Service
public class MongoMapping implements MongoMappingInterface {


    private static final String    PONTO                                    = ".";

    private static final String    TYPE                                     = "type";

    private static final String    ATTRUBUTE                                = "attrubute";

    private static final String    CLASS                                    = "class";

    private static final String    MONGO_MAPPING_REFERENCES_MONGO_REFERENCE = "//mongoMapping//references//mongoReference";

    private static final String    MONGO_MAPPING_ID_NAME_VALUE              = "//mongoMapping//id/name//@value";

    private Map<String, Reference> mongoReference;

    private String                 id;

    private String                 mongoMappingXml;


    /**
     * Classe MongoMapping utilizada realizar o load dos dados XML necessarias
     * para salvar os documentos.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     */
    public MongoMapping(String mongoMappingXml) throws Exception {

        super();
        try {
            this.mongoMappingXml = mongoMappingXml;
            
            loadMappingXML();
        }
        catch (Exception e) {

        //    throw new RepositoriosException(e, RepositoriosExceptionType.PSE_PAAS_REPOSITORIOS_6);
            throw e;
        }

    }


    /**
     *
     * Metodo utilizado para realizar o carrgementos dos dados de mappeamento do
     * arquivo xml para objetos java
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     * @throws Exception
     */
    private void loadMappingXML() throws Exception {

        ByteArrayInputStream bytesMongoMapping = new ByteArrayInputStream(getFileToByte(mongoMappingXml));

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        Document doc = domFactory.newDocumentBuilder().parse(bytesMongoMapping);
        XPath xpath = XPathFactory.newInstance().newXPath();

        xPathgetId(doc, xpath);
        xpathMappingReference(doc, xpath);
    }


    /**
     *
     * Metodo utilizado para realizar o mapeamento dos dados de referencia para
     * relacionamentos.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     * @param doc
     * @param xpath
     * @throws XPathExpressionException
     */
    private void xpathMappingReference(Document doc, XPath xpath) throws XPathExpressionException {

        XPathExpression compile = xpath.compile(MONGO_MAPPING_REFERENCES_MONGO_REFERENCE);
        NodeList mongoRef = (NodeList) compile.evaluate(doc, XPathConstants.NODESET);
        mongoReference = new HashMap<String, Reference>();

        for (int i = 0; i < mongoRef.getLength(); i++) {
            String clazz = ((Element) mongoRef.item(i)).getAttribute(CLASS);
            String attrubute = ((Element) mongoRef.item(i)).getAttribute(ATTRUBUTE);
            String type = ((Element) mongoRef.item(i)).getAttribute(TYPE);

            mongoReference.put(clazz + PONTO + attrubute, new Reference(clazz, attrubute, type));
        }
    }


    /**
     *
     * Metodo utilizado para realizar o mapeamento do id.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     * @param doc
     * @param xpath
     * @throws XPathExpressionException
     */
    private void xPathgetId(Document doc, XPath xpath) throws XPathExpressionException {

        XPathExpression xPathExpression = xpath.compile(MONGO_MAPPING_ID_NAME_VALUE);
        id = (String) xPathExpression.evaluate(doc, XPathConstants.STRING);
    }


    /**
     *
     * Metodo utilizado para carregar o arquivo de que contem os dados de
     * maperamento.
     * 
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     * @param path
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private byte[] getFileToByte(String path) throws IOException, URISyntaxException {

        byte[] bytes = Files.readAllBytes(Paths.get(MongoMapping.class.getClassLoader().getResource(path).toURI()));

        return bytes;
    }


    public String getId() {
        return id;
    }


    public Map<String, Reference> getMongoReference() {
        return mongoReference;
    }

    /**
     * 
     * Classe Reference utilizada para mapeamento das referencias de
     * relacionamento.
     *
     * @since 1.0
     * @version 1.0
     * @author Paulo Heck
     *
     */
    class Reference {


        private String clazz;

        private String attrubute;

        private String type;


        /**
         *
         * Construtor utilizado para criar objetos de referencia de
         * relacionamento.
         * 
         * @since 1.0
         * @version 1.0
         * @author Paulo Heck
         *
         * @param clazz
         * @param attrubute
         * @param type
         */
        public Reference(String clazz, String attrubute, String type) {

            super();
            this.clazz = clazz;
            this.attrubute = attrubute;
            this.type = type;
        }


        public String getClazz() {
            return clazz;
        }


        public void setClazz(String clazz) {
            this.clazz = clazz;
        }


        public String getAttrubute() {
            return attrubute;
        }


        public void setAttrubute(String attrubute) {
            this.attrubute = attrubute;
        }


        public String getType() {
            return type;
        }


        public void setType(String type) {
            this.type = type;
        }

    }

}
