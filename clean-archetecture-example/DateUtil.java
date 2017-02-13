/**
 * Common utility methods for processing dates / times
 */
public interface DateUtil {

    /**
     * Provides the same date as 'day', but sets the time to the 00:00:00
     * @param day target day
     * @return milliseconds of the beginning of the day
     */
    long getStartOfTheDay(long day);

    /**
     * Provides the same date as 'day', but sets the time the 23:59:59
     * @param day target day
     * @return milliseconds of the end of the day
     */
    long getEndOfTheDay(long day);

    /**
     * Provides the first day of the week for 'dateTime', with time 00:00:00
     * @param dateTime time within the week
     * @return milliseconds of the beginning of the week
     */
    long getStartOfTheWeek(long dateTime);

    /**
     * Provides the last day of the week for 'dateTime', with time 23:59:59
     * @param dateTime time within the week
     * @return milliseconds of the end of the week
     */
    long getEndOfWeek(long dateTime);
}
