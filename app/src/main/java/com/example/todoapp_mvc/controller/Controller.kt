package com.example.todoapp_mvc.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp_mvc.local.database.DatabaseHelper
import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Controller(private var db: DatabaseHelper) : ViewModel() {
    private val TAG = "Controller"
    private var categoryList = MutableLiveData<List<Category>>()
    private var taskList = MutableLiveData<List<TaskData>>()
    private var selected = MutableLiveData<ArrayList<TaskData>>()

    fun addCategory(category: Category) {
        db.addCategory(category)
    }

    fun deleteCategory(category: Category) {
        db.deleteCategory(category)
    }

    fun updateCategory(category: Category) {
        db.updateCategory(category)
    }

    fun getCategoryList(): MutableLiveData<List<Category>> {
        viewModelScope.launch {
            coroutineScope {
                val categoryList1 = db.getCategoryList()
                categoryList.value = categoryList1
            }
        }
        return categoryList
    }

    fun addTask(taskData: TaskData) {
        db.addTask(taskData)
    }

    fun deleteTask(taskData: TaskData) {
        db.deleteTask(taskData)
    }

    fun updateTask(taskData: TaskData) {
        db.updateTask(taskData)
    }

    fun getAllTaskList(): MutableLiveData<List<TaskData>> {
        viewModelScope.launch {
            coroutineScope {
                val allTaskList = db.getAllTaskList()
                taskList.value = allTaskList
            }
        }
        return taskList
    }

    fun getTaskByCategoryId(): List<TaskData> {
        return db.getTaskByCategoryId()
    }

    fun gettask(id: Int): Flow<List<TaskData>> {
        return db.gettask(id)
    }

    fun getbb(id: Int): List<TaskData> {
        return db.getbb(id)
    }
    fun addselect(){
//        selected.value =
    }
}