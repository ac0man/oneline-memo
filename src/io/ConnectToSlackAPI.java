package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectToSlackAPI {
    private static final String WEBHOOK_URL = "https://hooks.slack.com/something-to-become-a-uri";

    /**
     * jsonデータを Slack API にPOSTする
     *
     * @param json POSTする内容
     * @return errorMessage OK:空文字, NG:エラーメッセージ
     */
    public static String postJson(String json) {
        HttpURLConnection urlCon = null;

        try {
            // incoming-webhooks の URL を指定
            URL url = new URL(WEBHOOK_URL);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setUseCaches(false);
            urlCon.setDoOutput(true);
            urlCon.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // OutputStream を生成し、ストリームにjsonデータを書き込み後、ストリームを閉じる
            OutputStreamWriter out = new OutputStreamWriter(urlCon.getOutputStream());
            out.write(json);
            out.close();

            // データ送信後、getInputStream()メソッド実行まで完了することでhttp通信が確立される
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String line = in.readLine();

            // レスポンスが "ok" の場合、true を返却する
            if (line != null && "ok".equals(line)) {
                return "";
            }
            return "httpリクエストエラー";

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "webhooksのURLが無効の為、再生成が必要 : " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException : " + e.getMessage();
        } finally {
            urlCon.disconnect();
        }
    }

    /**
     * Slack API にPOSTする為のjsonデータ('text'のみ)を作成し、呼び出し元に返却する
     *
     * @param content Slackに投稿する内容
     * @return 作成したjsonデータ
     */
    public static String createJson(String content) {
        return "{'text':'" + content + "'}";
    }
}
