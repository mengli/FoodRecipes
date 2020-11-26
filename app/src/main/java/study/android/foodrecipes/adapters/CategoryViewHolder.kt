package study.android.foodrecipes.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import study.android.foodrecipes.R

class CategoryViewHolder(
    itemView: View,
    private val onCategoryClickListener: OnCategoryClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.findViewById(R.id.category_title)
    val image: CircleImageView = itemView.findViewById(R.id.category_image)

    init {
        itemView.setOnClickListener {
            onCategoryClickListener.onCategoryClick(title.text.toString())
        }
    }
}