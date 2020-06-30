package utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Common Utility Procedure
 */
public class CmnUtils {

    /**
     * data.csv のパスを文字列で取得
     *
     * @return data.csv のパス（文字列）
     */
    public static String getCsvPath() {
        return System.getProperty("user.dir") + "/data.csv";
    }

    /**
     * 現在時刻（HH:mm:ss 形式）を返却する
     *
     * @return HH:mm:ss 形式の文字列
     */
    public static String now() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * 現在日付（yyyy-MM-dd 形式）を返却する
     *
     * @return yyyy-MM-dd 形式の文字列
     */
    public static String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 現在日付（yyyy-MM-dd 形式）を返却する
     *
     * @return yyyy-MM-dd 形式の文字列
     */
    public static String todayNoDelimiter() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * String型 の時刻データを Time型(java.sql) に変換する
     *
     * @return now 標準入力を受け付けた時刻
     */
    public static Time formatStrToTime(String now) {
        return (Time) Time.valueOf(now);
    }

    /**
     * String型 の日付データを Date型(java.util) に変換する
     *
     * @return now 標準入力を受け付けた時刻
     */
    public static Date formatStrToDate(String now) {
        Date formatStrToDate = null;
        try {
            formatStrToDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(CmnUtils.today());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStrToDate;
    }

    /**
     * 入力チェックを行う
     *
     * @param line 入力値の文字列
     * @return 入力値が空またはスペースのみの場合、trueを返却する
     */
    public static boolean isEmptyString(String line) {
        if (line == null) {
            return true;
        }
        // 空文字、タブ、改行、全角・半角スペースのみの場合
        if (line.matches("^$|[\\s|　]+")) {
            return true;
        }
        return false;
    }

    /**
     * 日付形式のチェックを行う
     *
     * @param date 入力値の文字列
     * @return yyyy-MM-dd 形式の場合、trueを返却する
     */
    public static boolean isDateFormat(String date) {
        if (date == null) {
            return false;
        }
        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return true;
        }
        return false;
    }

    /**
     * 終了メッセージを表示し、プログラムを終了する
     */
    public static void excuteTermination(String msg) {
        System.out.println(msg);
        System.exit(0);
    }
}
