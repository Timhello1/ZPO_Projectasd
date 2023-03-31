package com.example.myapp

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class MyAdapter(private val context: Activity, private val arrayList: ArrayList<Product>) : ArrayAdapter<Product>(
    context, R.layout.list_item, arrayList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return super.getView(position, convertView, parent)
    }
}