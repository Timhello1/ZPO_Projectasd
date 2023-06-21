package com.example.myapp

import Basket
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase


class BasketAdapter(private val itemClickListener: BasketItemClickListener) : RecyclerView.Adapter<BasketAdapter.MyViewHolder>() {

    private val basketList = mutableListOf<Basket>()

    interface BasketItemClickListener {
        fun onDeleteButtonClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_basket, parent, false)
        return MyViewHolder(itemView)
    }

    fun getBasket(position: Int): Basket {
        return basketList[position]
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
        val buttonDelete: Button = itemView.findViewById(R.id.button2)

        init {
            buttonDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onDeleteButtonClick(position)
                }
            }
        }
        fun deleteBasketItem(position: Int) {
            val basket = basketList[position]
            val basketId = basket.basketId

            val basketRef = FirebaseDatabase.getInstance().reference.child("baskets").child(basketId)
            basketRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Item deletion from the database was successful
                    // You may perform any additional actions or updates here
                } else {

                }
            }
        }

        fun bind(basket: Basket) {
            textName.text = "name: ${basket.name}"
            textPrice.text = "price: ${basket.price} zł"
            textAmount.text = "amount: ${basket.amount.toString()}"
        }
    }
}