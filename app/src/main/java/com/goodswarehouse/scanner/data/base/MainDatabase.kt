package com.goodswarehouse.scanner.data.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.domain.track.TrackDao

@Database(entities = [TrackModel::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase(){

    abstract fun track(): TrackDao

}