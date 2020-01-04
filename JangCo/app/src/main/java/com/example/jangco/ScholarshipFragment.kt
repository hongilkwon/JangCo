package com.example.jangco


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
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
    private var searchView: SearchView? = null
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
        setHasOptionsMenu(true)

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
        adapter = ScholarshipRecyclerViewAdapter(
            options, context!!, mainActivity?.userProfile!!, mainActivity?.myScholarShipList!!)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.scholarship_fragment_option_menu, menu)

        val searchMenu = menu.findItem(R.id.scholarshipFragmentToolbarSearchMenu)
        searchView = searchMenu.actionView as SearchView
        searchView!!.setQueryHint("검색어 입력")

        val searchAutoComplete = searchView!!.findViewById(androidx.appcompat.R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.setHintTextColor(context!!.getColor(R.color.colorBlack))
        searchAutoComplete.setTextColor(context!!.getColor(R.color.colorBlack))

        val closeIcon = searchView!!.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        closeIcon.setImageDrawable(context!!.getDrawable(R.drawable.ic_close_black_24dp))
    }

}
