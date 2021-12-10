package com.efficacious.restaurantuserapp.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void menuDataList(MenuData menuData);

    @Query("SELECT * FROM MenuData")
    List<MenuData> getMenuListData();

    @Query("UPDATE MenuData SET price = :Price, qty = :Qty where menuName =(:menuName)" )
    void updateMenuList(Integer Price,Integer Qty,String menuName);

    @Query("DELETE FROM MenuData WHERE menuName=(:menuName)")
    void deleteMenu(String menuName);

    @Query("DELETE FROM MenuData")
    void deleteAllData();


}
