package com.mahjongscoresheet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbEndpoint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private String[] names = new String[4];
    private int[] points = new int[4];
    private int[] rPoints = new int[4];
    private int[] ranking = new int[4];
    private int[] uma = new int[4];         //順位馬
    private int[] uma_set = new int[4];     //由東到北，紀錄各名次的順位馬被記錄的次數，用以解決相同名次的問題
    private int startPoint, endPoint, top;  //top: 首位賞
    private TextView[] tv_name = new TextView[4];
    private TextView[] tv_point = new TextView[4];
    private TextView[] tv_rPoint = new TextView[4];
    private Spinner sp_uma;
    private AlertDialogSet ads = new AlertDialogSet(this);
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        pref = getPreferences(MODE_PRIVATE);
        prefEditor = getPreferences(MODE_PRIVATE).edit();

        //螢幕固定為直向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得Layout物件
        findView();

        //取得Intent傳值
        getExtra();

        //載入偏好設定
        loadPref();

        ranking = getRanking(); //排名
        top = (endPoint - startPoint) * 4;  //首位賞

        //計算得點
        getRPoints();

        //顯示玩家名稱與點數到TextView
        setText_tv();

        //Spinner選擇事件
        sp_uma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //計算得點
                getRPoints();

                //顯示玩家名稱與點數到TextView
                setText_tv();

                //儲存偏好設定
                savePref();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    //取得Layout物件
    private void findView() {
        tv_name[0] = (TextView)findViewById(R.id.tv_name1);
        tv_name[1] = (TextView)findViewById(R.id.tv_name2);
        tv_name[2] = (TextView)findViewById(R.id.tv_name3);
        tv_name[3] = (TextView)findViewById(R.id.tv_name4);
        tv_point[0] = (TextView)findViewById(R.id.tv_point1);
        tv_point[1] = (TextView)findViewById(R.id.tv_point2);
        tv_point[2] = (TextView)findViewById(R.id.tv_point3);
        tv_point[3] = (TextView)findViewById(R.id.tv_point4);
        tv_rPoint[0] = (TextView)findViewById(R.id.tv_rPoint1);
        tv_rPoint[1] = (TextView)findViewById(R.id.tv_rPoint2);
        tv_rPoint[2] = (TextView)findViewById(R.id.tv_rPoint3);
        tv_rPoint[3] = (TextView)findViewById(R.id.tv_rPoint4);
        sp_uma = (Spinner) findViewById(R.id.sp_uma);
    }

    //取得Intent傳值
    private void getExtra() {
        names = getIntent().getStringArrayExtra("names");
        points = getIntent().getIntArrayExtra("points");
        startPoint = getIntent().getIntExtra("startPoint", 0);
        endPoint = getIntent().getIntExtra("endPoint", 0);
    }

    //顯示玩家名稱與點數到TextView
    private void setText_tv() {
        for (int i = 0; i < 4; i++) {
            tv_name[i].setText(names[i] + "：");

            tv_point[i].setText(Integer.toString(points[i]));
            if (points[i] >= 0) tv_point[i].setTextColor(0xff0000FF);
            else tv_point[i].setTextColor(0xffFF0000);

            if (rPoints[i] >= 0) {
                tv_rPoint[i].setText("+" + Integer.toString(rPoints[i]));
                tv_rPoint[i].setTextColor(0xff00FF00);
            }
            else {
                tv_rPoint[i].setText(Integer.toString(rPoints[i]));
                tv_rPoint[i].setTextColor(0xffFF0000);
            }
        }
    }

    //儲存偏好設定
    private void savePref() {
        prefEditor.putInt("sp_uma_position", sp_uma.getSelectedItemPosition());
        prefEditor.apply();
    }

    //載入偏好設定
    private void loadPref() {
        sp_uma.setSelection(pref.getInt("sp_uma_position", 0));
    }

    //取得排名
    private int[] getRanking() {
        int[] ranking = new int[4]; //ranking[0] is the ranking of player[0]
        int ct;
        for (int i = 0; i < 4; i++) {
            ct = 0;
            for (int j = 0; j < 4; j++)
                if (points[i] < points[j])
                    ct += 1;
            ranking[i] = ct;
        }
        return ranking;
    }

    //取得輸入
    private void getInput() {
        //順位馬
        switch (sp_uma.getSelectedItemPosition()) {
            case 0:
                uma = new int[] {0, 0, 0, 0};
                break;
            case 1:
                uma = new int[] {10, 5, -5, -10};
                break;
            case 2:
                uma = new int[] {20, 10, -10, -20};
                break;
        }
    }

    //計算得點
    private void getRPoints() {
        //取得輸入
        getInput();
        uma_set = new int[] {0, 0, 0, 0};
        for (int i = 0; i < 4; i++) {
            //歸還返點
            rPoints[i] = points[i] - endPoint;

            //第一名加上首位賞
            if (ranking[i] == 0) rPoints[i] += top;

            //換算成千位
            int r = rPoints[i] % 1000;
            if (-500 <= r && r <= 500) rPoints[i] = rPoints[i] / 1000;
            else if (r > 500) rPoints[i] = rPoints[i] / 1000 + 1;
            else if (r < -500) rPoints[i] = rPoints[i] / 1000 - 1;

            //加上順位馬
            rPoints[i] += uma[ranking[i] + uma_set[ranking[i]]];
            uma_set[ranking[i]] += 1;
        }
    }

    //重開按鈕
    public void clickReset(View v) {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
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
