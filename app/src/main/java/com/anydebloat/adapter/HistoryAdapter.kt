package com.anydebloat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anydebloat.R
import com.anydebloat.manager.AppManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items = listOf<AppManager.HistoryEntry>()

    fun submitList(newItems: List<AppManager.HistoryEntry>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPackage: TextView = itemView.findViewById(R.id.tvHistoryPackage)
        val tvMode: TextView = itemView.findViewById(R.id.tvHistoryMode)
        val tvStatus: TextView = itemView.findViewById(R.id.tvHistoryStatus)
        val tvDate: TextView = itemView.findViewById(R.id.tvHistoryDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val entry = items[position]

        holder.tvPackage.text = entry.packageName
        holder.tvMode.text = entry.mode

        if (entry.success) {
            holder.tvStatus.text = "SUCCESS"
            holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.green))
        } else {
            holder.tvStatus.text = "FAILED"
            holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.red))
        }

        val date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            .format(Date(entry.timestamp))
        holder.tvDate.text = date
    }

    override fun getItemCount() = items.size
}
