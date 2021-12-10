package com.efficacious.restaurantuserapp.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MenuData.class},version = 1,exportSchema = false)
public abstract class MenuDatabase extends RoomDatabase {
    public abstract DAO dao();
}
