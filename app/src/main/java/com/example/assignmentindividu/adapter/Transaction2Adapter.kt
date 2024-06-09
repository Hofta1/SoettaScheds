package com.example.assignmentindividu.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.R
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.database.loadImage
import com.example.assignmentindividu.items.Flight
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private lateinit var databaseHelper: DatabaseHelper

class Transaction2Adapter(private val flightList: MutableList<Flight>, private val itemClickListener: OnItemClickListener, private val listener: TransactionDeletedListener, private val coroutineScope: CoroutineScope, private val context: Context): RecyclerView.Adapter<Transaction2Adapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(item: Flight)
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //tempat binding komponen transaction list
        val airplane: TextView = itemView.findViewById(R.id.airplaneNameTV)
        val flightNumber: TextView = itemView.findViewById(R.id.flightNumberTV)
        val timeTV: TextView = itemView.findViewById(R.id.timeTV)
        val flightIdTV: TextView = itemView.findViewById(R.id.flightIdTV)
        val imageView: ImageView = itemView.findViewById(R.id.airlineIV)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout2, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Set data berdasarkan komponen
        databaseHelper = DatabaseHelper(context)
        val currentItem = flightList[position]
        holder.airplane.text = currentItem.airlineName
        holder.flightNumber.text = currentItem.flightNumber
        holder.timeTV.text = currentItem.departureTime
        holder.flightIdTV.text = ("${currentItem.departureId} - ${currentItem.arrivalId}")

        val currentTransaction = Transaction(Data.myProfile?.userID)
        coroutineScope.launch {
            loadImage(holder.imageView,currentItem.airlineImage!!)
        }
//        holder.imageView.setImageResource(currentItem.path!!)
        val flightId = currentItem.flightID.toString().toIntOrNull()
        //when editting quantity active

        holder.imageView.setOnClickListener{
            itemClickListener.onItemClick(currentItem)
        }

        holder.deleteButton.setOnClickListener{
            showDeleteConfirmation(holder.itemView.context,position,flightId,currentItem)
        }

    }
    private fun showDeleteConfirmation(context: Context, position: Int, transactionId: Int?, flight: Flight){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to delete this?")
        builder.setPositiveButton("Yes"){ dialog, _ ->
            //hapus di db
            databaseHelper = DatabaseHelper(context)
            databaseHelper.deleteTransaction(Data.myProfile?.userID, flight.flightID)
            flightList.removeAt(position)
            notifyItemRemoved(position)
            val toast = Toast.makeText(context,"Transaction deleted successfully",Toast.LENGTH_SHORT)
            toast.show()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog, _ ->
            val toast = Toast.makeText(context,"Transaction not deleted",Toast.LENGTH_SHORT)
            toast.show()
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}