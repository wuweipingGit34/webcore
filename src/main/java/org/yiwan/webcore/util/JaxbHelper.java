package org.yiwan.webcore.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

/**
 * @author Kenny Wang
 */
public class JaxbHelper {
    private final static Logger logger = LoggerFactory.getLogger(JaxbHelper.class);

    /**
     * xml default encoding is UTF-8
     *
     * @param obj
     * @return xml string
     */
    public static String marshal(Object obj) {
        return marshal(obj, "UTF-8");
    }

    public static String marshalWithoutXmlRootElement(Object obj) {
        return marshalWithoutXmlRootElement(obj, "UTF-8");
    }

    /**
     * marshal object into a xml file with default encoding is UTF-8
     *
     * @param obj
     * @param file
     */
    public static void marshal(Object obj, File file) {
        String xml = marshal(obj, "UTF-8");
        try {
            FileUtils.write(file, xml);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * convert Java bean to xml string
     *
     * @param obj
     * @param encoding
     * @return xml string
     */
    public static String marshal(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static String marshalWithoutXmlRootElement(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            StringWriter writer = new StringWriter();
            //QName helps to marshal object without @XmlRootElement annotation
            QName q = new QName(Helper.firstLetterToLowerCase(obj.getClass().getSimpleName()));
            JAXBElement jaxbElement = new JAXBElement(q, obj.getClass(), obj);
            marshaller.marshal(jaxbElement, writer);
            result = writer.toString();
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * convert xml string to java bean without schema validation
     *
     * @param xml
     * @param clazz
     * @return object in generic type
     */
    public static <T> T unmarshal(String xml, Class<T> clazz) {
        return unmarshal(xml, null, clazz);
    }

    /**
     * convert file to java bean without schema validation
     *
     * @param file
     * @param clazz
     * @return object in generic type
     */
    public static <T> T unmarshal(File file, Class<T> clazz) {
        return unmarshal(file, null, clazz);
    }

    /**
     * convert input stream to java bean without schema validation
     *
     * @param in
     * @param clazz
     * @return object in generic type
     */
    public static <T> T unmarshal(InputStream in, Class<T> clazz) {
        return unmarshal(in, null, clazz);
    }

    /**
     * convert file to java bean
     *
     * @param file
     * @param xsd
     * @param clazz
     * @return object in generic type
     */
    public static <T> T unmarshal(File file, InputStream xsd, Class<T> clazz) {
        String xml = "";
        try {
            xml = FileUtils.readFileToString(file, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return unmarshal(xml, xsd, clazz);
    }

    /**
     * convert input stream to java bean
     *
     * @param in
     * @param xsd
     * @param clazz
     * @return object in generic type
     */
    public static <T> T unmarshal(InputStream in, InputStream xsd, Class<T> clazz) {
        String xml = "";
        try {
            xml = IOUtils.toString(in, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return unmarshal(xml, xsd, clazz);
    }

    /**
     * convert xml string java bean
     *
     * @param xml
     * @param xsd
     * @param clazz
     * @return object in generic type
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(String xml, InputStream xsd, Class<T> clazz) {
        T t = null;
        ValidationEventCollector validation = new ValidationEventCollector();
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            if (xsd != null) {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);// http://www.w3.org/2001/XMLSchema
                Schema schema = factory.newSchema(new StreamSource(xsd));
                unmarshaller.setSchema(schema);
            }
            unmarshaller.setEventHandler(validation);
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException | SAXException e) {
            logger.error("xml unmarshal exception", e);
            if (validation != null && validation.hasEvents()) {
                for (ValidationEvent ve : validation.getEvents()) {
                    String msg = ve.getMessage();
                    ValidationEventLocator vel = ve.getLocator();
                    int line = vel.getLineNumber();
                    int column = vel.getColumnNumber();
                    logger.error("at line " + line + ", column " + column + ", " + msg + "\n" + xml);
                }
            }
        }

        return t;
    }

}
