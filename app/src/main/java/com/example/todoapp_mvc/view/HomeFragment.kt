package com.example.todoapp_mvc.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp_mvc.R
import com.example.todoapp_mvc.adapters.CategoryAdapter
import com.example.todoapp_mvc.adapters.TaskAdapter
import com.example.todoapp_mvc.controller.Controller
import com.example.todoapp_mvc.databinding.CategoryItemBinding
import com.example.todoapp_mvc.databinding.EditDialogBinding
import com.example.todoapp_mvc.databinding.FragmentHomeBinding
import com.example.todoapp_mvc.local.database.AppDatabaseBuilder
import com.example.todoapp_mvc.local.database.DatabaseHelperImpl
import com.example.todoapp_mvc.local.entity.Category
import com.example.todoapp_mvc.local.entity.TaskData
import com.example.todoapp_mvc.utils.ViewmodelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: Controller
    lateinit var mContext: Context

    private var colorList =
        listOf(R.color.light_grey, R.color.green, R.color.red, R.color.yellow, R.color.purple)
    private val TAG = "HomeFragment"

    @SuppressLint("NotifyDataSetChanged", "ResourceType", "UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        setupViewModel()
        try {
            loadUIData()
            workUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun workUI() {
        try {
            var dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.popup)
            var isclick = false

            binding.fab.setOnClickListener {
                isclick = !isclick
                if (isclick) {
                    binding.dilalog.visibility = View.VISIBLE
                    binding.innerContainer.alpha = 0.2f
                    binding.fab.rotation = 45f
                } else {
                    binding.dilalog.visibility = View.GONE
                    binding.innerContainer.alpha = 1f
                    binding.fab.rotation = 0f
                }
            }
            binding.addTask.setOnClickListener {
                findNavController().navigate(R.id.addTaskFragment)
            }
            binding.option.setOnClickListener {
                binding.option.setImageResource(R.drawable.ic__delete)
            }
            binding.addCategory.setOnClickListener {
                val dialog = Dialog(mContext)
                var color = MutableLiveData<Int>()
                color.value = R.color.black
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                var view = EditDialogBinding.inflate(
                    LayoutInflater.from(mContext),
                    null,
                    false
                )
                color.observe(viewLifecycleOwner) {
                    view.add.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext, it
                        )
                    )
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
                    view.add.setOnClickListener {
                        viewModel.addCategory(
                            Category(
                                view.edit.text.toString(),
                                color
                            )
                        )
                        dialog.dismiss()
                        binding.dilalog.visibility = View.GONE
                        binding.innerContainer.alpha = 1f
                        binding.fab.rotation = 0f
                        loadUIData()
                    }
                }
                dialog.setContentView(view.root)
                dialog.setCancelable(true)
                dialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private fun loadUIData() {
        var tasCount = ArrayList<Int>()
        var today = ArrayList<TaskData>()
        var categoryAdapter = CategoryAdapter()
        try {
            viewModel.getCategoryList().observe(viewLifecycleOwner) {
                it.map { categ ->
                    val getbb = viewModel.getbb(categ.category_id!!)
                    tasCount.add(getbb.size)
                }
                categoryAdapter.context = mContext
                categoryAdapter.tascount = tasCount
                categoryAdapter.list = it
                categoryAdapter.isTask = false
                categoryAdapter.onpress = object : CategoryAdapter.onPress {
                    override fun selected(
                        position: Int,
                        oldItem: Int,
                        list: ArrayList<CategoryItemBinding>,
                        itemview: CategoryItemBinding,
                        category: Category
                    ) {

                    }

                    override fun click(category: Category) {
                        Log.e(TAG, "click: ")
                        val bundle = Bundle()
                        bundle.putSerializable("data", category)

                        findNavController().navigate(R.id.toDoFragment, bundle)
                    }
                }
                binding.categoryRv.adapter = categoryAdapter
                categoryAdapter.notifyDataSetChanged()
            }
            var completeList = ArrayList<TaskData>()
            var unfulfilledList = ArrayList<TaskData>()
            var reversed = ArrayList<TaskData>()

            viewModel.getAllTaskList().observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    completeList.clear()
                    unfulfilledList.clear()
                    it.map { data ->
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
                    val df = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                    val date1 = df.format(Calendar.getInstance().time)
                    today.clear()
                    for (i in reversed) {
                        if (date1 == i.task_date) {
                            today.add(i)
                        }
                    }
                    val taskAdapter =
                        TaskAdapter(mContext, requireParentFragment(), today)
                    binding.option.setOnClickListener {
                        taskAdapter.delete()
                        categoryAdapter.notifyDataSetChanged()
                    }
                    binding.taskRv.adapter = taskAdapter
                    taskAdapter.notifyDataSetChanged()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewmodelFactory(DatabaseHelperImpl(AppDatabaseBuilder.getInstance(mContext)))
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}