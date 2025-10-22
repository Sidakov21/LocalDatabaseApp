package com.example.localdatabaseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.localdatabaseapp.data.Task
import com.example.localdatabaseapp.databinding.ItemTaskBinding

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    // 🔹 Храним все задачи отдельно
    private var allTasks: List<Task> = emptyList()

    inner class TaskViewHolder(private val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvCategory.text = task.category
            binding.cbDone.isChecked = task.isDone

            binding.cbDone.setOnCheckedChangeListener { _, _ -> onTaskClick(task) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 🔹 Обновляем список и сохраняем оригинальные данные
    fun submitFullList(tasks: List<Task>) {
        allTasks = tasks
        submitList(tasks)
    }

    // 🔹 Фильтрация по названию
    fun filter(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            allTasks
        } else {
            allTasks.filter { it.title.contains(query, ignoreCase = true) }
        }
        submitList(filteredList)
    }
}
