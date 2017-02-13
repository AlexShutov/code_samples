import com.lodoss.util.impl.DateUtilImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DateUtilImpl}
 */
@RunWith(MockitoJUnitRunner.class)
public class DateUtilImplTest {

    @Test
    public void test_getStartOfTheDay(){
        DateUtilImpl impl = new DateUtilImpl();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.AM_PM, Calendar.AM);
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);

        long targetStartOfTheDay = impl.getStartOfTheDay(instance.getTimeInMillis());

        instance.setTimeInMillis(targetStartOfTheDay);
        assertEquals(instance.get(Calendar.HOUR), 0);
        assertEquals(instance.get(Calendar.MINUTE), 0);
        assertEquals(instance.get(Calendar.SECOND), 0);
        assertEquals(instance.get(Calendar.DAY_OF_WEEK), dayOfWeek);
    }

    @Test
    public void test_GetEndOfTheDay(){
        DateUtilImpl impl = new DateUtilImpl();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.AM_PM, Calendar.AM);
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);

        long targetEndOfTheDay = impl.getEndOfTheDay(instance.getTimeInMillis());

        instance.setTimeInMillis(targetEndOfTheDay);
        assertEquals(instance.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(instance.get(Calendar.MINUTE), 59);
        assertEquals(instance.get(Calendar.SECOND), 59);
        assertEquals(instance.get(Calendar.DAY_OF_WEEK), dayOfWeek);
    }

    @Test
    public void test_getStartOfTheWeek(){
        DateUtilImpl impl = new DateUtilImpl();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.AM_PM, Calendar.AM);

        long startOfTheWeek = impl.getStartOfTheWeek(instance.getTimeInMillis());
        instance.setTimeInMillis(startOfTheWeek);

        assertEquals(instance.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
        assertEquals(instance.get(Calendar.HOUR_OF_DAY), 0);
        assertEquals(instance.get(Calendar.MINUTE), 0);
        assertEquals(instance.get(Calendar.SECOND), 0);
    }

    @Test
    public void test_getEndOfWeek(){
        DateUtilImpl impl = new DateUtilImpl();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.AM_PM, Calendar.AM);

        long endOfWeek = impl.getEndOfWeek(instance.getTimeInMillis());
        instance.setTimeInMillis(endOfWeek);

        assertEquals(instance.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
        assertEquals(instance.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(instance.get(Calendar.MINUTE), 59);
        assertEquals(instance.get(Calendar.SECOND), 59);
    }
}