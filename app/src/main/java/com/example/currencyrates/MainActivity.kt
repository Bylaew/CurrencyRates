package com.example.currencyrates

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var progress: ProgressBar
    lateinit var recyclerViewDetails: RecyclerView
    val arrayListDetails: ArrayList<CurrencyModel> = ArrayList()
    val client = OkHttpClient()
    var objAdapter: CustomAdapter? = null
    private val STATE_LIST = "StateAdapterData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerViewDetails = findViewById(R.id.recycler_view)
        recyclerViewDetails.layoutManager = LinearLayoutManager(this)
        progress = findViewById(R.id.progress_bar)
        progress.visibility = View.VISIBLE
        if (savedInstanceState != null) {
            val savedArrayList: ArrayList<CurrencyModel>? =
                savedInstanceState.getParcelableArrayList(STATE_LIST)!!
            runOnUiThread {
                objAdapter = CustomAdapter(savedArrayList!!)
                recyclerViewDetails.adapter = objAdapter
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
            arr.clear()
            run(getString(R.string.central_bank_url))
        }

        if (!b) progress.visibility = View.INVISIBLE

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
                val size: Int
                var model: CurrencyModel
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
                    )
                    arrayListDetails.add(model)

                }
                runOnUiThread {
                    objAdapter = CustomAdapter(arrayListDetails)
                    recyclerViewDetails.adapter = objAdapter
                }

                progress.visibility = View.INVISIBLE
            }
        })
    }
}




