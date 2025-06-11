package id.ac.binus.dojomovie.model

data class TransactionModel(
    val id: Int = 0,
    val userId: Int,
    val filmId: String,
    val qty: Int,
    val totalPrice: Int,
    val transactionDate: String
)
