package com.db
import android.content.Context
import androidx.room.*

@Database(entities = arrayOf(ContactEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getContactDao(): ContactDao

    companion object {
        // Singleton prevents multiple
        // instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "videoDb").allowMainThreadQueries().build()
                INSTANCE = instance
                // return instance
               return instance
            }
        }
    }
}
