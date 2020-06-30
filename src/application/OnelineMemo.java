package application;

import java.util.Scanner;

import io.CsvIO;
import utils.CmnUtils;
import utils.Const;

/** OnelineMemo Main Procedure */
public final class OnelineMemo {
    /** controlByInput 内部のステータスコード: チェックOK */
    private static final String OK = "0";
    /** controlByInput 内部のステータスコード: 引数の指定方法誤り */
    private static final String WRONG_ARGS = "1";

    /** main */
    public static void main(String[] args) {
        // 起動引数に基づいた制御
        controlByInput(args);

        // 開始メッセージの表示
        new StartUp().printStartUpMsg();
        // メイン処理
        startRecording();
    }

    /**
     * 起動引数に基づいた制御を行う
     *
     * @param args コマンドライン引数
     */
    private static void controlByInput(String[] args) {
        if (args.length > 0) {
            String checkCode = WRONG_ARGS;
            String date = null;

            // 入力チェック
            if ("-e".equals(args[0])) {
                checkCode = OK;

                if (args.length == 2) {
                    // 日付指定の形式が正しい場合
                    if (CmnUtils.isDateFormat(args[1])) {
                        date = args[1];
                    } else {
                        checkCode = WRONG_ARGS;
                    }
                }
            }

            String msg = "";
            // 処理実行
            if (OK.equals(checkCode)) {
                // １日のデータを標準出力へ出力する
                exportToCsv(date);
                msg = "--end";
            } else {
                System.out.println(Const.BR + " 起動引数の指定誤り: 起動引数は以下のように指定して下さい。" + Const.BR);
                System.out.println(" memo -e 2019-04-22  : 指定された yyyy-mm-dd 形式の日付でデータを検索し出力する");
                System.out.println("                                (1桁台の数値は 0 で補完すること)");
                System.out.println(" memo -e             : 日付指定が無い場合、本日の日付でデータを検索し出力する");
                System.out.println(" memo                : 引数が未指定の場合、入力モードで起動する");
            }
            CmnUtils.excuteTermination(msg);
        }
    }

    /**
     * {@link OnelineMemo#recording(CsvIO, Scanner, StringBuilder)} を呼び出す
     *
     * <p>
     */
    private static void startRecording() {

        try (Scanner scan = new Scanner(System.in)) {
            recordingToCsv(scan);

            // Scannerによる何らかの例外が発生した場合、処理を終了する
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Const.BR + " Maybe, the exception data came here.");
            CmnUtils.excuteTermination(" Must fix the problem. Bye!!");
        }
    }

    /**
     * 入力内容の記録(csv)を繰り返し実行する
     *
     * <p>処理内容
     *
     * <p>
     *
     * <ul>
     *   <li>入力チェック
     *   <li>タイムスタンプの取得
     *   <li>入力内容の永続化
     * </ul>
     *
     * @param line 入力内容
     */
    private static void recordingToCsv(Scanner scan) throws Exception {

        CsvIO csv = new CsvIO();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            // 標準入力から "exit" が入力された場合、当プログラムを終了する
            if (line != null && "exit".equals(line.trim().toLowerCase())) {
                CmnUtils.excuteTermination(" Bye!!");
            }

            // 現在時刻を取得する
            String created_at = CmnUtils.now();

            // 入力値が空ではない場合、データを永続化する
            if (!CmnUtils.isEmptyString(line)) {
                // 永続先のcsvファイルをチェックする
                csv.observeCsv();
                csv.insertContentToCsv(line, created_at);
            }
            // 標準出力に入力受付時刻と次のプロンプトを表示する
            printContent(line, created_at, false);
        }
    }

    /**
     * 標準出力に入力内容を処理した文字列と処理時刻を表示する
     *
     * <p>出力サンプル：
     *
     * <p>INPUT>
     *
     * <p>Text data is empty!
     *
     * <p>
     *
     * <p>INPUT> 下にタイムスタンプが表示されます
     *
     * <p>TIME : 16:54:42
     *
     * <p>
     *
     * <p>INPUT>
     *
     * <p>（以下、続く．．．）
     *
     * <p>
     *
     * @param csv
     * @param line １行の入力内容
     * @param now 標準入力を受け付けた時刻
     */
    private static void printContent(String line, String created_at, boolean isException) {
        // 入力値が空の場合、メッセージを表示する
        if (CmnUtils.isEmptyString(line)) {
            System.out.println(" Text data is empty!");
        } else if (isException) {
            System.out.println(Const.BR + " It has error when the DB connected!");
        } else {
            System.out.println(" TIME : " + created_at);
        }
        System.out.print(Const.BR + " INPUT> ");
    }

    /** 出力モードで起動した際に、指定日付で標準出力画面に出力する */
    private static void exportToCsv(String date) {
        if (date == null) {
            date = "today";
        }
        System.out.println(Const.BR + "--" + date);
        System.out.println();
        System.out.println(CsvIO.readContentFromCsv(date));
    }
}
