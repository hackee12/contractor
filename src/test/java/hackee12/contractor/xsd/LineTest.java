package hackee12.contractor.xsd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LineTest {

    @Test
    public void parentElement() {
        Line parent = new Line.Builder("parent", Line.Qualifier.ELEMENT)
                .build();
        String message = "line.toString() doesn't match";
        String expected = "'parent','ELEMENT','','optional',";
        String actual = parent.toString();
        assertEquals(message, expected, actual);
    }

    @Test
    public void child() {
        Line parent = new Line.Builder("parent", Line.Qualifier.ELEMENT)
                .noParent()
                .required()
                .build();
        Line child = new Line.Builder("child", Line.Qualifier.ELEMENT)
                .parent(parent)
                .optional()
                .build();
        String message = "line.toString() doesn't match";
        String expected = "'parent/child','ELEMENT','','optional',";
        String actual = child.toString();
        assertEquals(message, expected, actual);
    }

    @Test
    public void list() {
        Line parent = new Line.Builder("parent", Line.Qualifier.LIST).build();
        String message = "line.toString() doesn't match";
        String expected = "'parent[]','LIST','','optional',";
        String actual = parent.toString();
        assertEquals(message, expected, actual);
    }

    @Test
    public void attribute() {
        Line parent = new Line.Builder("parent", Line.Qualifier.ELEMENT).build();
        Line child = new Line.Builder("child", Line.Qualifier.ATTRIBUTE)
                .parent(parent)
                .build();
        String message = "line.toString() doesn't match";
        String expected = "'parent/@child','ATTRIBUTE','','optional',";
        String actual = child.toString();
        assertEquals(message, expected, actual);
    }
}