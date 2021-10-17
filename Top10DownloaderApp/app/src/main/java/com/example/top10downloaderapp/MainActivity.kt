package com.example.top10downloaderapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var rvMain : RecyclerView
    lateinit var submit : Button
    lateinit var itemsList:ArrayList<String>
    var feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submit = findViewById(R.id.submit)

        submit.setOnClickListener{
            requestApi(feedURL)
            initRecyclerView()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.top10MenuItem -> { feedURL ="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
              //  Toast.makeText(this,"10 DownloaderApp" , Toast.LENGTH_SHORT)
                }
            R.id.top100MenuItem -> {feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=100/xml"
          //  Toast.makeText(this,"100 DownloaderApp" , Toast.LENGTH_SHORT)
                    }
            else -> return super.onOptionsItemSelected(item)
        }
        requestApi(feedURL)
        initRecyclerView()
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView()
    {
        rvMain = findViewById(R.id.rvMain)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.setHasFixedSize(true)
    }

    private fun downloadXML(urlPath: String?): String {
        val xmlResult = StringBuilder()

        try {
            val url = URL(urlPath)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val response = connection.responseCode
            Log.d(TAG, "downloadXML: The response code was $response")

            val reader = BufferedReader(InputStreamReader(connection.inputStream))

            val inputBuffer = CharArray(500)
            var charsRead = 0
            while (charsRead >= 0) {
                charsRead = reader.read(inputBuffer)
                if (charsRead > 0) {
                    xmlResult.append(String(inputBuffer, 0, charsRead))
                }
            }
            reader.close()

            Log.d(TAG, "Received ${xmlResult.length} bytes")
            return xmlResult.toString()

        } catch (e: IOException) {
            Log.e(TAG, "downloadXML: IO Exception reading data: ${e.message}")
        }
        return ""
    }

    private fun requestApi(url:String){

        var listItems = ArrayList<FeedEntry>()

        CoroutineScope(Dispatchers.IO).launch {


            val rssFeed = async {

                downloadXML(url)

            }.await()

            if (rssFeed.isEmpty()) {
                Log.e(TAG, "requestApi fun: Error downloading")
            } else {

                val parseApplications = async {

                    RSSFeedParser()

                }.await()

                parseApplications.parse(rssFeed)
                listItems = parseApplications.getParsedList()


                withContext(Dispatchers.Main) {
                    rvMain.adapter = TitleAdapter(listItems)



                }
            }



        }

    }

}