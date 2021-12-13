package com.efficacious.restaurantuserapp.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FAVORITE_MENU_DAO {
    @Insert
    void favoriteMenuList(FavoriteMenu favoriteMenu);

    @Query("SELECT * FROM FavoriteMenu")
    List<FavoriteMenu> getFavoriteMenuList();

    @Query("DELETE FROM FavoriteMenu WHERE menuName=(:menuName)")
    void removeMenu(String menuName);

    @Query("DELETE FROM FavoriteMenu")
    void deleteAllData();

}
