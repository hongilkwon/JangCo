package com.example.jangco


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
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
class ScholarshipFragment : Fragment(), ScholarshipRecyclerViewAdapter.OnItemClickListener {

    private var mainActivity: MainActivity? = null
    private var searchView: SearchView? = null
    private var adapter: ScholarshipRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var filter = 1


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
        Log.d("test", mainActivity?.userProfile.toString())
        adapter = ScholarshipRecyclerViewAdapter(

            context!!,
            mainActivity?.userProfile!!,
            mainActivity?.allScholarShipList!!,
            mainActivity?.fitScholarShipList!!,
            mainActivity?.myScholarShipList!!
        )

        adapter?.setOnItemClickListener(this)
        recyclerView?.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        Log.d("test", position.toString())
        val intent = Intent(context, DetailScholarshipActivity::class.java)
        if(filter == 1)
            intent.putExtra("scholarship", mainActivity?.allScholarShipList?.get(position))
        else
            intent.putExtra("scholarship", mainActivity?.fitScholarShipList?.get(position))

        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.scholarship_fragment_option_menu, menu)

        val searchMenu = menu.findItem(R.id.scholarshipFragmentToolbarSearchMenu)
        searchView = searchMenu.actionView as SearchView
        searchView!!.setQueryHint("검색어 입력")

        val searchAutoComplete =
            searchView!!.findViewById(androidx.appcompat.R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.setHintTextColor(context!!.getColor(R.color.colorBlack))
        searchAutoComplete.setTextColor(context!!.getColor(R.color.colorBlack))

        val closeIcon =
            searchView!!.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        closeIcon.setImageDrawable(context!!.getDrawable(R.drawable.ic_close_black_24dp))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.all -> {
                filter = 1
                adapter?.filter(filter)
                adapter?.notifyDataSetChanged()
            }
            R.id.fit -> {
                //Filter(mainActivity?.userAllInfo!!, mainActivity?.fitScholarShipList!!).compareSQualificationInfo()
                filter = 2
                adapter?.filter(filter)
                adapter?.notifyDataSetChanged()
            }

        }
        return super.onOptionsItemSelected(item)
    }

}
