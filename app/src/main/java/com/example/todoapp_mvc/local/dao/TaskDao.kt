package com.example.todoapp_mvc.local.dao

import androidx.room.*
import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun addCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("select * from category")
    suspend fun getCategoryList(): List<Category>

    @Insert
    fun addTask(taskData: TaskData)

    @Delete
    fun deleteTask(taskData: TaskData)

    @Update
    fun updateTask(taskData: TaskData)

    @Query("select * from taskdata")
    suspend fun getAllTaskList(): List<TaskData>

    @Query("select * from taskdata")
    fun getTaskByCategoryId(): List<TaskData>

    @Query("select * from taskdata where category_id=:id")
    fun getbb(id: Int): List<TaskData>

    @Query("select * from taskdata  where category_id=:id")
    fun gettask(id: Int): Flow<List<TaskData>>
}