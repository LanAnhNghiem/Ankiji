package com.jishin.ankiji.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

/*
 * Created by lana on 31/01/2018.
 */
@Dao
public interface MojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMojiList(ArrayList<Moji> list);
    @Delete
    public void deleteMojiList(ArrayList<Moji> list);
}
