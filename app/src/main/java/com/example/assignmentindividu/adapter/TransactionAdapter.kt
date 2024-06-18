package com.example.assignmentindividu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.R
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.database.loadImage
import com.example.assignmentindividu.items.Flight
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface TransactionDeletedListener {
    fun onTransactionDeleted()
}

private lateinit var databaseHelper: DatabaseHelper
private var defaultValue = "Unavailable"
class TransactionAdapter(private var flightList: MutableList<Flight>, private val itemClickListener: OnItemClickListener, private val listener: TransactionDeletedListener, private val coroutineScope: CoroutineScope, private val context: Context): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var fullFlightList: List<Flight> = ArrayList(flightList)
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
        val bookmarkButton: ImageButton = itemView.findViewById(R.id.bookmarkButton)
        val flightItem: ConstraintLayout = itemView.findViewById(R.id.flightItemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
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
        coroutineScope.launch {
            loadImage(holder.imageView,currentItem.airlineImage!!)
        }
        val flightId = currentItem.flightID.toString().toIntOrNull()
        //when editting quantity active

        holder.flightItem.setOnClickListener{
            itemClickListener.onItemClick(currentItem)
        }

        holder.bookmarkButton.setOnClickListener{
            databaseHelper = DatabaseHelper(context)
            if(databaseHelper.checkTransaction(flightId,Data.myProfile?.userID)){
                Toast.makeText(context, "You already booked this flight", Toast.LENGTH_SHORT).show()
            }else{
                databaseHelper.insertTransaction(Transaction(1, Data.myProfile?.userID,flightId))
                Toast.makeText(context, "You successfully booked this flight", Toast.LENGTH_SHORT).show()
                flightList.add(position,currentItem)
                notifyItemChanged(position)
            }
        }

    }
    fun filter(query: String) {
        flightList = if (query.isEmpty()) {
            fullFlightList.toMutableList()
        } else {
            val filteredList = fullFlightList.filter {
                it.airlineName?.contains(query, ignoreCase = true) == true ||
                        it.flightNumber?.contains(query, ignoreCase = true) == true ||
                        it.departureId?.contains(query, ignoreCase = true) == true ||
                        it.arrivalId?.contains(query, ignoreCase = true) == true
            }
            filteredList.toMutableList()
        }
        notifyDataSetChanged()
    }
}