package hackee12.contractor.xsd;

import hackee12.contractor.xsd.sample.Shiporder;
import org.junit.Test;

public class ContractorTest {

    @Test
    public void test() throws ClassNotFoundException {
        new Contractor().passThrough(Shiporder.class);
    }
}