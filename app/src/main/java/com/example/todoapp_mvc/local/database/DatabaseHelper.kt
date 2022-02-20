package com.example.todoapp_mvc.local.database

import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    fun addCategory(category: Category)

    fun deleteCategory(category: Category)

    fun updateCategory(category: Category)

    suspend fun getCategoryList(): List<Category>

    fun addTask(taskData: TaskData)

    fun deleteTask(taskData: TaskData)

    fun updateTask(taskData: TaskData)

    suspend fun getAllTaskList(): List<TaskData>

    fun getTaskByCategoryId(): List<TaskData>

    fun gettask(id: Int): Flow<List<TaskData>>

    fun getbb(id: Int): List<TaskData>
}