package com.goodswarehouse.scanner.domain.track

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goodswarehouse.scanner.TABLE_TRACK
import com.goodswarehouse.scanner.domain.model.TrackModel

@Dao
interface TrackDao {

    @Query("SELECT * FROM $TABLE_TRACK limit 100")
    fun getAll(): List<TrackModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg item: TrackModel): List<Long>


    @Query("DELETE FROM $TABLE_TRACK where trackingId in (:ids)")
    fun delete(ids: List<String>)

    @Query("DELETE FROM $TABLE_TRACK")
    fun deleteAll()

}