package com.mahjongscoresheet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class RonActivity extends AppCompatActivity {
    private String[] names = new String[4];
    private int select_r, select_h, ban, fu;
    private RadioGroup rg_r, rg_h;
    private RadioButton[] rb_r = new RadioButton[4];
    private RadioButton[] rb_h = new RadioButton[4];
    private Spinner sp_ban, sp_fu;
    private AlertDialogSet ads = new AlertDialogSet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ron);

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
        rg_r = (RadioGroup)findViewById(R.id.rg_r);
        rg_h = (RadioGroup)findViewById(R.id.rg_h);
        sp_ban = (Spinner) findViewById(R.id.sp_ban);
        sp_fu = (Spinner) findViewById(R.id.sp_fu);
        rb_r[0] = (RadioButton) findViewById(R.id.rb_r1);
        rb_r[1] = (RadioButton) findViewById(R.id.rb_r2);
        rb_r[2] = (RadioButton) findViewById(R.id.rb_r3);
        rb_r[3] = (RadioButton) findViewById(R.id.rb_r4);
        rb_h[0] = (RadioButton) findViewById(R.id.rb_h1);
        rb_h[1] = (RadioButton) findViewById(R.id.rb_h2);
        rb_h[2] = (RadioButton) findViewById(R.id.rb_h3);
        rb_h[3] = (RadioButton) findViewById(R.id.rb_h4);
    }

    //取得Intent傳值
    private void getExtra() {
        names = getIntent().getStringArrayExtra("names");
    }

    //顯示玩家名稱到RadioButton
    private void setText_rb() {
        for (int i = 0; i < 4; i++) {
            rb_r[i].setText(names[i]);
            rb_h[i].setText(names[i]);
        }
    }

    //取得輸入
    private boolean getInput() {
        //榮和玩家
        switch(rg_r.getCheckedRadioButtonId()) {
            case R.id.rb_r1:
                select_r = 0;
                break;
            case R.id.rb_r2:
                select_r = 1;
                break;
            case R.id.rb_r3:
                select_r = 2;
                break;
            case R.id.rb_r4:
                select_r = 3;
                break;
        }
        //放銃玩家
        switch(rg_h.getCheckedRadioButtonId()) {
            case R.id.rb_h1:
                select_h = 0;
                break;
            case R.id.rb_h2:
                select_h = 1;
                break;
            case R.id.rb_h3:
                select_h = 2;
                break;
            case R.id.rb_h4:
                select_h = 3;
                break;
        }
        if (select_r == select_h) {
            ads.showSameRon();
            return false;
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
            intentMain.putExtra("select_r", select_r);
            intentMain.putExtra("select_h", select_h);
            intentMain.putExtra("ban", ban);
            intentMain.putExtra("fu", fu);
            setResult(RESULT_OK, intentMain);
            finish();
        }
    }
}
