package application;

import static java.lang.System.*;

/** StartUp Procedure */
public class StartUp {

    //////////////////// Startup Message ////////////////////
    /**
     * 当ツールの起動時に開始メッセージを表示する
     *
     * <p>描画中に入力を行った場合、以下の事象発生有り（言語仕様の為、不可避）
     *
     * <ul>
     *   <li>開始メッセージの表示崩れ
     *   <li>画面表示される入力値が削除用スペースで隠れる（入力は正常に行われる）
     * </ul>
     */
    public void printStartUpMsg() {
        // 出力対象の文字列
        final String START_MSG = " Let's Recording!!";

        out.println();
        try {
            printOneEachMsg(START_MSG, 80);
            printBlinkingMsg(START_MSG, 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // プロンプトを表示する
        out.print(" INPUT> ");
    }

    /**
     * 文字列を１文字ずつ標準出力に表示する
     *
     * @param msg 出力対象の文字列
     * @param speed 点滅速度（ミリ秒）
     */
    private void printOneEachMsg(String msg, int speed) throws InterruptedException {
        // １文字ずつ標準出力に表示する
        for (int i = 1; i <= msg.length(); i++) {
            out.print(msg.substring(i - 1, i));
            Thread.sleep(speed);
        }
    }

    /**
     * 標準出力表示文字列を上書き削除する為のスペースを生成する
     *
     * @param msg 出力対象の文字列
     * @return スペース＋その先頭および末尾に復帰コードを結合した文字列
     */
    private StringBuilder createSpaces(String msg) {
        // 復帰コード
        String BGN_OF_LINE = "\r";
        StringBuilder sp = new StringBuilder(BGN_OF_LINE);

        for (int i = 0; i < msg.length(); i++) {
            // 半角スペースの追加
            sp.append(" ");
        }
        return sp.append(BGN_OF_LINE);
    }

    /**
     * 文字列を標準出力へ点滅表示後、文字列を非表示状態にする
     *
     * @param msg 出力対象の文字列
     * @param count 点滅回数
     */
    private void printBlinkingMsg(String msg, int count) throws InterruptedException {
        // 標準出力画面の上書き削除用スペース
        StringBuilder sp = createSpaces(msg);

        // 開始メッセージを点滅表示する
        for (int i = 0; i < count; i++) {
            Thread.sleep(200);
            // メッセージをスペースで上書きする
            out.print(sp);
            Thread.sleep(180);
            // メッセージを再表示する
            out.print(msg);
            if (i == count - 1) {
                Thread.sleep(600);
            }
        }
        out.print(sp);
    }
}
