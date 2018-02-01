package com.jishin.ankiji.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lana on 30/01/2018.
 */
@Entity(tableName = "chart")
public class Chart {
    @PrimaryKey
    private String id;
    private int correctAnswer;
    private int testTimes;
}
