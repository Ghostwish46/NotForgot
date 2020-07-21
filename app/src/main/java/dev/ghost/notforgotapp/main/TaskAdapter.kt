package dev.ghost.notforgotapp.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.entities.*
import dev.ghost.notforgotapp.helpers.ItemType
import dev.ghost.notforgotapp.taskinfo.TaskInfoActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_task_info.view.*
import kotlinx.android.synthetic.main.view_holder_task_content.view.*
import kotlinx.android.synthetic.main.view_holder_task_header.view.*

class TaskAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var allItems: MutableList<ItemForList> = mutableListOf()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

//    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    }

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
        } else if (current is TaskWithCategoryAndPriority) {
            holder.itemView.textViewTaskContentName.text = current.task.title
            holder.itemView.textViewTaskContentDescription.text = current.task.description
            holder.itemView.textViewTaskContentPriorityColor
                .setBackgroundColor(Color.parseColor(current.priority.color.toString()))
            holder.itemView.checkBoxTaskContentDone.isChecked = current.task.getDoneBoolean()

            holder.itemView.setOnClickListener {
                val intentInfo = Intent(holder.itemView.context, TaskInfoActivity::class.java)
                intentInfo.putExtra(MainActivity.TASK, current)
                holder.itemView.context.startActivity(intentInfo)
            }
        }
    }


    fun getItemByViewHolder(viewHolder: RecyclerView.ViewHolder):ItemForList
    {
        return allItems[viewHolder.adapterPosition]
    }

    internal fun updateData(tasksFullInfo: List<TaskWithCategoryAndPriority>) {
        allItems.clear()

        tasksFullInfo.distinctBy {
            it.category
        }.forEach { currentCategory ->
            allItems.add(currentCategory.category)
            tasksFullInfo.filter {
                it.category == currentCategory.category
            }.forEach {
                allItems.add(it)
            }
        }

//        categoriesAndTasks.filter {
//            it.tasks.isNotEmpty()
//        }.forEach {
//            allItems.add(it.category)
//            it.tasks.forEach {
//                allItems.add(it)
//            }
//        }
        notifyDataSetChanged()
    }
}