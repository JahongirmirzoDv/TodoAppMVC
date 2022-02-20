package com.example.todoapp_mvc.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp_mvc.R
import com.example.todoapp_mvc.adapters.CategoryAdapter
import com.example.todoapp_mvc.controller.Controller
import com.example.todoapp_mvc.databinding.CategoryItemBinding
import com.example.todoapp_mvc.databinding.FragmentAddTaskBinding
import com.example.todoapp_mvc.local.database.AppDatabaseBuilder
import com.example.todoapp_mvc.local.database.DatabaseHelperImpl
import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import com.example.todoapp_mvc.utils.ViewmodelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentAddTaskBinding
    lateinit var viewModel: Controller
    lateinit var mContext: Context
    private val TAG = "AddTaskFragment"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        setupViewModel()
        try {
            workUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun workUI() {
        var date = ""
        var time = ""
        var isclick = false
        var tasCount = ArrayList<Int>()
        var iscategory: Category? = null
        try {
            viewModel.getCategoryList().observe(viewLifecycleOwner) {
                it.map { categ ->
                    val getbb = viewModel.getbb(categ.category_id!!)
                    tasCount.add(getbb.size)
                }
                val categoryAdapter =
                    CategoryAdapter(
                        mContext,
                        tasCount,
                        requireParentFragment(),
                        it,
                        true,
                        object : CategoryAdapter.onPress {
                            override fun selected(
                                position: Int,
                                oldItem: Int,
                                list: ArrayList<CategoryItemBinding>,
                                itemview: CategoryItemBinding,
                                category: Category
                            ) {
                                binding.categoryColor.setBackgroundColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        category.category_color!!
                                    )
                                )
                                binding.categoryColor2.setBackgroundColor(
                                    ContextCompat.getColor(
                                        mContext,
                                        category.category_color!!
                                    )
                                )
                                binding.categoryName.text = category.category_name
                                iscategory = category
                            }

                            override fun click(category: Category) {

                            }
                        })
                binding.categoryRv.adapter = categoryAdapter
                categoryAdapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.select.setOnClickListener {
            isclick = !isclick
            if (isclick) {
                binding.select.setImageResource(R.drawable.ic_marked)
            } else {
                binding.select.setImageResource(R.drawable.ic_unmarked)
            }
        }
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }
        try {
            binding.calendar.setOnClickListener {
                val df = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                val date1 = df.format(Calendar.getInstance().time)
                val dateRangePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTheme(R.style.Theme_App)
                        .build()

                dateRangePicker.show(childFragmentManager, "")
                dateRangePicker.addOnPositiveButtonClickListener {
                    val timeZoneUTC = TimeZone.getDefault()
                    val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1

                    val simpleFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                    val dated = Date(it + offsetFromUTC)
                    date = if (date1 == simpleFormat.format(dated)) {
                        "Today"
                    } else simpleFormat.format(dated)
                    binding.calendar1.visibility = View.VISIBLE
                    binding.calendarDate.visibility = View.VISIBLE
                    binding.calendarDate.text = date
                }
            }
            binding.alarm.setOnClickListener {
                if (date.isNotEmpty()) {
                    val picker =
                        MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(12)
                            .setMinute(10)
                            .build()
                    picker.show(childFragmentManager, "tag")
                    picker.addOnPositiveButtonClickListener {
                        time = "${picker.hour}-${picker.minute}"
                        binding.alarm1.visibility = View.VISIBLE
                        binding.alarmTime.visibility = View.VISIBLE
                        binding.alarmTime.text = time
                    }
                } else Toast.makeText(mContext, "please add date", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.done.setOnClickListener {
                try {
                    if (time.isEmpty() && date.isEmpty()) {
                        Toast.makeText(
                            mContext,
                            "please select time or date",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (iscategory == null) {
                        Toast.makeText(
                            mContext,
                            "please select category",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        if (iscategory != null) {
                            var task = TaskData(
                                binding.taskName.text.toString(),
                                date,
                                time,
                                false,
                                iscategory!!.category_id,
                                iscategory!!.category_name,
                                iscategory!!.category_color
                            )
                            viewModel.addTask(task)
                            findNavController().popBackStack()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewmodelFactory(
                DatabaseHelperImpl(AppDatabaseBuilder.getInstance(mContext))
            )
        )[Controller::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}