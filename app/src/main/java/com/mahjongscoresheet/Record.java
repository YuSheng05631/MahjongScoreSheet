package com.mahjongscoresheet;

import java.util.ArrayList;
import java.util.List;

public class Record {
    //act: 1=初始，2=榮和，3=自摸，42=一砲雙響，43=一砲三響，5=流局，6=立直
    //     71=手動點數，72=手動局數，73=手動本場，74=手動立直，75=手動名字
    public List<String[]> names;
    public List<int[]> points;
    public List<int[]> richi_p; //各家立直數
    public List<Integer> oya, kyoku, honba, richi, act;
    public List<Boolean> allLast;
    public int count;
    public Record() {
        this.names = new ArrayList<>();
        this.points = new ArrayList<>();
        this.richi_p = new ArrayList<>();
        this.oya = new ArrayList<>();
        this.kyoku = new ArrayList<>();
        this.honba = new ArrayList<>();
        this.richi = new ArrayList<>();
        this.act = new ArrayList<>();
        this.allLast = new ArrayList<>();
        this.count = -1;
    }

    public void push(String[] names, int[] points, int[] richi_p, int oya, int kyoku, int honba, int richi, int act, boolean allLast) {
        this.names.add(new String[] {names[0], names[1], names[2], names[3]});
        this.points.add(new int[] {points[0], points[1], points[2], points[3]});
        this.richi_p.add(new int[] {richi_p[0], richi_p[1], richi_p[2], richi_p[3]});
        this.oya.add(oya);
        this.kyoku.add(kyoku);
        this.honba.add(honba);
        this.richi.add(richi);
        this.act.add(act);
        this.allLast.add(allLast);
        this.count += 1;
    }

    public void pop() {
        this.names.remove(this.count);
        this.points.remove(this.count);
        this.richi_p.remove(this.count);
        this.oya.remove(this.count);
        this.kyoku.remove(this.count);
        this.honba.remove(this.count);
        this.richi.remove(this.count);
        this.act.remove(this.count);
        this.allLast.remove(this.count);
        this.count -= 1;
    }
}
