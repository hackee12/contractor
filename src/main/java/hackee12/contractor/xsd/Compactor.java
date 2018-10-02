package hackee12.contractor.xsd;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compactor {
    public void start(Class<?> clazz) throws ClassNotFoundException {
        Line parent = new Line.Builder("", Line.Qualifier.ELEMENT).build();
        process(parent, clazz);
    }

    public void process(Line parent, Class clazz) throws ClassNotFoundException {
        System.out.println(parent);
        List<Field> fields = new ArrayList<>();
        List<Field> allFields = getAllFields(fields, clazz);
        for (Field field : allFields) {
            XmlElement xmlElement = field.getDeclaredAnnotation(XmlElement.class);
            if (null != xmlElement) {
                Class<?> type = field.getType();
                if (List.class.equals(type)) {
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    Class<?> genericClass = Class.forName(genericType.getActualTypeArguments()[0].getTypeName());
                    String elementName = "##default".equals(xmlElement.name()) ? field.getName() : xmlElement.name();
                    Line list = new Line.Builder(elementName, Line.Qualifier.LIST)
                            .parent(parent)
                            .required(xmlElement.required())
                            .build();
                    process(list, genericClass);
                } else {
                    String elementName = "##default".equals(xmlElement.name()) ? field.getName() : xmlElement.name();
                    Line element = new Line.Builder(elementName, Line.Qualifier.ELEMENT)
                            .parent(parent)
                            .required(xmlElement.required())
                            .build();
                    process(element, type);
                }
            }
            XmlAttribute xmlAttribute = field.getDeclaredAnnotation(XmlAttribute.class);
            if (null != xmlAttribute) {
                String attributeName = "##default".equals(xmlAttribute.name()) ? field.getName() : xmlAttribute.name();
                Line attribute = new Line.Builder(attributeName, Line.Qualifier.ATTRIBUTE)
                        .parent(parent)
                        .required(xmlAttribute.required())
                        .build();
                System.out.println(attribute);
            }
            XmlValue xmlValue = field.getDeclaredAnnotation(XmlValue.class);
            if (null != xmlValue) {
                Line value = new Line.Builder("", Line.Qualifier.VALUE)
                        .parent(parent)
                        .build();
                System.out.println(value);
            }
        }
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}
