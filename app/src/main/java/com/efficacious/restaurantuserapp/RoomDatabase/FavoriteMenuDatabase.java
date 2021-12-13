package com.efficacious.restaurantuserapp.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMenu.class},version = 1,exportSchema = false)
public abstract class FavoriteMenuDatabase extends RoomDatabase {
    public abstract FAVORITE_MENU_DAO favorite_menu_dao();
}
