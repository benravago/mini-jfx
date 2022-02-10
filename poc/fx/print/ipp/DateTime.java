package fx.print.ipp;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import java.time.temporal.Temporal;
import static java.time.temporal.ChronoField.*;

/**
 * RFC2579 DateAndTime octet-string to/from java.time.temporal.Temporal object.
 */
class DateTime {

  static byte[] format(Temporal t) {

    var year = t.get(YEAR);
    var month = t.get(MONTH_OF_YEAR);
    var day = t.get(DAY_OF_MONTH);
    var hour = t.get(HOUR_OF_DAY);
    var minutes = t.get(MINUTE_OF_HOUR);
    var seconds = t.get(SECOND_OF_MINUTE);
    var deciseconds = t.get(NANO_OF_SECOND) / 100_000_000;
    var offset = t.get(OFFSET_SECONDS);

    var utc_direction = '+';
    if (offset < 0) {
      utc_direction = '-';
      offset = -offset;
    }
    offset = offset / 60;
    var utc_hour = offset / 60;
    var utc_minutes = offset % 60;

    return new byte[] {
      (byte) (year >> 8), (byte) year, // 1
      (byte) month, // 2
      (byte) day, // 3
      (byte) hour, // 4
      (byte) minutes, // 5
      (byte) seconds, // 6
      (byte) deciseconds, // 7
      (byte) utc_direction, // 8
      (byte) utc_hour, // 9
      (byte) utc_minutes // 10
    };
  }

  static Temporal parse(byte[] dt) {
    assert dt.length == 11;

    int year = (dt[0] << 8) | (dt[1] & 0x0ff);
    int month = dt[2];
    int day = dt[3];
    int hour = dt[4];
    int minutes = dt[5];
    int seconds = dt[6];
    int deciseconds = dt[7];
    int utc_direction = dt[8];
    int utc_hour = dt[9];
    int utc_minutes = dt[10];

    if (utc_direction == '-') {
      utc_hour = -utc_hour;
      utc_minutes = -utc_minutes;
    }

    return OffsetDateTime.of(
      year, month, day, hour, minutes, seconds, deciseconds * 100_000_000,
      ZoneOffset.ofHoursMinutes( utc_hour, utc_minutes )
    );
  }

/**
 *  RFC 2579   Textual Conventions for SMIv2   April 1999
 *
 *  DateAndTime ::= TEXTUAL-CONVENTION
 *
 *    DISPLAY-HINT  "2d-1d-1d,1d:1d:1d.1d,1a1d:1d"
 *    STATUS        current
 *    SYNTAX        OCTET STRING (SIZE (8 | 11))
 *
 *    DESCRIPTION   "A date-time specification.
 *
 *      field  octets  contents            range
 *      -----  ------  --------            -----
 *        1      1-2   year*               0..65536
 *        2       3    month               1..12
 *        3       4    day                 1..31
 *        4       5    hour                0..23
 *        5       6    minutes             0..59
 *        6       7    seconds             0..60    (use 60 for leap-second)
 *        7       8    deci-seconds        0..9
 *        8       9    direction from UTC  '+' / '-'
 *        9      10    hours from UTC*     0..13
 *       10      11    minutes from UTC    0..59
 *
 *      * Notes:
 *      - the value of year is in network-byte order
 *      - daylight saving time in New Zealand is +13
 *
 *      For example,  Tuesday May 26, 1992 at 1:30:15 PM EDT
 *      would be displayed as:  1992-5-26,13:30:15.0,-4:0
 *
 *      Note that if only local time is known, then timezone
 *      information (fields 8-10) is not present."
 */
}
