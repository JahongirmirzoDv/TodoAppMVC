package com.example.todoapp_mvc.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp_mvc.R
import com.example.todoapp_mvc.adapters.TaskAdapter
import com.example.todoapp_mvc.controller.Controller
import com.example.todoapp_mvc.databinding.EditDialogBinding
import com.example.todoapp_mvc.databinding.FragmentToDoBinding
import com.example.todoapp_mvc.local.database.AppDatabaseBuilder
import com.example.todoapp_mvc.local.database.DatabaseHelperImpl
import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import com.example.todoapp_mvc.utils.ViewmodelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ToDoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ToDoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        category = requireArguments().getSerializable("data") as Category
    }

    lateinit var binding: FragmentToDoBinding
    lateinit var viewModel: Controller

    lateinit var list: List<TaskData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentToDoBinding.inflate(inflater, container, false)
        setupViewModel()
        try {
            workUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private fun workUI() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                binding.categoryName.text = category.category_name
                binding.container.setBackgroundColor(
                    requireContext().resources.getColor(category.category_color!!)
                )
                loadCategoryTasks(category.category_id!!)
                var completeList = ArrayList<TaskData>()
                var unfulfilledList = ArrayList<TaskData>()
                var reversed = ArrayList<TaskData>()

                var ll = viewModel.getbb(category.category_id!!)
                    if (ll.isNotEmpty()) {
                        list = ll
                        completeList.clear()
                        unfulfilledList.clear()
                        ll.map { data ->
                            if (data.task_complete == true) {
                                completeList.add(data)
                            } else {
                                unfulfilledList.add(data)
                            }
                        }
                        reversed.clear()
                        val s = unfulfilledList.reversed()
                        reversed.addAll(s)
                        reversed.addAll(if (completeList.isNotEmpty()) completeList.reversed() else emptyList())
                        val taskAdapter =
                            TaskAdapter(requireContext(), requireParentFragment(), reversed, true)
                        binding.edit.setOnClickListener {
                            edit(category)
                            taskAdapter.notifyDataSetChanged()
                        }
                        binding.todoRv.adapter = taskAdapter
                        taskAdapter.notifyDataSetChanged()
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ResourceAsColor")
    private fun edit(category: Category) {
        try {
            val dialog = Dialog(requireContext())
            var color = MutableLiveData<Int>()
            color.value = category.category_color
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            var view = EditDialogBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
            view.edit.setText(category.category_name)
            if (category.category_color == R.color.yellow || category.category_color == R.color.light_grey) {
                view.add.setTextColor(R.color.grey)
            }
            try {
                color.observe(viewLifecycleOwner) {
                    view.add.setBackgroundColor(
                        requireContext().resources.getColor(it)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val childcount: Int = view.viewgroup.childCount
            for (i in 0 until childcount) {
                view.viewgroup.getChildAt(i).setOnClickListener {
                    when (it.id) {
                        R.id.black -> {
                            color.value = R.color.black
//                        view.add.setBackgroundColor(R.color.black)
                        }
                        R.id.blue -> {
                            color.value = R.color.blue
//                        view.add.setBackgroundColor(R.color.blue)
                        }
                        R.id.purple -> {
                            color.value = R.color.purple
//                        view.add.setBackgroundColor(R.color.purple)
                        }
                        R.id.yellow -> {
                            color.value = R.color.yellow
//                        view.add.setBackgroundColor(R.color.yellow)
                        }
                        R.id.red -> {
                            color.value = R.color.red
//                        view.add.setBackgroundColor(R.color.red)
                        }
                        R.id.green -> {
                            color.value = R.color.green
//                        view.add.setBackgroundColor(R.color.green)
                        }
                        R.id.grey -> {
                            color.value = R.color.light_grey
//                        view.add.setBackgroundColor(R.color.light_grey)
                        }
                    }
                }
            }
            color.observe(viewLifecycleOwner) { color ->
                try {
                    view.add.setOnClickListener {
                        viewModel.updateCategory(
                            Category(
                                category.category_id,
                                view.edit.text.toString(),
                                color
                            )
                        )

                        try {
                            var mm = viewModel.getbb(category.category_id!!)
                            mm.map { i ->
                                viewModel.updateTask(
                                    TaskData(
                                        i.id,
                                        i.task_name,
                                        i.task_date,
                                        i.task_time,
                                        i.task_complete,
                                        i.category_id,
                                        i.category,
                                        color
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        binding.container.setBackgroundColor(
                            requireContext().resources.getColor(color)
                        )
                        binding.categoryName.text = view.edit.text.toString()
                        dialog.cancel()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            dialog.setContentView(view.root)
            dialog.setCancelable(true)
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadCategoryTasks(categoryId: Int) {
        val taskByCategoryId = viewModel.getbb(categoryId)
        var a = ""
        a = taskByCategoryId.size.toString()
        binding.categoryTask.text = "$a task"
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewmodelFactory(
                DatabaseHelperImpl(AppDatabaseBuilder.getInstance(requireContext()))
            )
        )[Controller::class.java]
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ToDoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}