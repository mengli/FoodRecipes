package study.android.foodrecipes.adapters

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import study.android.foodrecipes.R

class RecipeViewHolder(itemView: View, private val onRecipeClickListener: OnRecipeClickListener) :
    ViewHolder(itemView) {

    val title: TextView = itemView.findViewById(R.id.recipe_title)
    val publisher: TextView = itemView.findViewById(R.id.recipe_publisher)
    val socialScore: TextView = itemView.findViewById(R.id.recipe_social_score)
    val image: AppCompatImageView = itemView.findViewById(R.id.recipe_image)

    init {
        itemView.setOnClickListener { onRecipeClickListener.onRecipeClick(adapterPosition) }
    }
}