package hackee12.contractor.xsd;

import hackee12.contractor.xsd.sample.Shiporder;
import org.junit.Test;

public class CompactorTest {

    @Test
    public void test() throws ClassNotFoundException {
        new Compactor().start(Shiporder.class);
    }
}