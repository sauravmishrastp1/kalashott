package com.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "videoTable", indices = [Index(value = ["videoId"], unique = true)])
class ContactEntity(
    @ColumnInfo(name = "videoId") val vId: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "duration") val duration: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}