package com.example.myapp

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(private val context: Activity, private val arrayList: ArrayList<Product>) : ArrayAdapter<Product>(
    context, R.layout.list_item, arrayList){
    /**
     * Metoda do ListView
     */
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)
        val imageView : ImageView = view.findViewById(R.id.product_picture)
        val name : TextView = view.findViewById(R.id.productName)
        val tags : TextView = view.findViewById(R.id.productHashtags)


        imageView.setImageResource(arrayList[position].imageId)
        name.text = arrayList[position].name
        tags.text = arrayList[position].tags

        return view
//        return super.getView(position, convertView, parent)
    }
}