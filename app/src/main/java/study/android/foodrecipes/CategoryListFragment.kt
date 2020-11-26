package study.android.foodrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import study.android.foodrecipes.adapters.CategoryRecyclerAdapter
import study.android.foodrecipes.adapters.OnCategoryClickListener
import study.android.foodrecipes.utils.VerticalSpacingItemDecorator

class CategoryListFragment : Fragment(), OnCategoryClickListener {

    private lateinit var categoryList: RecyclerView
    private lateinit var categoryRecyclerAdapter: CategoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.category_list_fragment, container, false)
        categoryList = fragmentView.findViewById(R.id.category_list)
        categoryRecyclerAdapter = CategoryRecyclerAdapter(this)
        categoryList.adapter = categoryRecyclerAdapter
        categoryList.addItemDecoration(VerticalSpacingItemDecorator(30))
        categoryList.layoutManager = LinearLayoutManager(context)
        return fragmentView
    }

    override fun onCategoryClick(category: String) {
        val action = CategoryListFragmentDirections.nextAction(category)
        findNavController().navigate(action)
    }
}