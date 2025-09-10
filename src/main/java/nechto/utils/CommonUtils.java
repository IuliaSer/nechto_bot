package nechto.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CommonUtils {

    public static String convertFloatToStringWithTwoDotsPrecision(float num) {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat df = new DecimalFormat("0.##", sym);
        return df.format(num);
    }
}
