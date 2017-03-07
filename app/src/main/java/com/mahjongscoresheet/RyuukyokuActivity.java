package com.mahjongscoresheet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class RyuukyokuActivity extends AppCompatActivity {
    private String[] names = new String[4];
    private boolean[] select = new boolean[4];
    private CheckBox[] cb = new CheckBox[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ryuukyoku);

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得Layout物件
        findView();

        //取得Intent傳值
        getExtra();

        //顯示玩家名稱到CheckBox
        setText_cb();
    }

    //取得Layout物件
    private void findView() {
        cb[0] = (CheckBox)findViewById(R.id.cb1);
        cb[1] = (CheckBox)findViewById(R.id.cb2);
        cb[2] = (CheckBox)findViewById(R.id.cb3);
        cb[3] = (CheckBox)findViewById(R.id.cb4);
    }

    //取得Intent傳值
    private void getExtra() {
        names = getIntent().getStringArrayExtra("names");
    }

    //顯示玩家名稱到CheckBox
    private void setText_cb() {
        for (int i = 0; i < 4; i++) {
            cb[i].setText(names[i]);
        }
    }

    //取得輸入
    private boolean getInput() {
        //聽牌玩家
        for (int i = 0; i < 4; i++) {
            if (cb[i].isChecked()) select[i] = true;
            else select[i] = false;
        }
        return true;
    }

    //OK按鈕
    public void b_OK(View v) {
        if (getInput()) {
            Intent intentMain = new Intent();
            intentMain.putExtra("select", select);
            setResult(RESULT_OK, intentMain);
            finish();
        }
    }
}
