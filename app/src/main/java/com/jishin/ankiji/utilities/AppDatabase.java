package com.jishin.ankiji.utilities;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.jishin.ankiji.Dao.KanjiDao;
import com.jishin.ankiji.Dao.KanjiSetDao;
import com.jishin.ankiji.Dao.MojiDao;
import com.jishin.ankiji.Dao.MojiSetDao;
import com.jishin.ankiji.Dao.UserDao;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.KanjiSet;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.MojiSet;
import com.jishin.ankiji.model.User;

/**
 * Created by lana on 3/2018.
 */

@Database(entities = {User.class, Kanji.class, Moji.class, MojiSet.class, KanjiSet.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract KanjiDao kanjiDao();
    public abstract MojiDao mojiDao();
    public abstract MojiSetDao mojiSetDao();
    public abstract KanjiSetDao kanjiSetDao();
}
