package web_diary.api;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * データベースに登録する日付を作成するクラスです
 */
public class DateTimeLite {
  private LocalDateTime time = LocalDateTime.now();
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private DateTimeLite() {}
  private DateTimeLite(LocalDateTime time) {
    this.time = time;
  }

  /**
   * このクラスのインスタンスを返します
   * @return このクラスのインスタンス
   */
  public static DateTimeLite getInstance() {
    return new DateTimeLite();
  }

  /**
   * yyyy-MM-dd HH:mm:ss.SSS形式の文字列からこのクラスのインスタンスを作成します
   * @param timeString  yyyy-MM-dd HH:mm:ss 形式の文字列
   * @return 指定された文字列のさす時間が設定されたこのインスタンスを返す
   */
  public DateTimeLite parse(String timeString) {
    return new DateTimeLite(LocalDateTime.parse(timeString, this.formatter));
  }

  /**
   * インスタンスに設定された日付の一日後が設定されたインスタンスを返します。
   * @return 一日後が設定されたインスタンス
   */
  public DateTimeLite setNextDateTime() {
    return new DateTimeLite(this.time.plusDays(1l));
  }

  /**
   * yyyy-MM-dd 00:00:00 が設定されたインスタンスを返します
   * @return 時間以下が0に設定されたインスタンス
   */
  public DateTimeLite setZeroHourInstance() {
    return new DateTimeLite(this.time.truncatedTo(ChronoUnit.DAYS));
  }

  public DateTimeLite setTimestamp(Timestamp timestamp) {
    return DateTimeLite.getInstance()
    .parse(
      timestamp.toString().substring(0, timestamp.toString().indexOf("."))
    );
  }

  /**
   * このインスタンスを文字列化すると 
   * yyyy-MM-dd HH:mm:ss 形式の時間文字列が返されます
   */
  @Override
  public String toString() {
    return this.time.format(this.formatter);
  }
}