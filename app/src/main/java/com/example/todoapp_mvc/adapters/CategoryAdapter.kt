package com.example.todoapp_mvc.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp_mvc.R
import com.example.todoapp_mvc.controller.Controller
import com.example.todoapp_mvc.databinding.CategoryItemBinding
import com.example.todoapp_mvc.local.entity.Category
import kotlinx.coroutines.DelicateCoroutinesApi


class CategoryAdapter(

) :
    RecyclerView.Adapter<CategoryAdapter.Vh>() {
    lateinit var context: Context
    lateinit var tascount: ArrayList<Int>
    lateinit var viewowner: Fragment
    lateinit var list: List<Category>
    var isTask: Boolean = false
    lateinit var onpress: onPress
    var oldItem = -1
    var itemViewList = ArrayList<CategoryItemBinding>()
    lateinit var viewModel: Controller

    constructor(
        context: Context,
        tascount: ArrayList<Int>,
        list: List<Category>,
        isTask: Boolean = false,
        onpress: onPress
    ) : this() {
        this.context = context
        this.tascount = tascount
        this.list = list
        this.isTask = isTask
        this.onpress = onpress
    }

    inner class Vh(var itemview: CategoryItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        @OptIn(DelicateCoroutinesApi::class)
        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged", "SetTextI18n")
        fun bind(category: Category, position: Int) {
//            setupViewModel()


            itemview.categoryTask.text = "${tascount[position]} task"


            if (category.category_color == R.color.yellow || category.category_color == R.color.light_grey) {
                itemview.categoryName.setTextColor(R.color.grey)
                itemview.categoryTask.setTextColor(R.color.grey)
            }
            itemview.categoryName.text = category.category_name
            itemview.ln.setBackgroundColor(
                context.resources.getColor(category.category_color!!)
            )
            itemview.container.setOnClickListener {
                if (isTask) {
                    if (oldItem < 0) {
                        itemview.categorySelect.visibility = View.VISIBLE
                    } else {
                        itemViewList[oldItem].categorySelect.visibility = View.INVISIBLE
                        itemViewList[position].categorySelect.visibility = View.VISIBLE
                    }
                    onpress.selected(position, oldItem, itemViewList, itemview, category)
                    oldItem = position
                } else {
                    onpress.click(category)
                }
            }
        }
    }

//    private fun setupViewModel() {
//        viewModel = ViewModelProvider(
//            viewowner,
//            ViewmodelFactory(DatabaseHelperImpl(AppDatabaseBuilder.getInstance(context)))
//        )[Controller::class.java]
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        var vh = Vh(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        itemViewList.add(vh.itemview)
        return vh
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(list[position], position)

    }

    override fun getItemCount(): Int = list.size

    interface onPress {
        fun selected(
            position: Int,
            oldItem: Int,
            list: ArrayList<CategoryItemBinding>,
            itemview: CategoryItemBinding,
            category: Category
        )

        fun click(category: Category)
    }

}