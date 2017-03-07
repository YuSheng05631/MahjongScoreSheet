package com.mahjongscoresheet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Ron2Activity extends AppCompatActivity {
    private String[] names = new String[4];
    private boolean[] select_r = new boolean[4];
    private int select_h;
    private CheckBox[] cb = new CheckBox[4];
    private RadioGroup rg;
    private RadioButton[] rb = new RadioButton[4];
    private AlertDialogSet ads = new AlertDialogSet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ron2);

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得Layout物件
        findView();

        //取得Intent傳值
        getExtra();

        //顯示玩家名稱到CheckBox與RadioButton
        setText_cbrb();
    }

    //取得Layout物件
    private void findView() {
        cb[0] = (CheckBox) findViewById(R.id.cb1);
        cb[1] = (CheckBox) findViewById(R.id.cb2);
        cb[2] = (CheckBox) findViewById(R.id.cb3);
        cb[3] = (CheckBox) findViewById(R.id.cb4);
        rg = (RadioGroup)findViewById(R.id.rg);
        rb[0] = (RadioButton) findViewById(R.id.rb1);
        rb[1] = (RadioButton) findViewById(R.id.rb2);
        rb[2] = (RadioButton) findViewById(R.id.rb3);
        rb[3] = (RadioButton) findViewById(R.id.rb4);
    }

    //取得Intent傳值
    private void getExtra() {
        names = getIntent().getStringArrayExtra("names");
    }

    //顯示玩家名稱到CheckBox與RadioButton
    private void setText_cbrb() {
        for (int i = 0; i < 4; i++) {
            cb[i].setText(names[i]);
            rb[i].setText(names[i]);
        }
    }

    //取得輸入
    private boolean getInput() {
        //榮和玩家
        int num = 0;
        for (int i = 0; i < 4; i++) {
            if (cb[i].isChecked()) {
                select_r[i] = true;
                num += 1;
            }
            else select_r[i] = false;
        }
        if (num != 2 && num != 3) {
            ads.showRon2();
            return false;
        }

        //放銃玩家
        switch(rg.getCheckedRadioButtonId()) {
            case R.id.rb1:
                select_h = 0;
                break;
            case R.id.rb2:
                select_h = 1;
                break;
            case R.id.rb3:
                select_h = 2;
                break;
            case R.id.rb4:
                select_h = 3;
                break;
        }
        if (select_r[select_h]) {
            ads.showSameRon();
            return false;
        }
        return true;
    }

    //OK按鈕
    public void b_OK(View v) {
        if (getInput()) {
            Intent intentMain = new Intent();
            intentMain.putExtra("select_r", select_r);
            intentMain.putExtra("select_h", select_h);
            setResult(RESULT_OK, intentMain);
            finish();
        }
    }
}
