package io.viper.android.disk.sweeper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException

class DBHelper(context: Context?) :
    OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable(connectionSource, FileRecord::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(
        database: SQLiteDatabase,
        connectionSource: ConnectionSource,
        oldVersion: Int,
        newVersion: Int
    ) {
        try {
            TableUtils.dropTable<FileRecord, Any>(connectionSource, FileRecord::class.java, true)
            onCreate(database, connectionSource)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @get:Throws(SQLException::class)
    val daoImpl: Dao<FileRecord, String>
        get() = getDao(FileRecord::class.java)

    companion object {
        private const val DATABASE_NAME = "FileRecords.db"
        private const val DATABASE_VERSION = 5
    }
}
