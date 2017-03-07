package com.mahjongscoresheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private AlertDialogSet ads = new AlertDialogSet(this);
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //進入NameActivity
        Intent intentName = new Intent(this, NameActivity.class);
        startActivityForResult(intentName, 1);
    }

    //取得Layout物件
    private void findView() {
        game.tv_names[0] = (TextView) findViewById(R.id.tv_name1);
        game.tv_names[1] = (TextView) findViewById(R.id.tv_name2);
        game.tv_names[2] = (TextView) findViewById(R.id.tv_name3);
        game.tv_names[3] = (TextView) findViewById(R.id.tv_name4);
        game.tv_points[0] = (TextView) findViewById(R.id.tv_point1);
        game.tv_points[1] = (TextView) findViewById(R.id.tv_point2);
        game.tv_points[2] = (TextView) findViewById(R.id.tv_point3);
        game.tv_points[3] = (TextView) findViewById(R.id.tv_point4);
        game.tv_richi_p[0] = (TextView) findViewById(R.id.tv_richi_p1);
        game.tv_richi_p[1] = (TextView) findViewById(R.id.tv_richi_p2);
        game.tv_richi_p[2] = (TextView) findViewById(R.id.tv_richi_p3);
        game.tv_richi_p[3] = (TextView) findViewById(R.id.tv_richi_p4);
        game.tv_kyoku = (TextView) findViewById(R.id.tv_kyoku);
        game.tv_honba = (TextView) findViewById(R.id.tv_honba);
        game.tv_richi = (TextView) findViewById(R.id.tv_richi);
    }

    //判斷對局結束條件
    private void endGame() {
        //有玩家被打飛
        for (int i = 0; i < 4; i++) {
            if (game.points[i] < 0) {
                ads.showFly(game.names, game.points, game.startPoint, game.endPoint);
                return;
            }
        }
        //到最後一局
        if (game.kyoku >= game.allRound * 4) {
            int[] ranking = game.getRanking();
            int ct = 0;
            for (int i = 0; i < 4; i++) if (ranking[i] == 0) ct += 1;
            //兩個第一名
            if (ct >= 2) ads.showExtraTime_twoTop();
            else {
                //點數超過返點
                for (int i = 0; i < 4; i++) {
                    if (game.points[i] >= 30000) {
                        if (game.allRound == 1) ads.showLast("東風戰", game.names, game.points, game.startPoint, game.endPoint);
                        else if (game.allRound == 2) ads.showLast("半莊戰", game.names, game.points, game.startPoint, game.endPoint);
                        else if (game.allRound == 4) ads.showLast("全莊戰", game.names, game.points, game.startPoint, game.endPoint);
                        return;
                    }
                }
                //點數小於返點
                ads.showExtraTime_endPoint();
            }
        }
        //All Last 末莊拿第一
        else if (game.kyoku >= game.allRound * 4 - 1) {
            if (!game.allLast) game.allLast = true;   //剛進入時不觸發
            else {
                int[] ranking = game.getRanking();
                int ct = 0;
                for (int i = 0; i < 4; i++) if (ranking[i] == 0) ct += 1;
                if (ranking[3] == 0 && game.points[3] >= 30000 && ct == 1) {
                    ads.showAllLastTop(game.names, game.points, game.startPoint, game.endPoint);
                }
            }
        }
    }

    //三響和局
    private void threeRon(boolean[] select_r) {
        game.richi_p = new int[4];
        game.honba += 1;
        if (!select_r[game.oya]) game.kyoku += 1;   //莊家沒和牌
        game.setText_all();
        endGame();
        ads.showPoint("三響和局，點數不變。");
    }

    //處理來自NameActivity的傳回值: 1
    private void processNameActivity(Intent data) {
        game = new Game();
        record = new Record();
        findView(); //取得Layout物件

        game.allRound = data.getIntExtra("allRound", 0);
        game.startPoint = data.getIntExtra("startPoint", 0);
        game.endPoint = data.getIntExtra("endPoint", 0);
        for (int i = 0; i < 4; i++) {
            if (data.getStringArrayExtra("names")[i].isEmpty()) //沒有輸入名稱
                game.names[i] = "Player" + (i + 1);
            else
                game.names[i] = data.getStringArrayExtra("names")[i];
            game.points[i] = game.startPoint;
            //game.tv_names[i].setText(game.names[i]);
            //game.tv_points[i].setText(Integer.toString(game.startPoint));
        }
        game.oya = 0;
        game.setText_all();
        record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 1, game.allLast);    //紀錄
    }

    //處理來自RonActivity的傳回值: 2
    private void processRonActivity(Intent data) {
        int select_r = data.getIntExtra("select_r", 0);
        int select_h = data.getIntExtra("select_h", 0);
        int ban = data.getIntExtra("ban", 0);
        int fu = data.getIntExtra("fu", 0);

        //計算點數
        int[] ps = game.getPoint(ban, fu, game.oya == select_r, false);
        ps[0] += game.honba * 300;
        game.points[select_r] += ps[0] + game.richi * 1000;
        game.points[select_h] -= ps[0];

        //顯示警示窗
        String s;
        if (game.richi == 0)
            s = game.names[select_r] + "：+" + ps[0] + "\n" +
                    game.names[select_h] + "：-" + ps[0];
        else
            s = game.names[select_r] + "：+" + ps[0] + " (1000x" + game.richi + ")\n" +
                    game.names[select_h] + "：-" + ps[0];

        //更改Game資訊
        game.richi = 0;
        game.richi_p = new int[4];
        if (game.oya == select_r) {
            game.honba += 1;
        }
        else {
            game.kyoku += 1;
            game.honba = 0;
            game.oya = game.kyoku % 4;
        }
        game.setText_all();
        endGame();
        ads.showPoint(s);
        record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 2, game.allLast);    //紀錄
    }

    //處理來自TsumoActivity的傳回值: 3
    private void processTsumoActivity(Intent data) {
        int select = data.getIntExtra("select", 0);
        int ban = data.getIntExtra("ban", 0);
        int fu = data.getIntExtra("fu", 0);

        //計算點數
        int[] ps = game.getPoint(ban, fu, game.oya == select, true);
        ps[0] += game.honba * 100;
        ps[1] += game.honba * 100;
        if (game.oya == select) {   //莊家自摸
            game.points[select] += ps[0] * 3 + game.richi * 1000;
            for (int i = 0; i < 4; i++) if (i != select) game.points[i] -= ps[0];
        }
        else {  //閒家自摸
            game.points[select] += ps[0] * 2 + ps[1] + game.richi * 1000;
            for (int i = 0; i < 4; i++) {
                if (i != select && i != game.oya) game.points[i] -= ps[0];
                if (i != select && i == game.oya) game.points[i] -= ps[1];
            }
        }

        //顯示警示窗
        String s;
        if (game.oya == select) {   //莊家自摸
            if (game.richi == 0)
                s = game.names[select] + "：+" + ps[0] * 3 + "\n";
            else
                s = game.names[select] + "：+" + ps[0] * 3 + " (1000x" + game.richi + ")\n";
            for (int i = 0; i < 4; i++) if (i != select) s += game.names[i] + "：-" + ps[0] + "\n";
        }
        else {  //閒家自摸
            if (game.richi == 0)
                s = game.names[select] + "：+" + (ps[0] * 2 + ps[1]) + "\n";
            else
                s = game.names[select] + "：+" + (ps[0] * 2 + ps[1]) + " (1000x" + game.richi + ")\n";
            for (int i = 0; i < 4; i++) {
                if (i != select && i != game.oya) s += game.names[i] + "：-" + ps[0] + "\n";
                if (i != select && i == game.oya) s += game.names[i] + "：-" + ps[1] + "\n";
            }
        }

        //更改Game資訊
        game.richi = 0;
        game.richi_p = new int[4];
        if (game.oya == select) {
            game.honba += 1;
        }
        else {
            game.kyoku += 1;
            game.honba = 0;
            game.oya = game.kyoku % 4;
        }
        game.setText_all();
        endGame();
        ads.showPoint(s.substring(0, s.length() - 1));
        record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 3, game.allLast);    //紀錄
    }

    //處理來自Ron2Activity的傳回值: 4
    private void processRon2Activity(Intent data) {
        Intent intentRon2B = new Intent(this, Ron2BActivity.class);
        intentRon2B.putExtra("names", game.names);
        intentRon2B.putExtra("select_r", data.getBooleanArrayExtra("select_r"));
        intentRon2B.putExtra("select_h", data.getIntExtra("select_h", 0));
        int ct = 0;
        for (boolean b: data.getBooleanArrayExtra("select_r")) if (b) ct += 1;
        if (ct != 3) startActivityForResult(intentRon2B, 42);   //雙響
        else {
            threeRon(data.getBooleanArrayExtra("select_r"));    //三響
            record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 43, game.allLast);   //紀錄
        }
    }

    //處理來自Ron2BActivity的傳回值: 42
    private void processRon2BActivity(Intent data) {
        boolean[] select_r = data.getBooleanArrayExtra("select_r");
        int select_r1 = 0, select_r2 = 0;
        int select_h = data.getIntExtra("select_h", 0);
        int ban1 = data.getIntExtra("ban1", 0);
        int ban2 = data.getIntExtra("ban2", 0);
        int fu1 = data.getIntExtra("fu1", 0);
        int fu2 = data.getIntExtra("fu2", 0);

        //計算點數
        int[] ps1 = new int[2];
        int[] ps2 = new int[2];
        for (int i = 0; i < 4; i++) {
            if (select_r[i]) {
                select_r1 = i;
                ps1 = game.getPoint(ban1, fu1, game.oya == i, false);
                ps1[0] += game.honba * 300;
                game.points[i] += ps1[0];
                game.points[select_h] -= ps1[0];
                break;
            }
        }
        for (int i = 3; i >= 0; i--) {
            if (select_r[i]) {
                select_r2 = i;
                ps2 = game.getPoint(ban2, fu2, game.oya == i, false);
                ps2[0] += game.honba * 300;
                game.points[i] += ps2[0];
                game.points[select_h] -= ps2[0];
                break;
            }
        }
        //決定立直棒給哪個玩家
        if (game.richi != 0) {
            if (select_r1 < select_h && select_h < select_r2)   //給r2
                game.points[select_r2] += game.richi * 1000;
            else    //給r1
                game.points[select_r1] += game.richi * 1000;
        }

        //顯示警示窗
        String s;
        if (game.richi == 0)
            s = game.names[select_r1] + "：+" + ps1[0] + "\n" +
                    game.names[select_r2] + "：+" + ps2[0] + "\n" +
                    game.names[select_h] + "：-" + (ps1[0] + ps2[0]);
        else if (select_r1 < select_h && select_h < select_r2)
            s = game.names[select_r1] + "：+" + ps1[0] + "\n" +
                    game.names[select_r2] + "：+" + ps2[0] + " (1000x" + game.richi + ")\n" +
                    game.names[select_h] + "：-" + (ps1[0] + ps2[0]);
        else
            s = game.names[select_r1] + "：+" + ps1[0] + " (1000x" + game.richi + ")\n" +
                    game.names[select_r2] + "：+" + ps2[0] + "\n" +
                    game.names[select_h] + "：-" + (ps1[0] + ps2[0]);

        //更改Game資訊
        game.richi = 0;
        game.richi_p = new int[4];
        if (game.oya == select_r1 || game.oya == select_r2) {
            game.honba += 1;
        }
        else {
            game.kyoku += 1;
            game.honba = 0;
            game.oya = game.kyoku % 4;
        }
        game.setText_all();
        endGame();
        ads.showPoint(s);
        record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 42, game.allLast);   //紀錄
    }

    //處理來自RyuukyokuActivity的傳回值: 5
    private void processRyuukyokuActivity(Intent data) {
        boolean[] select = data.getBooleanArrayExtra("select");
        int num = 0;
        for (int i = 0; i < 4; i++) if (select[i]) num += 1;

        //計算點數
        if (num == 1) { //一家聽牌
            for (int i = 0; i < 4; i++) {
                if (select[i]) game.points[i] += 3000;
                else game.points[i] -= 1000;
            }
        }
        else if (num == 2) {    //兩家聽牌
            for (int i = 0; i < 4; i++) {
                if (select[i]) game.points[i] += 1500;
                else game.points[i] -= 1500;
            }
        }
        else if (num == 3) {    //三家聽牌
            for (int i = 0; i < 4; i++) {
                if (select[i]) game.points[i] += 1000;
                else game.points[i] -= 3000;
            }
        }

        //顯示警示窗
        String s = "";
        if (num == 1) { //一家聽牌
            for (int i = 0; i < 4; i++) if (select[i]) s += game.names[i] + "：+3000\n";
            for (int i = 0; i < 4; i++) if (!select[i]) s += game.names[i] + "：-1000\n";
        }
        else if (num == 2) {    //兩家聽牌
            for (int i = 0; i < 4; i++) if (select[i]) s += game.names[i] + "：+1500\n";
            for (int i = 0; i < 4; i++) if (!select[i]) s += game.names[i] + "：-1500\n";
        }
        else if (num == 3) {    //三家聽牌
            for (int i = 0; i < 4; i++) if (select[i]) s += game.names[i] + "：+1000\n";
            for (int i = 0; i < 4; i++) if (!select[i]) s += game.names[i] + "：-3000\n";
        }

        //更改Game資訊
        game.richi_p = new int[4];
        game.honba += 1;
        if (!select[game.oya]) {    //莊家沒聽
            game.kyoku += 1;
            game.oya = game.kyoku % 4;
        }
        game.setText_all();
        endGame();
        if (!s.isEmpty()) ads.showPoint(s.substring(0, s.length() - 1));
        else if (num == 0) ads.showPoint("沒有玩家聽牌，點數不變。");
        else if (num == 4) ads.showPoint("全部玩家聽牌，點數不變。");
        record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 5, game.allLast);    //紀錄
    }

    //處理來自RichiActivity的傳回值: 6
    private void processRichiActivity(Intent data) {
        int select = data.getIntExtra("select", 0);
        game.points[select] -= 1000;
        game.richi += 1;
        game.richi_p[select] += 1;
        game.setText_tv_points();
        game.setText_richi();
        game.setText_tv_richi_p();
        String s = game.names[select] + "：-1000";
        ads.showPoint(s);
        record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 6, game.allLast);    //紀錄
    }

    //接收傳回值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {   //收到OK
            switch(requestCode){
                case 1:
                    //處理來自NameActivity的傳回值
                    processNameActivity(data);
                    break;
                case 2:
                    //處理來自RonActivity的傳回值
                    processRonActivity(data);
                    break;
                case 3:
                    //處理來自TsumoActivity的傳回值
                    processTsumoActivity(data);
                    break;
                case 4:
                    //處理來自Ron2Activity的傳回值
                    processRon2Activity(data);
                    break;
                case 42:
                    //處理來自Ron2BActivity的傳回值
                    processRon2BActivity(data);
                    break;
                case 5:
                    //處理來自RyuukyokuActivity的傳回值
                    processRyuukyokuActivity(data);
                    break;
                case 6:
                    //處理來自RichiActivity的傳回值
                    processRichiActivity(data);
                    break;
            }
        }
        else if(resultCode == RESULT_CANCELED) {    //收到CANCELED
            switch(requestCode){
                case 1:
                    finish();   //關閉activity
                    break;
            }
        }
    }

    //榮和按鈕: 2
    public void clickRon(View v) {
        Intent intentRon = new Intent(this, RonActivity.class);
        intentRon.putExtra("names", game.names);
        startActivityForResult(intentRon, 2);
    }

    //自摸按鈕: 3
    public void clickTsumo(View v) {
        Intent intentTsumo = new Intent(this, TsumoActivity.class);
        intentTsumo.putExtra("names", game.names);
        startActivityForResult(intentTsumo, 3);
    }

    //一砲多響按鈕: 4
    public void clickRon2(View v) {
        Intent intentRon2 = new Intent(this, Ron2Activity.class);
        intentRon2.putExtra("names", game.names);
        startActivityForResult(intentRon2, 4);
    }

    //流局按鈕: 5
    public void clickRyuukyoku(View v) {
        Intent intentRyuukyoku = new Intent(this, RyuukyokuActivity.class);
        intentRyuukyoku.putExtra("names", game.names);
        startActivityForResult(intentRyuukyoku, 5);
    }

    //立直按鈕: 6
    public void clickRichi(View v) {
        Intent intentRichi = new Intent(this, RichiActivity.class);
        intentRichi.putExtra("names", game.names);
        intentRichi.putExtra("points", game.points);
        startActivityForResult(intentRichi, 6);
    }

    //點差查詢按鈕
    public void clickDiffer(View v) {
        String s = "";
        int[] ranking = game.getRanking();
        int topPoint = 0;
        for (int i = 0; i < 4; i++) if (ranking[i] == 0) topPoint = game.points[i];
        for (int i = 0; i < 4; i++)
            s += game.names[i] + "：" + Integer.toString(game.points[i] - topPoint) + "\n";
        ads.showDiffer(s.substring(0, s.length() - 1));
    }

    //還原按鈕
    public void clickBack(View v) {
        if (record.count > 0) ads.showBack(game, record);
    }

    //重開按鈕
    public void clickReset(View v) {
        ads.showReset();
    }

    //點表查詢按鈕
    public void clickTable(View v) {
        //進入PointTableActivity
        Intent intentPointTable = new Intent(this, PointTableActivity.class);
        startActivity(intentPointTable);
    }

    //手動更改玩家名稱
    public void alterName(View v) {
        ads.showAlter("玩家名稱", (TextView) v, game, record);
    }

    //手動更改玩家點數
    public void alterPoint(View v) {
        ads.showAlter("玩家點數", (TextView) v, game, record);
    }

    //手動更改局數
    public void alterKyoku(View v) {
        ads.showAlter("局數", (TextView) v, game, record);
    }

    //手動更改本場數
    public void alterHonba(View v) {
        ads.showAlter("本場數", (TextView) v, game, record);
    }

    //手動更改立直數
    public void alterRichi(View v) {
        ads.showAlter("立直棒", (TextView) v, game, record);
    }

    //返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ads.showQuit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
