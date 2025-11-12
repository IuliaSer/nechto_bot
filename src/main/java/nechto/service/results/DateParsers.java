package nechto.service.results;

import java.time.LocalDate;
import java.time.YearMonth;

public final class DateParsers {
    private DateParsers() {}
    public static LocalDate parseYmd(String s) {
        return LocalDate.parse(s); // ISO: 2025-11-10
    }
    public static YearMonth parseYm(String s) {
        return YearMonth.parse(s); // 2025-11
    }
}
