package dev.ghost.notforgotapp.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ItemCategoryBinding
import dev.ghost.notforgotapp.databinding.ItemTaskBinding
import dev.ghost.notforgotapp.entities.*
import dev.ghost.notforgotapp.helpers.ItemType
import dev.ghost.notforgotapp.taskinfo.TaskInfoActivity
import kotlinx.android.synthetic.main.item_task.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.coroutines.launch

class ItemsAdapter internal constructor(
    context: Context,
    // I know, it's dirty, but i will find the solution.
    private val mainActivityViewModel: MainActivityViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var allItems: MutableList<ItemForList> = mutableListOf()

    // Two ViewHolders for different types of list
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskFullInfo: TaskWithCategoryAndPriority) {
            binding.taskFullInfo = taskFullInfo;
            binding.executePendingBindings();
        }
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.category = category;
            binding.executePendingBindings();
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        // Return data binding for different types of VH
        return when (viewType) {
            ItemType.Category.ordinal -> {
                val categoryBinding: ItemCategoryBinding =
                    DataBindingUtil.inflate(inflater, R.layout.item_category, parent, false)
                CategoryViewHolder(categoryBinding)
            }
            else -> {
                val taskBinding: ItemTaskBinding =
                    DataBindingUtil.inflate(inflater, R.layout.item_task, parent, false)
                TaskViewHolder(taskBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = allItems[position]
        return currentItem.type.ordinal
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val category = allItems[position] as Category
            holder.bind(category)
        } else if (holder is TaskViewHolder) {
            val taskFullInfo = allItems[position] as TaskWithCategoryAndPriority
            holder.bind(taskFullInfo)

            // I need to do something with this mess...
            holder.itemView.setOnClickListener {
                val intentInfo = Intent(holder.itemView.context, TaskInfoActivity::class.java)
                intentInfo.putExtra(MainActivity.TASK, taskFullInfo)
                holder.itemView.context.startActivity(intentInfo)
            }

            holder.itemView.checkBoxTaskContentDone.setOnClickListener {
                if (holder.itemView.checkBoxTaskContentDone.isChecked) {
                    taskFullInfo.task.done = 1
                    mainActivityViewModel.viewModelScope.launch {
                        mainActivityViewModel.changeTask(taskFullInfo.task)
                    }
                } else {
                    holder.itemView.checkBoxTaskContentDone.isChecked = true
                }
            }
        }
    }

    // Get object by VH
    fun getItemByViewHolder(viewHolder: RecyclerView.ViewHolder): ItemForList {
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
        notifyDataSetChanged()
    }


}