package com.application.mybalancediary;

public class Calories {
    public float totalcalories;
    public float totalfat;
    public float totalcarbs;
    public float totalprotein;
    public Calories() {
    }

    public Calories(float totalcalories, float totalfat, float totalcarbs, float totalprotein) {
        this.totalcalories = totalcalories;
        this.totalfat = totalfat;
        this.totalcarbs = totalcarbs;
        this.totalprotein = totalprotein;
    }
}