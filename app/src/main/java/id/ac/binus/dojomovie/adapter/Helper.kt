package id.ac.binus.dojomovie.adapter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Helper(context: Context?) : SQLiteOpenHelper(context, "app.db", null, 10) {

    private val SQL_CREATE_TABLE_USER = """
        CREATE TABLE IF NOT EXISTS user(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            phone TEXT,
            password TEXT
        )
    """.trimIndent()

    private val SQL_CREATE_TABLE_NOW_PLAYING = """
    CREATE TABLE IF NOT EXISTS now_playing(
        id TEXT PRIMARY KEY,
        image TEXT,
        price INTEGER,
        title TEXT,
        description TEXT,
        genre TEXT,
        duration TEXT,
        year TEXT,
        rating TEXT
    )
""".trimIndent()

    private val SQL_CREATE_TABLE_TRANSACTION = """
    CREATE TABLE IF NOT EXISTS film_transaction(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        userId INTEGER,
        filmId TEXT,
        qty INTEGER,
        totalPrice INTEGER,
        transactionDate TEXT
    )
""".trimIndent()


    private val SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS user"
    private val SQL_DROP_TABLE_NOW_PLAYING = "DROP TABLE IF EXISTS now_playing"
    private val SQL_DROP_TABLE_TRANSACTION = "DROP TABLE IF EXISTS film_transaction"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USER)
        db?.execSQL(SQL_CREATE_TABLE_NOW_PLAYING)
        db?.execSQL(SQL_CREATE_TABLE_TRANSACTION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE_USER)
        db?.execSQL(SQL_DROP_TABLE_NOW_PLAYING)
        db?.execSQL(SQL_DROP_TABLE_TRANSACTION)
        onCreate(db)
    }
}