package com.example.todoapp_mvc.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TaskData {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var task_name: String? = null
    var task_date: String? = null
    var task_time: String? = null
    var task_complete: Boolean? = null
    var category_id: Int? = null
    var category: String? = null
    var category_color: Int? = null

    constructor()
    constructor(
        task_name: String?,
        task_date: String?,
        task_time: String?,
        task_complete: Boolean?,
        category_id: Int?,
        category: String?,
        category_color: Int?
    ) {
        this.task_name = task_name
        this.task_date = task_date
        this.task_time = task_time
        this.task_complete = task_complete
        this.category_id = category_id
        this.category = category
        this.category_color = category_color
    }

    constructor(
        id: Int?,
        task_name: String?,
        task_date: String?,
        task_time: String?,
        task_complete: Boolean?,
        category_id: Int?,
        category: String?,
        category_color: Int?
    ) {
        this.id = id
        this.task_name = task_name
        this.task_date = task_date
        this.task_time = task_time
        this.task_complete = task_complete
        this.category_id = category_id
        this.category = category
        this.category_color = category_color
    }


}