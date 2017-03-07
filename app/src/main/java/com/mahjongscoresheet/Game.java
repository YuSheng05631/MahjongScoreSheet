package com.mahjongscoresheet;

import android.widget.TextView;

public class Game {
    public String[] names = new String[4];
    public int[] points = new int[4];
    public int[] richi_p = new int[4];  //各家立直數
    public int allRound, startPoint, endPoint, oya, kyoku, honba, richi;
    public boolean allLast;
    public TextView[] tv_names = new TextView[4];
    public TextView[] tv_points = new TextView[4];
    public TextView[] tv_richi_p = new TextView[4]; //各家立直數
    public TextView tv_kyoku, tv_honba, tv_richi;
    public String[] kyokuArray = {"東一局", "東二局", "東三局", "東四局", "南一局", "南二局", "南三局", "南四局", "西一局", "西二局", "西三局", "西四局", "北一局", "北二局", "北三局", "北四局"};
    public Game() {
        this.oya = 1;
        this.kyoku = 0;
        this.honba = 0;
        this.richi = 0;
        this.allLast = false;
    }

    //由TextView取得玩家名稱
    public void setNames() {
        for (int i = 0; i < 4; i++) {
            names[i] = tv_names[i].getText().toString();
        }
    }

    //由TextView取得玩家點數
    public void setPoints() {
        for (int i = 0; i < 4; i++) {
            points[i] = Integer.parseInt(tv_points[i].getText().toString());
        }
    }

    //顯示玩家名字到TextView
    public void setText_tv_names() {
        for (int i = 0; i < 4; i++) {
            tv_names[i].setText(names[i]);
        }
    }

    //顯示玩家點數到TextView
    public void setText_tv_points() {
        for (int i = 0; i < 4; i++) {
            tv_points[i].setText(Integer.toString(points[i]));
        }
    }

    //顯示各家立直數道TextView
    public void setText_tv_richi_p() {
        for (int i = 0; i < 4; i++) {
            tv_richi_p[i].setText(Integer.toString(richi_p[i]));
        }
    }

    //顯示局數到TextView
    public void setText_kyoku() {
        tv_kyoku.setText(kyokuArray[kyoku % 16]);
    }

    //顯示本場到TextView
    public void setText_honba() {
        String s = Integer.toString(honba) + "本場";
        tv_honba.setText(s);
    }

    //顯示立直棒到TextView
    public void setText_richi() {
        String s = "立直棒x" + Integer.toString(richi);
        tv_richi.setText(s);
    }

    //顯示除玩家名稱的所有資訊到TextView
    public void setText_all() {
        setText_tv_names();
        setText_tv_points();
        setText_tv_richi_p();
        setText_kyoku();
        setText_honba();
        setText_richi();
    }

    //取得得點
    public int[] getPoint(int ban, int fu, boolean isOya, boolean isTsumo) {
        //點數 = 符 × 2 ^ (飜 + 2）
        //莊家 * 6
        //閒家 * 4
        int[] ps = new int[2];
        int p = fu * (int)Math.pow(2, ban + 2); //基本點
        if (!isOya && isTsumo) { //閒家自摸
            if (ban <= 2 || ban == 3 && fu <= 60 || ban == 4 && fu <= 30) {
                ps[0] = p;
                ps[1] = p * 2;
            }
            else if (ban <= 5) {
                ps[0] = 2000;
                ps[1] = 4000;
            }
            else if (ban <= 7) {
                ps[0] = 3000;
                ps[1] = 6000;
            }
            else if (ban <= 10) {
                ps[0] = 4000;
                ps[1] = 8000;
            }
            else if (ban <= 12) {
                ps[0] = 6000;
                ps[1] = 12000;
            }
            else {
                ps[0] = ban / 13 * 8000;
                ps[1] = ban / 13 * 16000;
            }
        }
        else if (!isOya && !isTsumo) { //閒家榮和
            if (ban <= 2 || ban == 3 && fu <= 60 || ban == 4 && fu <= 30) {
                ps[0] = p * 4;
            }
            else if (ban <= 5) {
                ps[0] = 8000;
            }
            else if (ban <= 7) {
                ps[0] = 12000;
            }
            else if (ban <= 10) {
                ps[0] = 16000;
            }
            else if (ban <= 12) {
                ps[0] = 24000;
            }
            else {
                ps[0] = ban / 13 * 32000;
            }
        }
        else if (isOya && isTsumo) { //莊家自摸
            if (ban <= 2 || ban == 3 && fu <= 60 || ban == 4 && fu <= 30) {
                ps[0] = p * 2;
            }
            else if (ban <= 5) {
                ps[0] = 4000;
            }
            else if (ban <= 7) {
                ps[0] = 6000;
            }
            else if (ban <= 10) {
                ps[0] = 8000;
            }
            else if (ban <= 12) {
                ps[0] = 12000;
            }
            else {
                ps[0] = ban / 13 * 16000;
            }
        }
        else if (isOya && !isTsumo) { //莊家榮和
            if (ban <= 2 || ban == 3 && fu <= 60 || ban == 4 && fu <= 30) {
                ps[0] = p * 6;
            }
            else if (ban <= 5) {
                ps[0] = 12000;
            }
            else if (ban <= 7) {
                ps[0] = 18000;
            }
            else if (ban <= 10) {
                ps[0] = 24000;
            }
            else if (ban <= 12) {
                ps[0] = 36000;
            }
            else {
                ps[0] = ban / 13 * 48000;
            }
        }
        if (ps[0] % 100 != 0) ps[0] = ps[0] / 100 * 100 + 100;
        if (ps[1] % 100 != 0) ps[1] = ps[1] / 100 * 100 + 100;
        return ps;
    }

    //取得排名
    public int[] getRanking() {
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

    //由Record設置屬性
    public void setRecord(Record record) {
        this.names[0] = record.names.get(record.count)[0];
        this.names[1] = record.names.get(record.count)[1];
        this.names[2] = record.names.get(record.count)[2];
        this.names[3] = record.names.get(record.count)[3];
        this.points[0] = record.points.get(record.count)[0];
        this.points[1] = record.points.get(record.count)[1];
        this.points[2] = record.points.get(record.count)[2];
        this.points[3] = record.points.get(record.count)[3];
        this.richi_p[0] = record.richi_p.get(record.count)[0];
        this.richi_p[1] = record.richi_p.get(record.count)[1];
        this.richi_p[2] = record.richi_p.get(record.count)[2];
        this.richi_p[3] = record.richi_p.get(record.count)[3];
        this.oya = record.oya.get(record.count);
        this.kyoku = record.kyoku.get(record.count);
        this.honba = record.honba.get(record.count);
        this.richi = record.richi.get(record.count);
        this.allLast = record.allLast.get(record.count);
    }
}
