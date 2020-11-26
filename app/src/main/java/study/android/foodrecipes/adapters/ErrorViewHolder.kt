package study.android.foodrecipes.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import study.android.foodrecipes.R

class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val errorMessage: TextView = itemView.findViewById(R.id.error_message)
}