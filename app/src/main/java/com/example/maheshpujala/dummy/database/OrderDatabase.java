package com.example.maheshpujala.dummy.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.maheshpujala.dummy.dao.OrderDao;
import com.example.maheshpujala.dummy.model.OrderEntity;

/**
 * Created by maheshpujala on 20,September,2018
 */
@Database(entities = {OrderEntity.class}, version = 1,exportSchema = false)
public abstract class OrderDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Order table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract OrderDao orderDao();



    /** The only instance */
    private static OrderDatabase sInstance;

    /**
     * Gets the singleton instance of OrderDatabase.
     *
     * @param context The context.
     * @return The singleton instance of OrderDatabase.
     */
    public static synchronized OrderDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), OrderDatabase.class, "order-database") //database name
                    .build();
        }
        return sInstance;
    }
}
