package id.ac.binus.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import id.ac.binus.dojomovie.adapter.DB
import id.ac.binus.dojomovie.adapter.ImageAdapter
import id.ac.binus.dojomovie.adapter.NowPlayingAdapter
import id.ac.binus.dojomovie.model.NowPlaying
import kotlin.math.abs

class DashboardPage : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var adapter1: NowPlayingAdapter? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dashboard_page)

        val ivProfile = findViewById<ImageView>(R.id.ivProfile)
        val ivHistory = findViewById<ImageView>(R.id.ivHistory)
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val btnLoc = findViewById<AppCompatButton>(R.id.btnLoc)

        btnLoc.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        ivProfile.setOnClickListener{
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        ivHistory.setOnClickListener{
            val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)
        }

        ivHome.setOnClickListener {
            val intent = Intent(this, DashboardPage::class.java)
            startActivity(intent)
        }

        val ctx = this
        val url = "https://api.npoint.io/66cce8acb8f366d2a508"
        val requestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val film = NowPlaying(
                        id = obj.getString("id"),
                        image = obj.getString("image"),
                        price = obj.getInt("price"),
                        title = obj.getString("title"),
                    )
                    DB.insertNewFilms(ctx, film)
                }
                val films = DB.getAllFilms(ctx)
                adapter1 = NowPlayingAdapter(films) { selectedFilm ->
                    val intent = Intent(ctx, DetailPage::class.java)
                    intent.putExtra("FILM_ID", selectedFilm.id)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter1
            },
            { error -> Log.e("VolleyError", error.message.toString()) })

        requestQueue.add(jsonArrayRequest)


        recyclerView = findViewById(R.id.rvNowPlaying)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter1

        init()
        setUpTransform()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 2000)
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransform() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{ page, position ->
            val scale = 0.85f + (1 - abs(position)) * 0.15f
            page.scaleY = scale
            page.scaleX = scale
            page.alpha = 0.5f + (1 - abs(position)) * 0.5f
        }
        viewPager2.setPageTransformer(transformer)
    }

    private fun init() {
        viewPager2 = findViewById(R.id.vpSlider)
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.bond_backdrop)
        imageList.add(R.drawable.fantalion_backdrop)
        imageList.add(R.drawable.kongzilla_backdrop)

        adapter = ImageAdapter(imageList, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }
}