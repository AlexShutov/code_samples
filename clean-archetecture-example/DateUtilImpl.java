
import com.lodoss.util.DateUtil;

import java.util.Calendar;

/**
 * {@inheritDoc}
 */
public class DateUtilImpl implements DateUtil {

    /**
     * {@inheritDoc}
     */
    @Override
    public long getStartOfTheDay(long day) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(day);
        instance.set(Calendar.AM_PM, Calendar.AM);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        return instance.getTimeInMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getEndOfTheDay(long day) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(day);
        instance.set(Calendar.AM_PM, Calendar.AM);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTimeInMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getStartOfTheWeek(long dateTime) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(dateTime);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        instance.set(Calendar.AM_PM, Calendar.AM);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        return instance.getTimeInMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getEndOfWeek(long dateTime) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(dateTime);
        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        instance.set(Calendar.AM_PM, Calendar.AM);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTimeInMillis();
    }
}
