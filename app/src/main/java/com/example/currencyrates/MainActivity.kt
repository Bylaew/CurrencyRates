package com.example.currencyrates

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var progress: ProgressBar
    lateinit var listViewDetails: ListView
    var arrayListDetails: ArrayList<CurrencyModel> = ArrayList();
    val cbUrl: String = "https://www.cbr-xml-daily.ru/daily_json.js"
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "todo", Snackbar.LENGTH_LONG)
                .setAction("Добавить", null).show()
        }

        progress = findViewById(R.id.progressBar)
        progress.visibility = View.VISIBLE
        listViewDetails = findViewById(R.id.listView)

        run(cbUrl)

        listViewDetails.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(view.context, CurrencyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("numCode", arrayListDetails[position].numCode)
            intent.putExtra("name", arrayListDetails[position].name)
            intent.putExtra("value", arrayListDetails[position].value)
            intent.putExtra("charCode", arrayListDetails[position].charCode)
            view.context.startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun run(url: String) {
        progress.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call, response: Response) {
                val listNames: JSONArray
                val strResponse = response.body()!!.string()
                var model: CurrencyModel
                var size = 0
                var jsonObjInfo: JSONObject
                var jsonContact = JSONObject(strResponse)

                jsonContact = jsonContact.getJSONObject("Valute")
                listNames = jsonContact.names()!!
                size = listNames.length()
                arrayListDetails = ArrayList();

                for (i in 0 until size) {
                    jsonObjInfo = jsonContact.getJSONObject(listNames[i].toString())
                    model = CurrencyModel(
                        jsonObjInfo["CharCode"] as String,
                        jsonObjInfo["Name"] as String,
                        jsonObjInfo["Value"].toString(),
                        jsonObjInfo["NumCode"] as String
                    );
                    arrayListDetails.add(model)
                }

                runOnUiThread {
                    val objAdapter =
                        CustomAdapter(applicationContext, arrayListDetails)
                    listViewDetails.adapter = objAdapter
                }
                progress.visibility = View.INVISIBLE
            }
        })
    }
}




