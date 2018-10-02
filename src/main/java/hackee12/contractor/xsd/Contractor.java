package hackee12.contractor.xsd;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contractor {
    public void passThrough(Class<?> clazz) throws ClassNotFoundException {
        passThrough("", clazz);
    }

    public void passThrough(String path, Class<?> clazz) throws ClassNotFoundException {
        System.out.println(path);
        List<Field> fields = new ArrayList<>();
        List<Field> allFields = getAllFields(fields, clazz);
        for (Field f : allFields) {
            XmlElement xmlElement = f.getDeclaredAnnotation(XmlElement.class);
            if (null != xmlElement) {
                Class<?> type = f.getType();
                String x;
                if (List.class.equals(type)) {
                    x = path + appendList(xmlElement, f);
                    ParameterizedType genericType = (ParameterizedType) f.getGenericType();
                    passThrough(x, Class.forName(genericType.getActualTypeArguments()[0].getTypeName()));
                } else {
                    x = path + appendElement(xmlElement, f);
                    passThrough(x, type);
                }
            }
            XmlAttribute xmlAttribute = f.getDeclaredAnnotation(XmlAttribute.class);
            if (null != xmlAttribute) {
                System.out.println(path + appendAttribute(xmlAttribute, f));
            }
            XmlValue xmlValue = f.getDeclaredAnnotation(XmlValue.class);
            if (null != xmlValue) {
                System.out.println(path + appendValue());
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

    private String appendAttribute(XmlAttribute attribute, Field field) {
        return "/@" + ("##default".equals(attribute.name()) ? field.getName() : attribute.name()) + (attribute.required() ? "(r)" : "");
    }

    private String appendElement(XmlElement element, Field field) {
        return "/" + ("##default".equals(element.name()) ? field.getName() : element.name()) + (element.required() ? "(r)" : "");
    }

    private String appendList(XmlElement list, Field field) {
        return "/" + ("##default".equals(list.name()) ? field.getName() : list.name()) + "[]" + (list.required() ? "(r)" : "");
    }

    private String appendValue() {
        return "/<value>";
    }
}
