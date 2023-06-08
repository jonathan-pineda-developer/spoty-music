package cr.ac.una.spoty

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.room.Room
import cr.ac.una.spoty.dao.SearchQueryDao
import cr.ac.una.spoty.db.AppDatabase
import cr.ac.una.spoty.entity.*
import cr.ac.una.spoty.service.SpotifyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var btnSearch: Button
    private lateinit var txtSearch: EditText
    private val trackList: MutableList<Track> = mutableListOf()
    private var listViewItems: ListView? = null
    private lateinit var trackAdapter: TrackAdapter


    //guardar en la base de datos local
    private lateinit var searchQueryDao: SearchQueryDao
    private lateinit var db: AppDatabase



    private val spotifyServiceToken: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }
    private val spotifyService: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        //searchTracks("iron maiden")

        trackAdapter = TrackAdapter(this, trackList)
        listViewItems?.adapter = trackAdapter

        // Crear la instancia de la base de datos Room
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "search_db")
            .build()

        // Obtener el DAO para las operaciones de búsqueda
        searchQueryDao = db.searchQueryDao()
    }
    private fun initViews() {
        txtSearch = findViewById(R.id.txtSearch)
        btnSearch = findViewById(R.id.btnSearch)
        listViewItems = findViewById(R.id.listViewItems)
        btnSearch.setOnClickListener {
            val query = txtSearch.text.toString()
            searchTracks(query)
        }
    }

    private fun searchTracks(query: String) {
        val clientId = "f710fecf58e242bd8054929a50a08728" /* f13969da015a4f49bb1f1edef2185d4e*/
        val clientSecret = "bb3621a5cb6f402bbb543e2d97c7cd29" /*e3077426f4714315937111d5e82cd918*/
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )


        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if (response.isSuccessful) {
                    val accessTokenResponse = response.body()
                    val accessToken = accessTokenResponse?.accessToken

                    if (accessToken != null) {
                        val searchRequest = spotifyService.searchTrack("Bearer $accessToken", query)
                        searchRequest.enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                                if (response.isSuccessful) {
                                    val trackResponse = response.body()

                                    if (trackResponse != null && trackResponse.tracks.items.isNotEmpty()) {

                                        trackList.clear()
                                        var count = 0 // Variable de conteo para limitar la cantidad de canciones a mostrar

                                        for (track in trackResponse.tracks.items) {
                                            val trackName = track.name
                                            val artistName = track.album.name
                                            val trackUri = track.uri
                                            val imageUrl = track.album.images.firstOrNull()?.url ?: ""

                                            //displayTrackInfo(trackName, artistName, track.album.images[0].url)
                                            displayTrackInfo(trackName, artistName, imageUrl)
                                            count++ // Incrementar el conteo

                                            if (count >= 50) {
                                                break // Detener el bucle después de 50 canciones
                                            }
                                        }
                                        // Guardar la búsqueda en la base de datos local
                                        val searchQuery = SearchQuery(query = query)
                                        saveSearchQuery(searchQuery)
                                    } else {
                                        displayErrorMessage("No se encontraron canciones.")
                                    }
                                } else {
                                    displayErrorMessage("Error en la respuesta del servidor.")
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                displayErrorMessage("Error en la solicitud de búsqueda.")
                            }
                        })
                    } else {
                        displayErrorMessage("Error al obtener el accessToken.")
                    }
                } else {
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })
    }

    private fun displayTrackInfo(trackName: String, artistName: String, imageUrl: String) {
        val album = Album(artistName, listOf(Image(imageUrl, 0, 0)))
        val track = Track(trackName, album, "")
        trackList.add(track)
        trackAdapter.notifyDataSetChanged()
    }

    private fun saveSearchQuery(searchQuery: SearchQuery) {
        // Utilizar una corutina para realizar la operación de inserción en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            searchQueryDao.insertSearchQuery(searchQuery)
        }
    }



    private fun displayErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}