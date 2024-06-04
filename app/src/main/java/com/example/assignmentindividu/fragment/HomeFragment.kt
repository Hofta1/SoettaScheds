package com.example.assignmentindividu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.DollDetail
import com.example.assignmentindividu.R
import com.example.assignmentindividu.adapter.DollAdapter
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.items.Doll
import org.json.JSONObject

class HomeFragment : Fragment() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var dollRecyclerView: RecyclerView
    private lateinit var dollAdapter: DollAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(requireContext())
        val dollArray = databaseHelper.getDolls()

        dollRecyclerView = view.findViewById(R.id.dollRecyclerView)

        dollAdapter = DollAdapter(dollArray, object: DollAdapter.OnItemClickListener{
            //waktu click akan pindah ke doll detail page
            override fun onItemClick(item: Doll) {
                val intent = Intent(requireContext(), DollDetail::class.java)
                intent.putExtra("doll",item)
                startActivity(intent)
            }

        },viewLifecycleOwner.lifecycleScope)

        dollRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)
        dollRecyclerView.adapter = dollAdapter

    }

}