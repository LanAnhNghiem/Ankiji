package com.jishin.ankiji.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jishin.ankiji.model.User;

/**
 * Created by lana on 31/01/2018.
 */

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUser(User... user);

    @Update
    public void updateUser(User... user);

    @Query("SELECT * FROM user WHERE id = :id")
    public User loadUser(String id);

}
