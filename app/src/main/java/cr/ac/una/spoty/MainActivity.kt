package cr.ac.una.spoty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import cr.ac.una.spoty.controllers.TrackController
import cr.ac.una.spoty.databinding.ActivityMainBinding
import cr.ac.una.spoty.entity.TrackAdapter

class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        private lateinit var btnSearch: Button
        private lateinit var txtSearch: EditText
        private var listViewItems: ListView? = null
        private lateinit var controller: TrackController

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            controller = TrackController(this)
            initViews()
        }

        fun initViews() {
            txtSearch = findViewById(R.id.txtSearch)
            btnSearch = findViewById(R.id.btnSearch)
            listViewItems = findViewById(R.id.listViewItems)
            btnSearch.setOnClickListener {
                val query = txtSearch.text.toString()
                controller.searchTracks(query)
            }
        }

        fun setTrackAdapter(adapter: TrackAdapter) {
            listViewItems?.adapter = adapter
        }
    }