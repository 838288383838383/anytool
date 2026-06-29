package com.anydebloat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anydebloat.R
import com.anydebloat.ui.AppManagerActivity

class AppListAdapter(
    private val onAction: (String, String) -> Unit
) : ListAdapter<AppManagerActivity.AppInfo, AppListAdapter.AppViewHolder>(AppDiffCallback()) {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvAppName)
        val tvPackage: TextView = itemView.findViewById(R.id.tvAppPackage)
        val tvVersion: TextView = itemView.findViewById(R.id.tvAppVersion)
        val tvSize: TextView = itemView.findViewById(R.id.tvAppSize)
        val tvType: TextView = itemView.findViewById(R.id.tvAppType)
        val btnMenu: ImageButton = itemView.findViewById(R.id.btnMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)
        holder.tvName.text = app.name
        holder.tvPackage.text = app.packageName
        holder.tvVersion.text = "v${app.version}"
        holder.tvSize.text = app.size
        holder.tvType.text = if (app.isSystem) "System" else "User"

        holder.btnMenu.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menu.add(0, 1, 0, "Force Stop")
            popup.menu.add(0, 2, 1, "Clear Data")
            popup.menu.add(0, 3, 2, "Disable")
            popup.menu.add(0, 4, 3, "Enable")
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> { onAction(app.packageName, "force_stop"); true }
                    2 -> { onAction(app.packageName, "clear_data"); true }
                    3 -> { onAction(app.packageName, "disable"); true }
                    4 -> { onAction(app.packageName, "enable"); true }
                    else -> false
                }
            }
            popup.show()
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppManagerActivity.AppInfo>() {
        override fun areItemsTheSame(old: AppManagerActivity.AppInfo, new: AppManagerActivity.AppInfo) = old.packageName == new.packageName
        override fun areContentsTheSame(old: AppManagerActivity.AppInfo, new: AppManagerActivity.AppInfo) = old == new
    }
}
