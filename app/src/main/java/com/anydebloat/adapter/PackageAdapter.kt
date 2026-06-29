package com.anydebloat.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anydebloat.R
import com.anydebloat.models.PackageInfo

class PackageAdapter(
    private val listener: OnPackageClickListener
) : ListAdapter<PackageInfo, PackageAdapter.PackageViewHolder>(PackageDiffCallback()) {

    interface OnPackageClickListener {
        fun onPackageClick(position: Int, packageInfo: PackageInfo)
        fun onPackageLongClick(position: Int, packageInfo: PackageInfo)
    }

    inner class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.cbPackage)
        val tvName: TextView = itemView.findViewById(R.id.tvPackageName)
        val tvDisplayName: TextView = itemView.findViewById(R.id.tvDisplayName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onPackageClick(position, getItem(position))
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onPackageLongClick(position, getItem(position))
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package, parent, false)
        return PackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val pkg = getItem(position)

        holder.tvDisplayName.text = pkg.displayName
        holder.tvName.text = pkg.packageName
        holder.tvCategory.text = pkg.category
        holder.checkBox.isChecked = pkg.isSelected

        // Status
        when {
            !pkg.isInstalled -> {
                holder.tvStatus.text = "NOT INSTALLED"
                holder.tvStatus.setTextColor(Color.GRAY)
                holder.itemView.alpha = 0.5f
            }
            !pkg.isEnabled -> {
                holder.tvStatus.text = "DISABLED"
                holder.tvStatus.setTextColor(Color.RED)
                holder.itemView.alpha = 0.7f
            }
            else -> {
                holder.tvStatus.text = "ACTIVE"
                holder.tvStatus.setTextColor(Color.GREEN)
                holder.itemView.alpha = 1.0f
            }
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            pkg.isSelected = isChecked
        }
    }

    class PackageDiffCallback : DiffUtil.ItemCallback<PackageInfo>() {
        override fun areItemsTheSame(oldItem: PackageInfo, newItem: PackageInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: PackageInfo, newItem: PackageInfo): Boolean {
            return oldItem == newItem
        }
    }
}
