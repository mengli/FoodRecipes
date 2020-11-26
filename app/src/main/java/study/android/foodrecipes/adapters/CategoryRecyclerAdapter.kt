package study.android.foodrecipes.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import study.android.foodrecipes.R
import study.android.foodrecipes.models.Category
import study.android.foodrecipes.utils.DEFAULT_SEARCH_CATEGORIES
import study.android.foodrecipes.utils.DEFAULT_SEARCH_CATEGORY_IMAGES

class CategoryRecyclerAdapter(private val onCategoryClickListener: OnCategoryClickListener) :
    Adapter<ViewHolder>() {

    companion object {
        private const val TAG = "CategoryRecyclerAdapter"
    }

    private val categories: List<Category>

    init {
        var index = 0
        val categoryList = mutableSetOf<Category>()
        while (index < DEFAULT_SEARCH_CATEGORIES.size) {
            categoryList.add(
                Category(
                    DEFAULT_SEARCH_CATEGORIES[index],
                    DEFAULT_SEARCH_CATEGORY_IMAGES[index]
                )
            )
            index++
        }
        categories = categoryList.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val categoryView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_category_list_item, parent, false)
        return CategoryViewHolder(categoryView, onCategoryClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder")
        val categoryViewHolder = holder as CategoryViewHolder
        categoryViewHolder.title.text = categories[position].title
        val requestOptions: RequestOptions = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
        val imageUrl: Uri =
            Uri.parse("android.resource://study.android.foodrecipes/drawable/${categories[position].image_url}")
        Glide.with(categoryViewHolder.itemView.context)
            .setDefaultRequestOptions(requestOptions)
            .load(imageUrl).into(categoryViewHolder.image)
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount")
        return categories.size
    }
}