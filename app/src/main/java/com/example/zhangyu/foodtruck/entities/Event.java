package com.example.zhangyu.foodtruck.entities;

import java.util.Date;

/**
 * Created by zhangyu on 2/26/15.
 */
public class Event{
    String name;

    int endTime;
    long startTime;

    String location;
    String timeZone;

    String id;

    private String sortKey;

    public Event(String name, long startTime, String location, String id) {
        this.name = name;
        this.endTime = 0;
        this.startTime = startTime;
        this.location = location;
        this.timeZone = null;
        this.id = id;

        // Sort the event by date
        this.sortKey = startTime + "";
    }

    public Event(String name, int endTime, int startTime, String location, String timeZone, String id) {
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
        this.location = location;
        this.timeZone = timeZone;
        this.id = id;

        // Sort the event by date
        this.sortKey = startTime + "";
    }

    public long getStartTime() {
        return startTime;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getName() {
        return name;
    }

    public String getSortKey() {
        return sortKey;
    }
}
