package com.example.jangco


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * A simple [Fragment] subclass.
 */
class ScholarshipFragment : Fragment() {
    private var mainActivity: MainActivity? = null

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var scholarshipRef: CollectionReference = db.collection("ScholarShip")
    private var adapter: ScholarshipRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scholarship, container, false)
        mainActivity = activity as MainActivity
        recyclerView = view.findViewById(R.id.scholarshipFragmentRecyclerView)
        setUpRecyclerView()

        return view
    }

    private fun setUpRecyclerView() {
        val query = scholarshipRef.orderBy("endDate", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<ScholarShip>()
            .setQuery(query, ScholarShip::class.java)
            .build()
        Log.d("test", mainActivity?.userProfile.toString())
        adapter = ScholarshipRecyclerViewAdapter(options, context!!, mainActivity?.userProfile!!)
        recyclerView?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }


    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }


}
