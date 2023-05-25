package com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Local
import com.example.myapp.R

class LocalsAdapter : RecyclerView.Adapter<LocalsAdapter.LocalViewHolder>() {

    private val localsList = mutableListOf<Local>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_local, parent, false)
        return LocalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        val local = localsList[position]
        holder.bind(local)
    }

    override fun getItemCount(): Int {
        return localsList.size
    }

    fun setLocals(locals: List<Local>) {
        localsList.clear()
        localsList.addAll(locals)
        notifyDataSetChanged()
    }

    inner class LocalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textAddress: TextView = itemView.findViewById(R.id.textAddress)
        private val textTime: TextView = itemView.findViewById(R.id.textTime)

        fun bind(local: Local) {
            textName.text = local.name
            textAddress.text = local.address
            textTime.text = local.time
        }
    }
}
