import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapp.Product
import com.example.myapp.R
import com.google.firebase.database.DataSnapshot

class MyAdapter(private val context: Activity, private val productList: List<DataSnapshot>) : ArrayAdapter<DataSnapshot>(
    context, R.layout.list_item, productList){
    /**
     * Metoda do ListView
     */
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)
        val name : TextView = view.findViewById(R.id.productName)
        val tags : TextView = view.findViewById(R.id.productHashtags)

        val productSnapshot = productList[position]
        val productName = productSnapshot.child("name").value as String?
        val productTags = productSnapshot.child("tags").value as String?

        name.text = productName ?: "Nazwa nieznana"
        tags.text = productTags ?: "Brak tagów"

        return view
    }
}