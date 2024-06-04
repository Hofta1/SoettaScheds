package com.example.assignmentindividu.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.R
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.database.loadImage
import com.example.assignmentindividu.fragment.TransactionFragment
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface TransactionDeletedListener {
    fun onTransactionDeleted()
}

private lateinit var databaseHelper: DatabaseHelper

class TransactionAdapter(private val transactionList: List<Transaction>, private val listener: TransactionDeletedListener, private val coroutineScope: CoroutineScope, private val context: Context): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //tempat binding komponen transaction list
        val dollName = itemView.findViewById<TextView>(R.id.dollNameTV)
        val quantity = itemView.findViewById<TextView>(R.id.transactionQuantityTV)
        val transactionDate = itemView.findViewById<TextView>(R.id.transactionDateTV)
        val transactionId = itemView.findViewById<TextView>(R.id.transactionIDTV)
        val imageView = itemView.findViewById<ImageView>(R.id.transactionIV)
        val editButton = itemView.findViewById<ImageButton>(R.id.editImageButton)
        val quantityET = itemView.findViewById<EditText>(R.id.quantityET)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteImageButton)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Set data berdasarkan komponen
        databaseHelper = DatabaseHelper(context)
        val currentItem = transactionList[position]
        holder.dollName.text = currentItem.dollName
        holder.quantity.text = ("Quantity: ${currentItem.transactionQuantity}")
        holder.transactionDate.text = currentItem.transactionDate
        holder.transactionId.text = ("ID: ${currentItem.transactionID}")
        coroutineScope.launch {
            loadImage(holder.imageView,currentItem.path!!)
        }
//        holder.imageView.setImageResource(currentItem.path!!)
        val transactionId = currentItem.transactionID.toString().toIntOrNull()
        //when editting quantity active
            holder.quantityET.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if(holder.quantityET.text.isNullOrEmpty()|| holder.quantityET.text.toString().toInt() <=0){
                        val toast = Toast.makeText(holder.itemView.context,"Quantity must be more than 0",Toast.LENGTH_SHORT)
                        toast.show()
                    }
                    else{
                        //change display text
                        holder.quantity.text = ("Quantity: ${holder.quantityET.text}")
                        println(holder.quantityET.text.toString().toInt())
                        println(currentItem.transactionID)
                        databaseHelper.editQuantity(holder.quantityET.text.toString().toInt(),
                            currentItem.transactionID!!
                        )
//                        Data.transactionList!![position].transactionQuantity = holder.quantityET.text.toString().toIntOrNull()

                        holder.quantityET.visibility = View.INVISIBLE
                        holder.quantityET.text = null
                        val inputMethodManager = holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(holder.quantityET.windowToken, 0)
                    }
                    true
                }
                else{
                    false
                }
            }

        //when clicking edit button show edit text
         holder.editButton.setOnClickListener{
                holder.quantityET.visibility = View.VISIBLE
                holder.quantityET.requestFocus()
                val inputMethodManager = holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(holder.quantityET, InputMethodManager.SHOW_IMPLICIT)
            }


        holder.deleteButton.setOnClickListener{
            showDeleteConfirmation(holder.itemView.context,position,transactionId,currentItem)
        }

    }
    private fun showDeleteConfirmation(context: Context, position: Int, transactionId: Int?, transaction: Transaction){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to delete this?")
        builder.setPositiveButton("Yes"){dialog, which ->
            //hapus di db
            databaseHelper = DatabaseHelper(context)
            databaseHelper.deleteTransaction(transaction)
            notifyDataSetChanged()
            listener.onTransactionDeleted()
            val toast = Toast.makeText(context,"Transaction deleted successfully",Toast.LENGTH_SHORT)
            toast.show()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, which ->
            val toast = Toast.makeText(context,"Transaction not deleted",Toast.LENGTH_SHORT)
            toast.show()
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}