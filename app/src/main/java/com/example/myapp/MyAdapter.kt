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
import com.example.myapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class MyAdapter(private val context: Activity, private val productList: List<DataSnapshot>) : ArrayAdapter<DataSnapshot>(
    context, R.layout.list_item, productList), AdapterView.OnItemClickListener {

    private var isAdmin: Boolean = false

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val user = Firebase.auth.currentUser
        val email = user?.email

        val db = FirebaseFirestore.getInstance()

        val usersRef = db.collection("admins")
        val userQuery = usersRef.whereEqualTo("email", user?.email)
        userQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            if (task.isSuccessful) {
                isAdmin = task.result != null && !task.result.isEmpty
                if (isAdmin) {
                    val selectedProductSnapshot = parent.getItemAtPosition(position) as DataSnapshot
                    val intent = Intent("com.example.ACTION_ADMIN_PRODUCT_DETAILS")
                    intent.putExtra("productId", selectedProductSnapshot.key)
                    context.startActivity(intent)
                } else {
                    val selectedProductSnapshot = parent.getItemAtPosition(position) as DataSnapshot
                    val intent = Intent("com.example.ACTION_PRODUCT_DETAILS")
                    intent.putExtra("productId", selectedProductSnapshot.key)
                    context.startActivity(intent)
                }
            }
        }
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
        val price: TextView = view.findViewById(R.id.productPrice)


        val productSnapshot = productList[position]
        val productName = productSnapshot.child("name").value as String?
        val productTags = productSnapshot.child("tags").value as String?
        val productPrice = productSnapshot.child("price").value as String?

        name.text = productName ?: "Nazwa nieznana"
        tags.text = productTags ?: "Brak tagów"
        price.text = "${productPrice} zł/szt" ?: "Cena nieznana"

        return view
    }

    init {
        // Dodajemy nasłuchiwanie na kliknięcie elementu listy
        val listView = context.findViewById<ListView>(R.id.listView)
        listView.onItemClickListener = this

    }
}