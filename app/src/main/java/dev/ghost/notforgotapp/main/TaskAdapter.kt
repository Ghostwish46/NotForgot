package dev.ghost.notforgotapp.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.CategoryAndTasks
import dev.ghost.notforgotapp.entities.ItemForList
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.helpers.ItemType
import kotlinx.android.synthetic.main.view_holder_task_content.view.*
import kotlinx.android.synthetic.main.view_holder_task_header.view.*

class TaskAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    //    private var tasks: List<Task> = listOf()
    private var allItems: MutableList<ItemForList> = mutableListOf()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val taskName: TextView = itemView.findViewById(R.id.textViewTaskContentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        var itemView = when (viewType) {
            ItemType.Category.ordinal -> {
                inflater.inflate(R.layout.view_holder_task_header, parent, false)
            }
            else -> {
                inflater.inflate(R.layout.view_holder_task_content, parent, false)
            }
        }

        return TaskViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = allItems[position]
        return currentItem.type.ordinal
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = allItems[position]
        if (current is Category) {
            holder.itemView.textViewTaskHeaderName.text = current.name
        } else if (current is Task) {
            holder.itemView.textViewTaskContentName.text = current.title
            holder.itemView.textViewTaskContentDescription.text = current.description
        }
    }

    internal fun updateData(categoriesAndTasks: List<CategoryAndTasks>) {
        allItems.clear()
        categoriesAndTasks.filter {
            it.tasks.isNotEmpty()
        }.forEach {
            allItems.add(it.category)
            it.tasks.forEach {
                allItems.add(it)
            }
        }
        notifyDataSetChanged()
    }
}