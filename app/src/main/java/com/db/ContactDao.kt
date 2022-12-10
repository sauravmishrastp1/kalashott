package com.db

import androidx.room.*

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(note : ContactEntity)

    @Delete
     fun delete(note: ContactEntity)

    @Query("Select * from videoTable order by id ASC")
    fun getAllContact(): MutableList<ContactEntity>

    @Query("Select * from videoTable Where title LIKE :search")
    fun search(search:String):MutableList<ContactEntity>

    @Query("DELETE FROM videoTable WHERE id = :id")

    fun deleteById(id: Int)


    @Update
     fun update(note: ContactEntity)

}