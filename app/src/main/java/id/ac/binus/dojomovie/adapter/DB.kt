package id.ac.binus.dojomovie.adapter

import android.content.ContentValues
import android.content.Context
import android.util.Log
import id.ac.binus.dojomovie.model.NowPlaying
import id.ac.binus.dojomovie.model.TransactionModel
import id.ac.binus.dojomovie.model.User

class DB {
    companion object {
        var userList = mutableListOf<User>()
        var HAS_SYNC = false
        var LOGGED_IN_USER: User? = null

        fun syncData(ctx: Context) {
            if (HAS_SYNC) return

            val helper = Helper(ctx)
            val db = helper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM user", null)
            userList.clear()

            while (cursor.moveToNext()) {
                val id = cursor.getInt(0)
                val phone = cursor.getString(1)
                val password = cursor.getString(2)

                val temp = User(id, phone, password)
                userList.add(temp)
            }

            cursor.close()
            db.close()
            HAS_SYNC = true
        }

        fun insertNewUser(ctx: Context, phone: String, password: String) {
            var id = 1
            if (userList.isNotEmpty()) {
                id = userList.last().id + 1
            }

            val temp = User(id, phone, password)
            userList.add(temp)

            val helper = Helper(ctx)
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put("phone", phone)
                put("password", password)
            }

            db.insert("user", null, values)

            HAS_SYNC = false // Refresh data on next sync
        }

        fun login(ctx: Context, phone: String, password: String) {
            // Always refresh data before login
            HAS_SYNC = false
            syncData(ctx)

            for (user in userList) {
                if (user.phone == phone && user.password == password) {
                    LOGGED_IN_USER = user
                    Log.d("LoginCheck", "Logged in as userId: ${user.id}")
                    return
                }
            }
            LOGGED_IN_USER = null // Explicitly set to null if not found
        }


        fun insertNewFilms(ctx: Context, film: NowPlaying) {
            val helper = Helper(ctx)
            val db = helper.writableDatabase

            val description: String
            val genre: String
            val duration: String
            val year: Int
            val rating: Double

            when (film.id) {
                "MV001" -> {
                    description = "In a world shaken by titans, Godzilla and Kong clash in a monumental battle for dominance. As humanity searches for answers behind Godzilla’s sudden fury, Kong embarks on a journey to find his true home—only for both to uncover a hidden threat deep within the Earth."
                    genre = "Action/Sci-fi"
                    duration = "1h 55m"
                    year = 2025
                    rating = 5.7
                }
                "MV002" -> {
                    description = "Kimberly (A.J. Cook) has a premonition of a horrible highway accident killing multiple people -- including her and her friends. She blocks the cars behind her on the ramp from joining traffic -- and as a police trooper (Michael Landes) arrives, the accident actually happens. Now, Death is stalking this group of mistaken survivors -- and one by one they are dying as they were supposed to on the highway."
                    genre = "Horror/Crime"
                    duration = "1h 30m"
                    year = 2025
                    rating = 4.7
                }
                "MV003" -> {
                    description = "James Bond has left active service and is enjoying a quiet life in Jamaica. But his peace is short-lived when an old friend from the CIA asks for help. The mission to rescue a kidnapped scientist turns out to be far more dangerous than expected, leading Bond on the trail of a mysterious villain armed with a deadly new technology."
                    genre = "Action/Adventure"
                    duration = "2h 43m"
                    year = 2025
                    rating = 3.5
                }
                else -> {
                    description = "No description"
                    genre = "Unknown"
                    duration = "Unknown"
                    year = 2025
                    rating = 1.2
                }
            }

            try {
                val values = ContentValues().apply {
                    put("id", film.id)
                    put("image", film.image)
                    put("price", film.price)
                    put("title", film.title)
                    put("description", description)
                    put("genre", genre)
                    put("duration", duration)
                    put("year", year)
                    put("rating", rating)
                }
                db.insert("now_playing", null, values)
            } finally {
                db.close()
            }
        }

        fun getAllFilms(ctx: Context): List<NowPlaying> {
            val films = mutableListOf<NowPlaying>()
            val helper = Helper(ctx)
            val db = helper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM now_playing", null)

            while (cursor.moveToNext()) {
                films.add(
                    NowPlaying(
                        id = cursor.getString(0),
                        image = cursor.getString(1),
                        price = cursor.getInt(2),
                        title = cursor.getString(3),
                        description = cursor.getString(4),
                        genre = cursor.getString(5),
                        duration = cursor.getString(6),
                        year = cursor.getInt(7),
                        rating = cursor.getDouble(8)
                    )
                )
            }
            cursor.close()
            db.close()
            return films
        }

        fun getFilmById(ctx: Context, filmId: String): NowPlaying? {
            val helper = Helper(ctx)
            val db = helper.readableDatabase

            val cursor = db.rawQuery("SELECT * FROM now_playing WHERE id = ?", arrayOf(filmId))
            var film: NowPlaying? = null

            if (cursor.moveToFirst()) {
                film = NowPlaying(
                    id = cursor.getString(0),
                    image = cursor.getString(1),
                    price = cursor.getInt(2),
                    title = cursor.getString(3),
                    description = cursor.getString(4),
                    genre = cursor.getString(5),
                    duration = cursor.getString(6),
                    year = cursor.getString(7)?.toIntOrNull(),
                    rating = cursor.getString(8)?.toDoubleOrNull()
                )
            }
            cursor.close()
            db.close()
            return film
        }

        fun insertTransaction(ctx: Context, transaction: TransactionModel): Boolean {
            val helper = Helper(ctx)
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put("userId", transaction.userId)
                put("filmId", transaction.filmId)
                put("qty", transaction.qty)
                put("totalPrice", transaction.totalPrice)
                put("transactionDate", transaction.transactionDate)
            }

            val result = db.insert("film_transaction", null, values)
            db.close()
            return result != -1L
        }

        fun getTransactionsByUser(ctx: Context, userId: Int): List<TransactionModel> {
            val helper = Helper(ctx)
            val db = helper.readableDatabase
            val list = mutableListOf<TransactionModel>()

            val cursor = db.rawQuery("SELECT * FROM film_transaction WHERE userId = ?", arrayOf(userId.toString()))
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val filmId = cursor.getString(cursor.getColumnIndexOrThrow("filmId"))
                val qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty"))
                val totalPrice = cursor.getInt(cursor.getColumnIndexOrThrow("totalPrice"))
                val transactionDate = cursor.getString(cursor.getColumnIndexOrThrow("transactionDate"))

                list.add(TransactionModel(id, userId, filmId, qty, totalPrice, transactionDate))
            }

            cursor.close()
            db.close()
            return list
        }
    }
}