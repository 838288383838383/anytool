package com.anydebloat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anydebloat.R
import com.anydebloat.manager.AppManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupAdapter(
    private val onRestoreClick: (AppManager.BackupEntry) -> Unit,
    private val onDeleteClick: (AppManager.BackupEntry) -> Unit
) : ListAdapter<AppManager.BackupEntry, BackupAdapter.BackupViewHolder>(BackupDiffCallback()) {

    inner class BackupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLabel: TextView = itemView.findViewById(R.id.tvBackupLabel)
        val tvDate: TextView = itemView.findViewById(R.id.tvBackupDate)
        val tvCount: TextView = itemView.findViewById(R.id.tvBackupCount)
        val btnRestore: Button = itemView.findViewById(R.id.btnRestore)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_backup, parent, false)
        return BackupViewHolder(view)
    }

    override fun onBindViewHolder(holder: BackupViewHolder, position: Int) {
        val backup = getItem(position)

        holder.tvLabel.text = backup.label
        val date = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            .format(Date(backup.timestamp))
        holder.tvDate.text = date
        holder.tvCount.text = "${backup.packageNames.size} packages"

        holder.btnRestore.setOnClickListener { onRestoreClick(backup) }
        holder.btnDelete.setOnClickListener { onDeleteClick(backup) }
    }

    class BackupDiffCallback : DiffUtil.ItemCallback<AppManager.BackupEntry>() {
        override fun areItemsTheSame(
            oldItem: AppManager.BackupEntry,
            newItem: AppManager.BackupEntry
        ): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(
            oldItem: AppManager.BackupEntry,
            newItem: AppManager.BackupEntry
        ): Boolean {
            return oldItem == newItem
        }
    }
}
