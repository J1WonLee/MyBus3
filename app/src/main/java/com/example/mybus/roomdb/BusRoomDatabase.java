package com.example.mybus.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mybus.vo.User;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class BusRoomDatabase extends RoomDatabase {
    public abstract BusDao getDao();
}
