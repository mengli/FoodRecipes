package study.android.foodrecipes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import study.android.foodrecipes.R
import study.android.foodrecipes.models.Recipe
import study.android.foodrecipes.viewmodels.RecipeListViewModel.LoadStatus
import java.util.LinkedList
import kotlin.math.roundToInt

class RecipeRecyclerAdapter(private val onRecipeClickListener: OnRecipeClickListener) :
    Adapter<ViewHolder>() {

    companion object {
        private const val TAG = "RecipeRecyclerAdapter"
    }

    enum class RecipeViewType {
        RECIPE_TYPE,
        LOADING_TYPE,
        ERROR
    }

    private var loadStatus: LoadStatus = LoadStatus.LOADING
    private var recipes: List<Recipe> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        return when (viewType) {
            RecipeViewType.RECIPE_TYPE.ordinal -> {
                val recipeView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_recipe_list_item, parent, false)
                RecipeViewHolder(recipeView, onRecipeClickListener)
            }
            RecipeViewType.ERROR.ordinal -> {
                val errorView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_error_list_item, parent, false)
                ErrorViewHolder(errorView)
            }
            else -> {
                val loadingView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_loading_list_item, parent, false)
                LoadingViewHolder(loadingView)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder")
        when (getItemViewType(position)) {
            RecipeViewType.RECIPE_TYPE.ordinal -> {
                val recipeViewHolder = holder as RecipeViewHolder
                recipeViewHolder.title.text = recipes[position].title
                recipeViewHolder.publisher.text = recipes[position].publisher
                recipeViewHolder.socialScore.text =
                    recipes[position].social_rank.roundToInt().toString()
                val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                Glide.with(recipeViewHolder.itemView.context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipes[position].image_url).into(recipeViewHolder.image)
            }
            RecipeViewType.ERROR.ordinal -> {
                val errorViewHolder = holder as ErrorViewHolder
                errorViewHolder.errorMessage.text = when (loadStatus) {
                    LoadStatus.TIME_OUT ->
                        holder.itemView.context.resources.getString(
                            R.string.error_message_time_out)
                    LoadStatus.END ->
                        holder.itemView.context.resources.getString(
                            R.string.error_message_end_of_list)
                    else -> holder.itemView.context.resources.getString(R.string.error_message)
                }
            }
        }
    }

    fun showLoadingBar() {
        loadStatus = LoadStatus.LOADING
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount")
        return when (loadStatus) {
            LoadStatus.LOADING,
            LoadStatus.TIME_OUT,
            LoadStatus.ERROR,
            LoadStatus.END -> recipes.size + 1
            else -> recipes.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (loadStatus == LoadStatus.LOADING
            && position == recipes.size
        ) {
            RecipeViewType.LOADING_TYPE.ordinal
        } else if ((loadStatus == LoadStatus.TIME_OUT
                    ||loadStatus == LoadStatus.ERROR
                    || loadStatus == LoadStatus.END)
            && position == recipes.size
        ) {
            RecipeViewType.ERROR.ordinal
        } else {
            RecipeViewType.RECIPE_TYPE.ordinal
        }
    }

    fun getRecipes(position: Int): Recipe {
        Log.i(TAG, "getRecipe")
        return recipes[position]
    }

    fun setRecipes(recipes: List<Recipe>) {
        Log.i(TAG, "setRecipes")
        this.recipes = recipes
        notifyDataSetChanged()
    }

    fun setLoadStatus(loadStatus: LoadStatus) {
        Log.i(TAG, "setLoadStatus")
        this.loadStatus = loadStatus
        notifyDataSetChanged()
    }
}