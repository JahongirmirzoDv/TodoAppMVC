package com.example.todoapp_mvc.local.database

import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import kotlinx.coroutines.flow.Flow

class DatabaseHelperImpl(var appDatabase: AppDatabase) : DatabaseHelper {
    override fun addCategory(category: Category) = appDatabase.taskDao().addCategory(category)

    override fun deleteCategory(category: Category) = appDatabase.taskDao().deleteCategory(category)

    override fun updateCategory(category: Category) = appDatabase.taskDao().updateCategory(category)

    override suspend fun getCategoryList(): List<Category> = appDatabase.taskDao().getCategoryList()

    override fun addTask(taskData: TaskData) = appDatabase.taskDao().addTask(taskData)

    override fun deleteTask(taskData: TaskData) = appDatabase.taskDao().deleteTask(taskData)

    override fun updateTask(taskData: TaskData) = appDatabase.taskDao().updateTask(taskData)

    override suspend fun getAllTaskList(): List<TaskData> = appDatabase.taskDao().getAllTaskList()

    override fun getTaskByCategoryId(): List<TaskData> =
        appDatabase.taskDao().getTaskByCategoryId()

    override fun gettask(id: Int): Flow<List<TaskData>> = appDatabase.taskDao().gettask(id)

    override fun getbb(id: Int): List<TaskData> = appDatabase.taskDao().getbb(id)
}