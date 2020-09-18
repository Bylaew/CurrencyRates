package com.example.currencyrates
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomAdapter(context: Context, private val arrayListDetails: ArrayList<CurrencyModel>) : BaseAdapter(){

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return arrayListDetails.size
    }

    override fun getItem(position: Int): Any {
        return arrayListDetails[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.content_main, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        listRowHolder.charCode.text = arrayListDetails[position].charCode
        listRowHolder.name.text = arrayListDetails[position].name
        listRowHolder.value.text = arrayListDetails[position].value
        return view
    }
}

private class ListRowHolder(row: View?) {
    val name: TextView = row?.findViewById<TextView>(R.id.name) as TextView
    val charCode: TextView = row?.findViewById<TextView>(R.id.charName) as TextView
    val value: TextView = row?.findViewById<TextView>(R.id.value) as TextView

}