package com.example.assignmentindividu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.HomePage
import com.example.assignmentindividu.R
import com.example.assignmentindividu.RegisterPage
import com.example.assignmentindividu.adapter.TransactionAdapter
import com.example.assignmentindividu.adapter.TransactionDeletedListener
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data

class TransactionFragment : Fragment(), TransactionDeletedListener {

    override fun onTransactionDeleted() {
        val intent = Intent(requireContext(), HomePage::class.java)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var myTransaction: MutableList<Transaction> = mutableListOf()

        databaseHelper = DatabaseHelper(requireContext())
        myTransaction = databaseHelper.getTransaction(Data.myProfile?.userID)

        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView)
        transactionAdapter = TransactionAdapter(myTransaction,this,viewLifecycleOwner.lifecycleScope,requireContext())
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = transactionAdapter

    }
    
}