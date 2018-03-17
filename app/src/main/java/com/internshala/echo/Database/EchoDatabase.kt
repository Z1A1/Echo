package com.internshala.echo.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.widget.DialogTitle
import com.internshala.echo.Database.EchoDatabase.Stataicated.COLUMN_ID
import com.internshala.echo.Database.EchoDatabase.Stataicated.TABLE_NAME

import com.internshala.echo.Songs
import com.internshala.echo.activities.MainActivity

/**
 * Created by admin on 3/2/2018.
 */
class EchoDatabase : SQLiteOpenHelper {


    var songList = ArrayList<Songs>()

    object Stataicated {
        val TABLE_NAME = "favouriteTable"
        val COLUMN_ID = "songId"
        val COLUMN_SONG_tITLE = "songTitle"
        val COLUMN_SONG_ARTITST = "songartist"
        val COLUMN_SONG_PATH = "songPath"
        var DB_VERSION = 1
        val DB_NAME = "favouritedatabase"
    }


    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL(
                "CREATE TABLE " + Stataicated.TABLE_NAME + "( " + Stataicated.COLUMN_ID + " INTEGER," + Stataicated.COLUMN_SONG_ARTITST + " STRING," +
                        Stataicated.COLUMN_SONG_tITLE + " STRING" + Stataicated.COLUMN_SONG_PATH + " STRING);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, Stataicated.DB_NAME, null, Stataicated.DB_VERSION)

    fun storeAsfovurite(id: Int?, artist: String?, songTitle: String?, path: String?) {
        val db = this.writableDatabase
        var contentvlaue = ContentValues()
        contentvlaue.put(Stataicated.COLUMN_ID, id)
        contentvlaue.put(Stataicated.COLUMN_SONG_ARTITST, artist)
        contentvlaue.put(Stataicated.COLUMN_SONG_tITLE, songTitle)
        contentvlaue.put(Stataicated.COLUMN_SONG_PATH, path)
        db.insert(TABLE_NAME, null, contentvlaue)
        db.close()


    }

    fun quertDBlist(): ArrayList<Songs>? {
        try {
            val db = this.readableDatabase
            val query_params = "SELECT * From " + TABLE_NAME
            var cursor = db.rawQuery(query_params, null)
            if (cursor.moveToFirst()) {
                try {

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                do {
                    var _id = cursor.getInt(cursor.getColumnIndexOrThrow(Stataicated.COLUMN_ID))
                    var _artist = cursor.getString(cursor.getColumnIndexOrThrow(Stataicated.COLUMN_SONG_ARTITST))

                    var _title = cursor.getString(cursor.getColumnIndexOrThrow(Stataicated.COLUMN_SONG_tITLE))

                    var _songpath = cursor.getString(cursor.getColumnIndexOrThrow(Stataicated.COLUMN_SONG_PATH))
                    songList.add(Songs(_id.toLong(), _artist, _title, _songpath, 0))


                } while (cursor.moveToNext())
            } else {
                return null
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return songList

    }


    fun checkifIdExists(_id: Int): Boolean {


        var storeId = -1090
        val db = this.readableDatabase
        val query_params = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_ID+" = '$_id'"
         val cursor = db.rawQuery(query_params, null)
        if (cursor.moveToFirst()) {
            do {
                storeId = cursor.getInt(cursor.getColumnIndexOrThrow(Stataicated.COLUMN_ID))

            } while (cursor.moveToNext())
        } else {
            return false
        }
        return storeId != -1090
    }

    fun deleteFavourite(_id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, Stataicated.COLUMN_ID + "=" + _id, null)
        db.close()
    }

    fun checkSize(): Int {
        var counter = 0
        val db = this.readableDatabase
        var query_params = "SELECT * FROM " + Stataicated.TABLE_NAME
        val cSor = db.rawQuery(query_params, null)
        if (cSor.moveToNext()) {
            do {

            } while (cSor.moveToNext())
        } else {
            return 0
        }
        return counter

    }


}