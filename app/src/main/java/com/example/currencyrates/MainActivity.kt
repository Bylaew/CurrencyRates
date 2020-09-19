package com.example.currencyrates

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var progress: ProgressBar
    lateinit var listViewDetails: ListView
    val arrayListDetails: ArrayList<CurrencyModel> = ArrayList();
    val cbUrl: String = "https://www.cbr-xml-daily.ru/daily_json.js"
    val client = OkHttpClient()
    var objAdapter: CustomAdapter? = null
    private val STATE_LIST = "StateAdapterData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        progress = findViewById(R.id.progressBar)
        progress.visibility = View.VISIBLE
        listViewDetails = findViewById(R.id.listView)
        if (savedInstanceState != null) {
            val savedArrayList: ArrayList<CurrencyModel>? =
                savedInstanceState.getParcelableArrayList(STATE_LIST)!!
            runOnUiThread {
                objAdapter = CustomAdapter(applicationContext, savedArrayList!!)
                listViewDetails.adapter = objAdapter
            }
            update(false, savedArrayList!!)

        } else {
            update(true)
        }
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            view.isEnabled = false
            update(true)
            view.isEnabled = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putParcelableArrayList(STATE_LIST, objAdapter?.getList())
        }

        super.onSaveInstanceState(outState)
    }

    fun update(b: Boolean = false, arr: ArrayList<CurrencyModel> = arrayListDetails) {
        progress.visibility = View.VISIBLE
        if (b) {
            arrayListDetails.clear()
            run(cbUrl)
        }

        listViewDetails.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(view.context, CurrencyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("numCode", arr[position].numCode)
            intent.putExtra("name", arr[position].name)
            intent.putExtra("value", arr[position].value)
            intent.putExtra("charCode", arr[position].charCode)
            view.context.startActivity(intent)
        }
        if(!b) progress.visibility = View.INVISIBLE

    }

    private fun run(url: String) {
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
                    objAdapter = CustomAdapter(applicationContext, arrayListDetails)
                    listViewDetails.adapter = objAdapter
                }

                progress.visibility = View.INVISIBLE
            }
        })
    }


}




