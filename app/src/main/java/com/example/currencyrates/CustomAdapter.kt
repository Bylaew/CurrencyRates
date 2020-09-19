package com.example.currencyrates

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private var arrayListDetails: ArrayList<CurrencyModel>) :
    RecyclerView.Adapter<ListRowHolder>() {

    override fun getItemCount(): Int {
        return arrayListDetails.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRowHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val forRow = layoutInflater.inflate(R.layout.content_main, parent, false)
        return ListRowHolder(forRow)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ListRowHolder, position: Int) {
        val currency = arrayListDetails[position]
        holder.charCode.text = arrayListDetails[position].charCode
        holder.name.text = arrayListDetails[position].name
        holder.value.text = arrayListDetails[position].value
        holder.crm = currency
    }

    fun getList(): ArrayList<CurrencyModel> {
        return this.arrayListDetails
    }
}

class ListRowHolder(row: View?, var crm: CurrencyModel? = null) : RecyclerView.ViewHolder(row!!) {
    val name: TextView = row?.findViewById<TextView>(R.id.name) as TextView
    val charCode: TextView = row?.findViewById<TextView>(R.id.charName) as TextView
    val value: TextView = row?.findViewById<TextView>(R.id.value) as TextView

    init {
        row?.setOnClickListener {
            val intent = Intent(it.context, CurrencyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("numCode", crm?.numCode)
            intent.putExtra("name", crm?.name)
            intent.putExtra("value", crm?.value)
            intent.putExtra("charCode", crm?.charCode)
            it.context.startActivity(intent)
        }
    }
}

