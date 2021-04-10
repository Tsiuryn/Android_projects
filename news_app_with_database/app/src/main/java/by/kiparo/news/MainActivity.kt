package by.kiparo.news

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kiparo.news.adapter.NewsListAdapter
import by.kiparo.news.database.News
import by.kiparo.news.repository.Repository
import by.kiparo.news.viewmodel.NewsViewModel
import by.kiparo.news.viewmodel.NewsViewModelFactory
import com.facebook.drawee.backends.pipeline.Fresco

class MainActivity: AppCompatActivity() {
    private lateinit var adapter: NewsListAdapter
    private lateinit var recycler: RecyclerView
    private var list = ArrayList<News>()

    companion object {
        const val TITLE = "title"
        const val STORY_URL = "storyUrl"
        const val SUMMARY = "summary"
        const val IMAGE_URL = "imageURL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)
        recycler = findViewById(R.id.list)
        adapter = NewsListAdapter(list) { news ->
            openDetailActivity(news)
        }
        val vm: NewsViewModel = ViewModelProvider(this, NewsViewModelFactory(Repository(applicationContext))).get(NewsViewModel::class.java)
        vm.listNews.observe(this, Observer {
            adapter.updateAdapter(it)
        })
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = adapter
    }

    private fun openDetailActivity (news: News){
        val title = news.title
        var imageURL = news.imageUrl
        val  summary = news.summary
        val  storyUrl = news.storyUrl
        val intent = Intent (this, DetailViewActivity::class.java)
        intent.putExtra(TITLE, title)
        intent.putExtra(IMAGE_URL, imageURL)
        intent.putExtra(SUMMARY, summary)
        intent.putExtra(STORY_URL, storyUrl)
        startActivity(intent)
    }
}