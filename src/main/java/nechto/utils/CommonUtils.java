package nechto.utils;

import static java.lang.String.format;

public class CommonUtils {

    public static String convertFloatToStringWithTwoDotsPrecision(float num) {
        return format("%.2f", num);
    }
}
