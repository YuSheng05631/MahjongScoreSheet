package com.mahjongscoresheet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class Ron2BActivity extends AppCompatActivity {
    private String[] names = new String[4];
    private boolean[] select_r;
    private int select_h, ban1, ban2, fu1, fu2;
    private TextView tv1, tv2;
    private Spinner sp_ban1, sp_ban2, sp_fu1, sp_fu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ron2_b);

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得Layout物件
        findView();

        //取得Intent傳值
        getExtra();

        //顯示玩家名稱到TextView
        setText_tv();
    }

    //取得Layout物件
    private void findView() {
        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        sp_ban1 = (Spinner) findViewById(R.id.sp_ban1);
        sp_ban2 = (Spinner) findViewById(R.id.sp_ban2);
        sp_fu1 = (Spinner) findViewById(R.id.sp_fu1);
        sp_fu2 = (Spinner) findViewById(R.id.sp_fu2);
    }

    //取得Intent傳值
    private void getExtra() {
        names = getIntent().getStringArrayExtra("names");
        select_r = getIntent().getBooleanArrayExtra("select_r");
        select_h = getIntent().getIntExtra("select_h", 0);
    }

    //顯示玩家名稱到RadioButton
    private void setText_tv() {
        String s1 = "選擇飜符(";
        for (int i = 0; i < 4; i++) {
            if (select_r[i]) {
                s1 += names[i] + ")：";
                break;
            }
        }
        String s2 = "選擇飜符(";
        for (int i = 3; i >= 0; i--) {
            if (select_r[i]) {
                s2 += names[i] + ")：";
                break;
            }
        }
        tv1.setText(s1);
        tv2.setText(s2);
    }

    //取得輸入
    private boolean getInput() {
        //飜1
        String s = sp_ban1.getSelectedItem().toString();
        ban1 = Integer.parseInt(s.substring(0, s.indexOf("飜")));
        //飜2
        s = sp_ban2.getSelectedItem().toString();
        ban2 = Integer.parseInt(s.substring(0, s.indexOf("飜")));
        //符1
        s = sp_fu1.getSelectedItem().toString();
        fu1 = Integer.parseInt(s.substring(0, s.indexOf("符")));
        //符2
        s = sp_fu2.getSelectedItem().toString();
        fu2 = Integer.parseInt(s.substring(0, s.indexOf("符")));
        return true;
    }

    //OK按鈕
    public void b_OK(View v) {
        if (getInput()) {
            Intent intentMain = new Intent();
            intentMain.putExtra("select_r", select_r);
            intentMain.putExtra("select_h", select_h);
            intentMain.putExtra("ban1", ban1);
            intentMain.putExtra("ban2", ban2);
            intentMain.putExtra("fu1", fu1);
            intentMain.putExtra("fu2", fu2);
            setResult(RESULT_OK, intentMain);
            finish();
        }
    }
}
