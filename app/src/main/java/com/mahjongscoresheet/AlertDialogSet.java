package com.mahjongscoresheet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.text.InputFilter;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AlertDialogSet {
    private AlertDialog.Builder ad;
    private AppCompatActivity activity;
    private int lastShow;   //上次顯示哪一個警示窗，用以節省執行速度
    public AlertDialogSet(AppCompatActivity activity){
        this.activity = activity;
        this.lastShow = -1;
    }

    //退出警示窗: 0
    public void showQuit() {
        if (lastShow == 0) {
            ad.show();
            return;
        }
        lastShow = 0;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("離開");
        ad.setMessage("確定要離開？");
        ad.setCancelable(true);
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                activity.setResult(0);
                activity.finish();
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad.show();
    }

    //無點數警示窗: 1
    public void showNoPoint() {
        if (lastShow == 1) {
            ad.show();
            return;
        }
        lastShow = 1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("錯誤");
        ad.setMessage("忘記輸入點數了喔！");
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //返點小於起點警示窗: 2
    public void showEndPoint() {
        if (lastShow == 2) {
            ad.show();
            return;
        }
        lastShow = 2;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("錯誤");
        ad.setMessage("返點必須大於起點喔！");
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //榮和與放銃相同警示窗: 3
    public void showSameRon() {
        if (lastShow == 3) {
            ad.show();
            return;
        }
        lastShow = 3;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("錯誤");
        ad.setMessage("不可以榮和自己喔！");
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //一砲多響榮和玩家數警示窗: 4
    public void showRon2() {
        if (lastShow == 4) {
            ad.show();
            return;
        }
        lastShow = 4;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("錯誤");
        ad.setMessage("榮和要2或3個玩家喔！");
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //立直小於1000警示窗: 5
    public void showRichi() {
        if (lastShow == 5) {
            ad.show();
            return;
        }
        lastShow = 5;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("錯誤");
        ad.setMessage("點數小於1000無法立直喔！");
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //延長賽警示窗(小於返點): 6
    public void showExtraTime_endPoint() {
        if (lastShow == 6) {
            ad.show();
            return;
        }
        lastShow = 6;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("進入延長賽");
        ad.setMessage("第一名的點數尚未超過返點，進入延長賽。");
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //延長賽警示窗(兩個第一): 7
    public void showExtraTime_twoTop() {
        if (lastShow == 7) {
            ad.show();
            return;
        }
        lastShow = 7;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("進入延長賽");
        ad.setMessage("有兩個第一名，進入延長賽。");
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //還原警示窗
    public void showBack(final Game game, final Record record) {
        lastShow = -1;
        String s = "";
        switch (record.act.get(record.count)) {
            case 2:
                s = "確定要還原上一個動作嗎？\n動作：榮和。";
                break;
            case 3:
                s = "確定要還原上一個動作嗎？\n動作：自摸。";
                break;
            case 42:
                s = "確定要還原上一個動作嗎？\n動作：一砲雙響。";
                break;
            case 43:
                s = "確定要還原上一個動作嗎？\n動作：一砲三響。";
                break;
            case 5:
                s = "確定要還原上一個動作嗎？\n動作：流局。";
                break;
            case 6:
                s = "確定要還原上一個動作嗎？\n動作：立直。";
                break;
            case 71:
                s = "確定要還原上一個動作嗎？\n動作：手動更改點數。";
                break;
            case 72:
                s = "確定要還原上一個動作嗎？\n動作：手動更改局數。";
                break;
            case 73:
                s = "確定要還原上一個動作嗎？\n動作：手動更改本場。";
                break;
            case 74:
                s = "確定要還原上一個動作嗎？\n動作：手動更改立直棒。";
                break;
            case 75:
                s = "確定要還原上一個動作嗎？\n動作：手動更改名字。";
                break;
        }

        ad = new AlertDialog.Builder(activity);
        ad.setTitle("還原");
        ad.setMessage(s);
        ad.setCancelable(true);
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                record.pop();
                game.setRecord(record);
                game.setText_all();
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad.show();
    }

    //點數變動警示窗
    public void showPoint(String s) {
        lastShow = -1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("點數變動");
        ad.setMessage(s);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //點差查詢警示窗
    public void showDiffer(String s) {
        lastShow = -1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("與第一名點差");
        ad.setMessage(s);
        ad.setCancelable(true);
        ad.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    //重開警示窗
    public void showReset() {
        lastShow = -1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("重新開局");
        ad.setMessage("確定要重新開局？");
        ad.setCancelable(true);
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                //進入NameActivity
                Intent intentName = new Intent(activity, NameActivity.class);
                activity.startActivityForResult(intentName, 1);
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad.show();
    }



    //飛走警示窗
    public void showFly(final String[] names, final int[] points, final int startPoint, final int endPoint) {
        lastShow = -1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("對局結束");
        ad.setMessage("有人飛走了喔QQ");
        ad.setCancelable(false);
        ad.setNegativeButton("成績結算", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                enterEndActivity(names, points, startPoint, endPoint);
            }
        });
        ad.show();
    }

    //最後一局警示窗
    public void showLast(String s, final String[] names, final int[] points, final int startPoint, final int endPoint) {
        lastShow = -1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("對局結束");
        ad.setMessage(s + "結束。");
        ad.setCancelable(false);
        ad.setNegativeButton("成績結算", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                enterEndActivity(names, points, startPoint, endPoint);
            }
        });
        ad.show();
    }

    //All Last 末莊拿第一警示窗
    public void showAllLastTop(final String[] names, final int[] points, final int startPoint, final int endPoint) {
        lastShow = -1;
        ad = new AlertDialog.Builder(activity);
        ad.setTitle("對局結束");
        ad.setMessage("All Last 末莊成為第一名。");
        ad.setCancelable(false);
        ad.setNegativeButton("成績結算", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                enterEndActivity(names, points, startPoint, endPoint);
            }
        });
        ad.show();
    }

    //進入EndActivity
    private void enterEndActivity(String[] names, int[] points, int startPoint, int endPoint) {
        Intent intentEnd = new Intent(activity, EndActivity.class);
        intentEnd.putExtra("names", names);
        intentEnd.putExtra("points", points);
        intentEnd.putExtra("startPoint", startPoint);
        intentEnd.putExtra("endPoint", endPoint);
        activity.startActivity(intentEnd);
        activity.finish();
    }



    //建立一個Spinner
    private Spinner makeSpinner(String item, Game game) {
        if (!item.equals("局數")) return null;
        Spinner sp = new Spinner(activity);
        ArrayAdapter<String> adp = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, game.kyokuArray);
        sp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);
        sp.setSelection(game.kyoku % 4);
        return sp;
    }

    //依照item建立一個EditText
    private EditText makeEditText(String item, String original) {
        if (item.equals("局數")) return null;
        EditText et = new EditText(activity);
        if (item.equals("玩家名稱")) {
            et.setText(original);
            et.setInputType(96);
        }
        else if (item.equals("玩家點數")) {
            et.setText(original);
            et.setInputType(2);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});  //maxLength
        }
        else if (item.equals("本場數")) {
            et.setText(original.substring(0, original.indexOf('本')));
            et.setInputType(2);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});  //maxLength
        }
        else if (item.equals("立直棒")) {
            et.setText(original.substring(original.indexOf('x') + 1));
            et.setInputType(2);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});  //maxLength
        }
        return et;
    }

    //依照item修改Game內容
    private void alterGame(String item, TextView tv, Game game, EditText et, Spinner sp, Record record) {
        if (item.equals("玩家名稱")) {
            tv.setText(et.getText().toString());
            game.setNames();
            record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 75, game.allLast);
        }
        else if (item.equals("玩家點數")) {
            //先從String轉int，再從int轉String，去掉數字前面的0
            tv.setText(Integer.toString(Integer.parseInt(et.getText().toString())));
            game.setPoints();
            record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 71, game.allLast);
        }
        else if (item.equals("局數")) {
            game.kyoku = sp.getSelectedItemPosition();
            game.setText_kyoku();
            game.oya = game.kyoku % 4;
            if (game.kyoku >= game.allRound * 4 - 1) game.allLast = true;
            record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 72, game.allLast);
        }
        else if (item.equals("本場數")) {
            String s = et.getText().toString();
            int i = Integer.parseInt(s);
            game.honba = i;
            s = Integer.toString(i) + "本場";
            tv.setText(s);
            record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 73, game.allLast);
        }
        else if (item.equals("立直棒")) {
            String s = et.getText().toString();
            int i = Integer.parseInt(s);
            game.richi = i;
            s = "立直棒x" + Integer.toString(i);
            tv.setText(s);
            record.push(game.names, game.points, game.richi_p, game.oya, game.kyoku, game.honba, game.richi, 74, game.allLast);
        }
    }

    //手動更改
    public void showAlter(final String item, final TextView tv, final Game game, final Record record) {
        lastShow = -1;
        //依照item建立一個EditText
        final EditText et = makeEditText(item, tv.getText().toString());
        //建立一個Spinner
        final Spinner sp = makeSpinner(item, game);

        ad = new AlertDialog.Builder(activity);
        ad.setTitle("手動更改");
        ad.setMessage("手動更改" + item + "：");
        ad.setCancelable(true);
        if (!item.equals("局數")) ad.setView(et);
        else ad.setView(sp);
        ad.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (item.equals("局數") || !et.getText().toString().isEmpty()) {
                    //依照item修改Game內容
                    alterGame(item, tv, game, et, sp, record);
                }
            }
        });
        ad.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad.show();
    }
}
