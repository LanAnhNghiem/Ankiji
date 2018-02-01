package com.jishin.ankiji.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jishin.ankiji.model.MojiSet;

import java.util.List;

/**
 * Created by lana on 01/02/2018.
 */
@Dao
public interface MojiSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMojiSet(MojiSet mojiSet);
    @Delete
    public void deleteMojiSet(MojiSet mojiSet);
    @Query("SELECT * FROM `moji-set`")
    public List<MojiSet> loadMojiSet();
}
