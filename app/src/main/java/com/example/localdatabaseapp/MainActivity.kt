package com.example.localdatabaseapp

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localdatabaseapp.databinding.ActivityMainBinding
import com.example.localdatabaseapp.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter { task -> viewModel.updateTask(task.copy(isDone = !task.isDone)) }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Наблюдаем за изменениями
        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitFullList(tasks)
        }

        // Добавление задачи
        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val category = binding.etCategory.text.toString()
            if (title.isNotEmpty()) {
                viewModel.addTask(title, category)
                binding.etTitle.text.clear()
                binding.etCategory.text.clear()
            }
        }

        // Поиск
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return true
            }
        })

        // Статистика
        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitFullList(tasks)

            // 🔹 Обновляем статистику
            val doneCount = tasks.count { it.isDone }
            val totalCount = tasks.size
            binding.tvStats.text = "Выполнено: $doneCount / $totalCount"
        }
    }
}