package sun.util.locale.provider;

import static java.util.Calendar.*;
import java.util.Locale;
import java.util.Map;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CalendarNameProvider;

/**
 * {@code CalendarDataUtility} is a utility class for calling the
 * {@link CalendarDataProvider} methods.
 *
 * @author Masayoshi Okutsu
 * @author Naoto Sato
 */
public class CalendarDataUtility {
    public final static String FIRST_DAY_OF_WEEK = "firstDayOfWeek";
    public final static String MINIMAL_DAYS_IN_FIRST_WEEK = "minimalDaysInFirstWeek";

    // No instantiation
    private CalendarDataUtility() {
    }

    public static int retrieveFirstDayOfWeek(Locale locale) {
        LocaleServiceProviderPool pool =
                LocaleServiceProviderPool.getPool(CalendarDataProvider.class);
        Integer value = pool.getLocalizedObject(CalendarWeekParameterGetter.INSTANCE,
                                                locale, FIRST_DAY_OF_WEEK);
        return (value != null && (value >= SUNDAY && value <= SATURDAY)) ? value : SUNDAY;
    }

    public static int retrieveMinimalDaysInFirstWeek(Locale locale) {
        LocaleServiceProviderPool pool =
                LocaleServiceProviderPool.getPool(CalendarDataProvider.class);
        Integer value = pool.getLocalizedObject(CalendarWeekParameterGetter.INSTANCE,
                                                locale, MINIMAL_DAYS_IN_FIRST_WEEK);
        return (value != null && (value >= 1 && value <= 7)) ? value : 1;
    }

    public static String retrieveFieldValueName(String id, int field, int value, int style, Locale locale) {
        LocaleServiceProviderPool pool =
                LocaleServiceProviderPool.getPool(CalendarNameProvider.class);
        return pool.getLocalizedObject(CalendarFieldValueNameGetter.INSTANCE, locale, normalizeCalendarType(id),
                                       field, value, style, false);
    }

    public static String retrieveJavaTimeFieldValueName(String id, int field, int value, int style, Locale locale) {
        LocaleServiceProviderPool pool =
                LocaleServiceProviderPool.getPool(CalendarNameProvider.class);
        String name;
        name = pool.getLocalizedObject(CalendarFieldValueNameGetter.INSTANCE, locale, normalizeCalendarType(id),
                                       field, value, style, true);
        if (name == null) {
            name = pool.getLocalizedObject(CalendarFieldValueNameGetter.INSTANCE, locale, normalizeCalendarType(id),
                                           field, value, style, false);
        }
        return name;
    }

    public static Map<String, Integer> retrieveFieldValueNames(String id, int field, int style, Locale locale) {
        LocaleServiceProviderPool pool =
            LocaleServiceProviderPool.getPool(CalendarNameProvider.class);
        return pool.getLocalizedObject(CalendarFieldValueNamesMapGetter.INSTANCE, locale,
                                       normalizeCalendarType(id), field, style, false);
    }

    public static Map<String, Integer> retrieveJavaTimeFieldValueNames(String id, int field, int style, Locale locale) {
        LocaleServiceProviderPool pool =
            LocaleServiceProviderPool.getPool(CalendarNameProvider.class);
        Map<String, Integer> map;
        map = pool.getLocalizedObject(CalendarFieldValueNamesMapGetter.INSTANCE, locale,
                                       normalizeCalendarType(id), field, style, true);
        if (map == null) {
            map = pool.getLocalizedObject(CalendarFieldValueNamesMapGetter.INSTANCE, locale,
                                           normalizeCalendarType(id), field, style, false);
        }
        return map;
    }

    static String normalizeCalendarType(String requestID) {
        String type;
        if (requestID.equals("gregorian") || requestID.equals("iso8601")) {
            type = "gregory";
        } else if (requestID.startsWith("islamic")) {
            type = "islamic";
        } else {
            type = requestID;
        }
        return type;
    }

    /**
     * Obtains a localized field value string from a CalendarDataProvider
     * implementation.
     */
    private static class CalendarFieldValueNameGetter
        implements LocaleServiceProviderPool.LocalizedObjectGetter<CalendarNameProvider,
                                                                   String> {
        private static final CalendarFieldValueNameGetter INSTANCE =
            new CalendarFieldValueNameGetter();

        @Override
        public String getObject(CalendarNameProvider calendarNameProvider,
                                Locale locale,
                                String requestID, // calendarType
                                Object... params) {
            assert params.length == 4;
            int field = (int) params[0];
            int value = (int) params[1];
            int style = (int) params[2];
            boolean javatime = (boolean) params[3];

            // If javatime is true, resources from CLDR have precedence over JRE
            // native resources.
            if (javatime && calendarNameProvider instanceof CalendarNameProviderImpl) {
                String name;
                name = ((CalendarNameProviderImpl)calendarNameProvider)
                        .getJavaTimeDisplayName(requestID, field, value, style, locale);
                return name;
            }
            return calendarNameProvider.getDisplayName(requestID, field, value, style, locale);
        }
    }

    /**
     * Obtains a localized field-value pairs from a CalendarDataProvider
     * implementation.
     */
    private static class CalendarFieldValueNamesMapGetter
        implements LocaleServiceProviderPool.LocalizedObjectGetter<CalendarNameProvider,
                                                                   Map<String, Integer>> {
        private static final CalendarFieldValueNamesMapGetter INSTANCE =
            new CalendarFieldValueNamesMapGetter();

        @Override
        public Map<String, Integer> getObject(CalendarNameProvider calendarNameProvider,
                                              Locale locale,
                                              String requestID, // calendarType
                                              Object... params) {
            assert params.length == 3;
            int field = (int) params[0];
            int style = (int) params[1];
            boolean javatime = (boolean) params[2];

            // If javatime is true, resources from CLDR have precedence over JRE
            // native resources.
            if (javatime && calendarNameProvider instanceof CalendarNameProviderImpl) {
                Map<String, Integer> map;
                map = ((CalendarNameProviderImpl)calendarNameProvider)
                        .getJavaTimeDisplayNames(requestID, field, style, locale);
                return map;
            }
            return calendarNameProvider.getDisplayNames(requestID, field, style, locale);
        }
    }

     private static class CalendarWeekParameterGetter
        implements LocaleServiceProviderPool.LocalizedObjectGetter<CalendarDataProvider,
                                                                   Integer> {
        private static final CalendarWeekParameterGetter INSTANCE =
            new CalendarWeekParameterGetter();

        @Override
        public Integer getObject(CalendarDataProvider calendarDataProvider,
                                 Locale locale,
                                 String requestID,    // resource key
                                 Object... params) {
            assert params.length == 0;
            int value;
            switch (requestID) {
            case FIRST_DAY_OF_WEEK:
                value = calendarDataProvider.getFirstDayOfWeek(locale);
                break;
            case MINIMAL_DAYS_IN_FIRST_WEEK:
                value = calendarDataProvider.getMinimalDaysInFirstWeek(locale);
                break;
            default:
                throw new InternalError("invalid requestID: " + requestID);
            }
            return (value != 0) ? value : null;
        }
    }
}
