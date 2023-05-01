package de.mathema.springboot.infrastructure.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateTimeUtils {

  private DateTimeUtils() {
  }

  public static LocalDate toLocalDate(Date date) {
    return new java.sql.Date(date.getTime()).toLocalDate();
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return new Timestamp(date.getTime()).toLocalDateTime().withNano(0);
  }

  public static Date toDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date toDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}
