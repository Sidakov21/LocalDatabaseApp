package com.example.localdatabaseapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.localdatabaseapp.data.Task
import com.example.localdatabaseapp.data.TaskDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    val allTasks = taskDao.getAllTasks()

    fun addTask(title: String, category: String) {
        viewModelScope.launch {
            val newTask = Task(title = title, category = category)
            taskDao.insert(newTask)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun search(query: String): LiveData<List<Task>> {
        return taskDao.searchTasks(query)
    }
}