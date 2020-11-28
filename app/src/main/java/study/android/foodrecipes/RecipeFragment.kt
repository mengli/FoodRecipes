package study.android.foodrecipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import study.android.foodrecipes.models.Recipe
import study.android.foodrecipes.viewmodels.RecipeViewModel
import kotlin.math.roundToInt

class RecipeFragment : Fragment() {

    companion object {
        private const val TAG = "RecipeFragment"
    }

    private val recipeViewModel: RecipeViewModel by lazy {
        ViewModelProvider(this).get(
            RecipeViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: RecipeFragmentArgs by navArgs()
        val fragmentView = inflater.inflate(R.layout.recipe_fragment, container, false)

        observeRecipe()
        observeLoadingStatus()

        if (savedInstanceState == null) {
            getRecipeApi(safeArgs.rid)
        }

        return fragmentView
    }

    private fun observeRecipe() {
        recipeViewModel.getRecipe().observe(
            viewLifecycleOwner,
            Observer<Recipe> { recipe -> run { updateRecipe(recipe) } })
    }

    private fun observeLoadingStatus() {
        recipeViewModel.loadStatus.observe(
            viewLifecycleOwner,
            Observer<RecipeViewModel.LoadStatus> {
                    loadStatus -> run { updateLoadingStatus(loadStatus) } })
    }

    private fun updateLoadingStatus(loadStatus: RecipeViewModel.LoadStatus?) {
        view?.findViewById<View>(R.id.recipe_loading_progress_bar)?.visibility =
            if (loadStatus == RecipeViewModel.LoadStatus.LOADING) View.VISIBLE else View.GONE
        view?.findViewById<View>(R.id.recipe_content)?.visibility =
            if (loadStatus == RecipeViewModel.LoadStatus.LOADING) View.GONE else View.VISIBLE
    }

    private fun updateRecipe(recipe: Recipe?) {
        hideLoadingBar()
        if (recipe != null) {
            view?.let {
                val requestOptions: RequestOptions = RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background)
                Glide.with(requireContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.image_url).into(it.findViewById(R.id.recipe_detail_image))
                it.findViewById<TextView>(R.id.recipe_detail_title).text = recipe.title
                it.findViewById<TextView>(R.id.recipe_detail_social_score).text =
                    recipe.social_rank.roundToInt().toString()
                val ingredientsContainer =
                    it.findViewById<LinearLayout>(R.id.ingredients_detail_container)
                ingredientsContainer.removeAllViews()
                if (recipe.ingredients != null) {
                    for (ingredient in recipe.ingredients) {
                        val textView = TextView(requireContext())
                        textView.text = ingredient
                        textView.textSize = 15f
                        textView.layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        ingredientsContainer.addView(textView)
                    }
                }
            }
        }
    }

    private fun getRecipeApi(rid: String) {
        showLoadingBar()
        GlobalScope.launch(Dispatchers.IO) {
            recipeViewModel.getRecipeApi(rid)
        }
    }

    private fun showLoadingBar() {
        Log.i(TAG, "showLoadingBar")
        recipeViewModel.loadStatus.value = RecipeViewModel.LoadStatus.LOADING
    }

    private fun hideLoadingBar() {
        Log.i(TAG, "hideLoadingBar")
        recipeViewModel.loadStatus.value = RecipeViewModel.LoadStatus.DONE
    }
}