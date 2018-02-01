package com.jishin.ankiji.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jishin.ankiji.model.KanjiSet;

import java.util.List;

/**
 * Created by lana on 01/02/2018.
 */
@Dao
public interface KanjiSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertKanjiSet(KanjiSet kanjiSet);
    @Delete
    public void deleteKanjiSet(KanjiSet kanjiSet);
    @Query("SELECT * FROM `kanji-set`")
    public List<KanjiSet> loadKanjiSet();
}
