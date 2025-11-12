package nechto.utils;

public final class NumFmt {
    private NumFmt() {}
    public static String two(double v) {
        return String.format("%.2f", v);
    }
    public static String three(double v) {
        return String.format("%.3f", v);
    }
    public static String withSign(double v, String s) {
        return v > 0 ? " " + s : s;
    }
}
