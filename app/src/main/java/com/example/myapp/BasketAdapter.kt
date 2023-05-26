package com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BasketAdapter : RecyclerView.Adapter<BasketAdapter.MyViewHolder>() {

    private val basketList = mutableListOf<Basket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_basket, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val basket = basketList[position]
        holder.bind(basket)
    }

    override fun getItemCount(): Int {
        return basketList.size
    }

    fun setBasket(baskets: List<Basket>) {
        basketList.clear()
        basketList.addAll(baskets)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        private val textAmount: TextView = itemView.findViewById(R.id.textAmount)

        fun bind(basket: Basket) {
            textName.text = "name: ${basket.name}"
            textPrice.text = "price: ${basket.price} zł"
            textAmount.text = "amount: ${basket.amount.toString()}"
        }
    }
}