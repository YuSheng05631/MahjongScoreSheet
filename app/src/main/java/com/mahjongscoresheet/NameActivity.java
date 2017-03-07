package com.mahjongscoresheet;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;

public class NameActivity extends AppCompatActivity {
    private String[] names = new String[4];
    private int startPoint, endPoint, allRound;
    private EditText[] et_names = new EditText[4];
    private EditText et_startPoint, et_endPoint;
    private RadioGroup rg;
    private RadioButton[] rb = new RadioButton[3];
    private AlertDialogSet ads = new AlertDialogSet(this);
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        pref = getPreferences(MODE_PRIVATE);
        prefEditor = getPreferences(MODE_PRIVATE).edit();

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得Layout物件
        findView();

        //載入偏好設定
        loadPref();
    }

    //取得Layout物件
    private void findView() {
        et_names[0] = (EditText) findViewById(R.id.et_name1);
        et_names[1] = (EditText) findViewById(R.id.et_name2);
        et_names[2] = (EditText) findViewById(R.id.et_name3);
        et_names[3] = (EditText) findViewById(R.id.et_name4);
        et_startPoint = (EditText) findViewById(R.id.et_startPoint);
        et_endPoint = (EditText) findViewById(R.id.et_endPoint);
        rg = (RadioGroup) findViewById(R.id.rg);
        rb[0] = (RadioButton) findViewById(R.id.rb1);
        rb[1] = (RadioButton) findViewById(R.id.rb2);
        rb[2] = (RadioButton) findViewById(R.id.rb3);
    }

    //取得輸入
    private boolean getInput() {
        //對戰局數
        switch(rg.getCheckedRadioButtonId()) {
            case R.id.rb1:
                allRound = 1;
                break;
            case R.id.rb2:
                allRound = 2;
                break;
            case R.id.rb3:
                allRound = 4;
                break;
        }
        //起點
        String input = et_startPoint.getText().toString();
        if (input.isEmpty() || input.isEmpty()) {
            ads.showNoPoint();
            return false;
        }

        startPoint = Integer.parseInt(input);
        //返點
        input = et_endPoint.getText().toString();
        if (input.isEmpty() || input.isEmpty()) {
            ads.showNoPoint();
            return false;
        }
        endPoint = Integer.parseInt(input);
        if (endPoint < startPoint) {
            ads.showEndPoint();
            return false;
        }
        //玩家名稱
        for (int i = 0; i < 4; i++) names[i] = et_names[i].getText().toString();
        return true;
    }

    //儲存偏好設定
    private void savePref() {
        prefEditor.putInt("allRound", allRound);
        prefEditor.putInt("startPoint", startPoint);
        prefEditor.putInt("endPoint", endPoint);
        prefEditor.apply();
    }

    //載入偏好設定
    private void loadPref() {
        allRound = pref.getInt("allRound", 2);
        startPoint = pref.getInt("startPoint", 25000);
        endPoint = pref.getInt("endPoint", 30000);
        et_startPoint.setText(Integer.toString(startPoint));
        et_endPoint.setText(Integer.toString(endPoint));
        if (allRound == 1) rb[0].setChecked(true);
        else if (allRound == 2) rb[1].setChecked(true);
        else if (allRound == 4) rb[2].setChecked(true);
    }

    //OK按鈕
    public void b_OK(View v) {
        if (getInput()) {
            //儲存偏好設定
            savePref();

            Intent intentMain = new Intent();
            intentMain.putExtra("names", names);
            intentMain.putExtra("allRound", allRound);
            intentMain.putExtra("startPoint", startPoint);
            intentMain.putExtra("endPoint", endPoint);
            setResult(RESULT_OK, intentMain);
            finish();
        }
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
