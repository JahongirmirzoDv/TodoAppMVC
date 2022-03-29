package com.example.todoapp_mvc.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp_mvc.R
import com.example.todoapp_mvc.controller.AlarmController
import com.example.todoapp_mvc.controller.Controller
import com.example.todoapp_mvc.databinding.TaskItemBinding
import com.example.todoapp_mvc.local.database.AppDatabaseBuilder
import com.example.todoapp_mvc.local.database.DatabaseHelperImpl
import com.example.todoapp_mvc.local.entity.TaskData
import com.example.todoapp_mvc.utils.ViewmodelFactory

class TaskAdapter(
    var context: Context,
    var viewowner: Fragment,
    val list: ArrayList<TaskData>,
    var isTodo: Boolean = false
) :
    RecyclerView.Adapter<TaskAdapter.Vh>() {
    var itemViewList = ArrayList<TaskData>()
    var selected = ArrayList<TaskData>()
    lateinit var viewModel: Controller
    var view: ImageView? = null
    lateinit var alarmController: AlarmController

    inner class Vh(var itemview: TaskItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(taskData: TaskData) {
            setupViewModel()
            alarmController = AlarmController(context)


            itemview.taskName.text = taskData.task_name
            itemview.categoryColor.setBackgroundColor(
                context.resources.getColor(taskData.category_color!!)
            )
            if (isTodo) {
                itemview.cat.visibility = View.INVISIBLE
                itemview.select.visibility = View.GONE
            }
            selected.clear()
            if (taskData.task_complete == true) {
                itemview.select.setImageResource(R.drawable.ic_marked)
                selected.add(taskData)
            } else {
                itemview.select.setImageResource(R.drawable.ic_unmarked)
            }
            itemview.select.setOnClickListener {
                var isclick = true
                itemViewList.add(taskData)
                if (isTodo) {
                    itemview.cat.visibility = View.VISIBLE
                    itemview.cat.setOnClickListener {
                        delete()
                    }
                }
                if (isclick) {
                    itemview.select.setImageResource(R.drawable.ic_marked)
                    selected.add(taskData)
                    isclick = false
                    itemview.select.isClickable = false
                    alarmController.disableAlarm(
                        "${
                            taskData.task_time!!.substring(
                                0,
                                2
                            )
                        }${taskData.task_time!!.substring(3)}".toInt()
                    )
                    viewModel.updateTask(
                        TaskData(
                            taskData.id,
                            taskData.task_name,
                            taskData.task_date,
                            taskData.task_time,
                            true,
                            taskData.category_id,
                            taskData.category,
                            taskData.category_color
                        )
                    )
                } else {
                    itemview.select.setImageResource(R.drawable.ic_unmarked)
                    selected.remove(taskData)
                    isclick = true
                }
            }
            itemview.alarm.visibility =
                if (taskData.task_time!!.isEmpty()) View.INVISIBLE else View.VISIBLE
            itemview.calendar.visibility =
                if (taskData.task_date!!.isEmpty()) View.INVISIBLE else View.VISIBLE
            itemview.alarmTime.text =
                if (taskData.task_time!!.isNotEmpty()) taskData.task_time else ""
            itemview.calendarDate.text =
                if (taskData.task_date!!.isNotEmpty()) taskData.task_date else ""
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            viewowner,
            ViewmodelFactory(DatabaseHelperImpl(AppDatabaseBuilder.getInstance(context)))
        )[Controller::class.java]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        var vh = Vh(TaskItemBinding.inflate((LayoutInflater.from(parent.context)), parent, false))
        return vh
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun delete() {
        if (selected.size != 0) {
            for (i in selected) {
                viewModel.deleteTask(i)
                list.remove(i)
                notifyDataSetChanged()
            }
        }
    }
}