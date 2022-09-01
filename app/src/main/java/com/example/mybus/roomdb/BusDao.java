package com.example.mybus.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mybus.vo.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BusDao {

    @Query("select * from USER_INFO")
    Single<User> getUser();

    @Insert
    Completable register(User user);

    @Query("delete from USER_INFO")
    Completable delete();

    @Query("update USER_INFO set is_Logout = 0 where user_tk =:user_tk")
    Completable logOut(String user_tk);

    @Query("update USER_INFO set is_Logout = 1 where user_tk =:user_tk")
    Completable logIn(String user_tk);
}
