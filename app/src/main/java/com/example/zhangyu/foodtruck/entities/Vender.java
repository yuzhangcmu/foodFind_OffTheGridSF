package com.example.zhangyu.foodtruck.entities;

/**
 * Created by zhangyu on 2/26/15.
 */
public class Vender {
    String name;
    int frequency;

    private int sortKey;

    public Vender(String name, int frequency) {
        this.frequency = frequency;
        this.name = name;

        // Sort the vendor by frequency
        this.sortKey = this.frequency;
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getSortKey() {
        return sortKey;
    }
}
