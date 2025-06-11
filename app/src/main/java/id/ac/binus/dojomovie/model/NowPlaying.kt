package id.ac.binus.dojomovie.model

data class NowPlaying(
    val id: String,
    val image: String,
    val price: Int,
    val title: String,
    val description: String = "",
    val genre: String = "",
    val duration: String = "",
    val year: Int? = null,
    val rating: Double? = null
)