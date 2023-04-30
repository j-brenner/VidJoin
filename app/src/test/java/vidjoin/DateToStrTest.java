package vidjoin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//

import java.util.GregorianCalendar;

public class DateToStrTest {
    @Test
    void dateToYYyMMmDDd() {
        String real1 = JaDateToStr.toYYyMMmDD(new GregorianCalendar(2020, 0, 1));
        assertEquals(real1, "20y01m01");
    }
}
