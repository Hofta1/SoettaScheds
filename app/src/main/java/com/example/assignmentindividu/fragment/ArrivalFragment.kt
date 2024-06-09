package com.example.assignmentindividu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.FlightDetail
import com.example.assignmentindividu.HomePage
import com.example.assignmentindividu.R
import com.example.assignmentindividu.adapter.TransactionAdapter
import com.example.assignmentindividu.adapter.TransactionDeletedListener
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.items.Flight

class ArrivalFragment : Fragment(), TransactionDeletedListener {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var titleTV: TextView
    private lateinit var searchView: SearchView

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
        titleTV = view.findViewById(R.id.mainText)
        titleTV.text = "Arrival"
        val allFlight = databaseHelper.getArrivalFlights()
        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView)

        transactionAdapter = TransactionAdapter(allFlight,object: TransactionAdapter.OnItemClickListener{
            //waktu click akan pindah ke flight detail page
            override fun onItemClick(item: Flight) {
                println(item)
                val intent = Intent(requireContext(), FlightDetail::class.java)
                intent.putExtra("flight",item)
                startActivity(intent)
            }

        },this,viewLifecycleOwner.lifecycleScope,requireContext())


        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = transactionAdapter
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    transactionAdapter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    transactionAdapter.filter(newText)
                }
                return false
            }
        })
    }

    override fun onTransactionDeleted() {
        val intent = Intent(requireContext(), HomePage::class.java)
        startActivity(intent)
    }

}