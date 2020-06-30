package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.CmnUtils;
import utils.Const;

public class CsvIO {
    /** 現在レコードの id の数値 */
    private int idCounter = 0;
    /** csvファイルの存在チェック済フラグ */
    private boolean isCheckedCsv = false;
    /** csvファイルパス */
    private String csvPath;
    /** csvファイルオブジェクト */
    private File csvFile;

    public CsvIO() {
        csvPath = System.getProperty("user.dir") + "/data.csv";
        csvFile = new File(csvPath);
    }

    /**
     * csv file にレコードを挿入する
     *
     * <p>レコードの形式：
     *
     * <p>id(0000), date(yyyy-MM-dd), timestamp(HH:mm:ss), content
     *
     * <p>
     *
     * @param content １行の入力内容
     * @param now 標準入力を受け付けた時刻
     */
    public void insertContentToCsv(String content, String now) {

        // 先頭ゼロ埋め（４桁）
        String id = String.format("%04d", ++idCounter);
        // １レコードを作成する
        StringBuilder sb = new StringBuilder();
        sb.append(id)
                .append(",")
                .append(CmnUtils.today())
                .append(",")
                .append(now)
                .append(",")
                .append(content)
                .append(Const.BR);

        // ファイル末尾に追加する ※書き込み先ファイルの末尾に１行の空行の存在が絶対条件
        try (FileWriter filewriter = new FileWriter(csvFile, true); ) {
            filewriter.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        // TODO test
        //        System.out.print(" sb: " + sb);
        //        System.out.println(" csvPath: " + csvFile.getPath());
    }

    //////////////////// csv Controller ////////////////////
    /**
     * 既存csvファイルが未チェック（初回実行時）の場合、以下の処理を行う
     *
     * <p>・csvファイルが存在する場合、idの最大値を取得し、フィールドに設定する
     *
     * <p>・csvファイルが存在しない場合、新規作成する
     *
     * <p>
     */
    public void observeCsv() {
        // csvファイルが未チェックの場合
        if (!isCheckedCsv) {
            // csvファイルが存在する場合
            if (isExistCsv()) {
                adjustIdCounter(CmnUtils.today());
            }
        }
    }

    /**
     * csvファイルの存在を確認し、実行時に確認済フラグ（isChecked）を true に変更する
     *
     * <p>
     *
     * @return ファイルが存在する場合、true を返却する
     */
    private boolean isExistCsv() {
        // 当メソッドを実行したことにより、確認済フラグを true に設定する
        isCheckedCsv = true;
        if (new File(csvPath).exists()) {
            return true;
        }
        return false;
    }

    /**
     * ※csvファイルが既に存在する場合に呼び出される
     *
     * <p>csvファイルからidの最大値を取得し、フィールド（idCounter）に設定する
     *
     * <p>
     *
     * @param strDate 指定の日付
     */
    private void adjustIdCounter(String strDate) {

        List<String> list = new ArrayList<>();

        try (Scanner scan = new Scanner(csvFile); ) {
            // 次の行が存在する場合
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (CmnUtils.isEmptyString(line)) {
                    break;
                }
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // リストが null ではない、かつサイズ 1 以上の場合
        if (list != null && list.size() > 0) {
            String row = list.get(list.size() - 1);
            if (row != null && row.length() >= 4) {
                idCounter = Integer.parseInt(row.substring(0, 4));
            } else {
                System.out.println(" csv file read error.");
            }
        }
    }

    /**
     * csvファイルから指定の日付に該当するレコードにアクセスし、コンテンツを取得する
     *
     * <p>レコードの形式：
     *
     * <p>id(0000), date(yyyy-MM-dd), timestamp(HH:mm:ss), content
     *
     * <p>
     *
     * TODO 未完成
     *
     * @param strDate 指定の日付
     * @return
     */
    public static String readContentFromCsv(String strDate) {

        // ファイルから読み込んだ投稿履歴を一時保存するオブジェクトの生成
        StringBuilder articles = new StringBuilder();
        CsvIO csvIo = new CsvIO();

        // ファイル内容を読み込む為のオブジェクトを生成する
        try (Scanner scan = new Scanner(csvIo.csvFile); ) {
            // 次の行が存在する場合
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                // timestamp の部分のみを取得する
                String timestamp = line.substring(16, 24);
                // content の部分のみを取得する
                String content = line.substring(25, line.length());
                // ファイルから読み込んだ内容 + 空行を StringBuilder に追加する
                articles.append(timestamp).append("\t").append(content).append(Const.BR);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return articles.toString();
    }
}
