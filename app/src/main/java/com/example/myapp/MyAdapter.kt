import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide.init
import com.example.myapp.Product
import com.example.myapp.ProductDetailsActivity
import com.example.myapp.R
import com.google.firebase.database.DataSnapshot

class MyAdapter(private val context: Activity, private val productList: List<DataSnapshot>) : ArrayAdapter<DataSnapshot>(
    context, R.layout.list_item, productList), AdapterView.OnItemClickListener
    {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedProductSnapshot = parent.getItemAtPosition(position) as DataSnapshot
            val intent = Intent("com.example.ACTION_PRODUCT_DETAILS")
            intent.putExtra("productId", selectedProductSnapshot.key)
            context.startActivity(intent)
        }

        /**
         * Metoda do ListView
         */
        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.list_item, null)
            val name: TextView = view.findViewById(R.id.productName)
            val tags: TextView = view.findViewById(R.id.productHashtags)

            val productSnapshot = productList[position]
            val productName = productSnapshot.child("name").value as String?
            val productTags = productSnapshot.child("tags").value as String?

            name.text = productName ?: "Nazwa nieznana"
            tags.text = productTags ?: "Brak tagów"

            return view
        }
        init {
            // Dodajemy nasłuchiwanie na kliknięcie elementu listy
            val listView = context.findViewById<ListView>(R.id.listView)
            listView.onItemClickListener = this

        }
    }