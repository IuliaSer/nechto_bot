package nechto.service.results;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.IsoFields;

public record TimeRange(LocalDateTime start, LocalDateTime end) {
    public static TimeRange forDay(LocalDate date) {
        // ваш «день» 27 часов можно оставить как фичу: [00:00, +27h)
        return new TimeRange(date.atStartOfDay(), date.atStartOfDay().plusHours(27));
    }
    public static TimeRange forMonth(YearMonth ym) {
        var start = ym.atDay(1).atStartOfDay();
        return new TimeRange(start, start.plusMonths(1));
    }
    public static TimeRange forQuarter(LocalDateTime anchor) {
        var firstDay = anchor.with(IsoFields.DAY_OF_QUARTER, 1)
                             .withHour(0).withMinute(0).withSecond(0).withNano(0);
        return new TimeRange(firstDay, anchor);
    }
}
