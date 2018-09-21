package com.example.maheshpujala.dummy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.example.maheshpujala.dummy.model.OrderEntity;

import java.util.List;

@Dao
public interface OrderDao {

    /**
     * Return all  Orders in the table.
     *
     * @return all  Orders.
     */
    @Query("SELECT * FROM " + OrderEntity.TABLE_NAME)
    List<OrderEntity> getAllOrders();

    /**
     * Inserts a OrderEntity into the table.
     *
     * @param OrderEntity A new OrderEntity.
     * @return The row ID of the newly inserted OrderEntity.
     */
    @Insert
    long insert(OrderEntity OrderEntity);

    /**
     * Inserts multiple Order into the database
     *
     * @param Order An array of new Order.
     * @return The row IDs of the newly inserted Order.
     */
    @Insert
    long[] insertAll(OrderEntity[] Order);

    /**
     * Select all Order.
     *
     * @return A {@link Cursor} of all the Order in the table.
     */
    @Query("SELECT * FROM " + OrderEntity.TABLE_NAME)
    Cursor selectAll();

    /**
     * Select a OrderEntity by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected OrderEntity.
     */
    @Query("SELECT * FROM " + OrderEntity.TABLE_NAME + " WHERE " + OrderEntity.ORDER_ID + " = :id")
    Cursor selectById(String id);

    /**
     * Delete a OrderEntity by the ID.
     *
     * @param id The row ID.
     * @return A number of Order deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + OrderEntity.TABLE_NAME + " WHERE " + OrderEntity.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the OrderEntity. The OrderEntity is identified by the row ID.
     *
     * @param OrderEntity The OrderEntity to update.
     * @return A number of Order updated. This should always be {@code 1}.
     */
    @Update
    int update(OrderEntity OrderEntity);

}
