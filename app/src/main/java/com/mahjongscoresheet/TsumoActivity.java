package com.mahjongscoresheet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class TsumoActivity extends AppCompatActivity {
    private String[] names = new String[4];
    private int select, ban, fu;
    private RadioGroup rg;
    private RadioButton[] rb = new RadioButton[4];
    private Spinner sp_ban, sp_fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsumo);

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得Layout物件
        findView();

        //取得Intent傳值
        getExtra();

        //顯示玩家名稱到RadioGroup
        setText_rb();
    }

    //取得Layout物件
    private void findView() {
        rg = (RadioGroup)findViewById(R.id.rg);
        sp_ban = (Spinner) findViewById(R.id.sp_ban);
        sp_fu = (Spinner) findViewById(R.id.sp_fu);
        rb[0] = (RadioButton) findViewById(R.id.rb1);
        rb[1] = (RadioButton) findViewById(R.id.rb2);
        rb[2] = (RadioButton) findViewById(R.id.rb3);
        rb[3] = (RadioButton) findViewById(R.id.rb4);
    }

    //取得Intent傳值
    private void getExtra() {
        names = getIntent().getStringArrayExtra("names");
    }

    //顯示玩家名稱到RadioButton
    private void setText_rb() {
        for (int i = 0; i < 4; i++) {
            rb[i].setText(names[i]);
        }
    }

    //取得輸入
    private boolean getInput() {
        //自摸玩家
        switch(rg.getCheckedRadioButtonId()) {
            case R.id.rb1:
                select = 0;
                break;
            case R.id.rb2:
                select = 1;
                break;
            case R.id.rb3:
                select = 2;
                break;
            case R.id.rb4:
                select = 3;
                break;
        }
        //飜
        String s = sp_ban.getSelectedItem().toString();
        ban = Integer.parseInt(s.substring(0, s.indexOf("飜")));
        //符
        s = sp_fu.getSelectedItem().toString();
        fu = Integer.parseInt(s.substring(0, s.indexOf("符")));
        return true;
    }

    //OK按鈕
    public void b_OK(View v) {
        if (getInput()) {
            Intent intentMain = new Intent();
            intentMain.putExtra("select", select);
            intentMain.putExtra("ban", ban);
            intentMain.putExtra("fu", fu);
            setResult(RESULT_OK, intentMain);
            finish();
        }
    }
}
