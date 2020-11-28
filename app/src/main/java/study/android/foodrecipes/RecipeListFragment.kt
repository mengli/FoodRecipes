package study.android.foodrecipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import study.android.foodrecipes.adapters.OnRecipeClickListener
import study.android.foodrecipes.adapters.RecipeRecyclerAdapter
import study.android.foodrecipes.models.Recipe
import study.android.foodrecipes.utils.VerticalSpacingItemDecorator
import study.android.foodrecipes.viewmodels.RecipeListViewModel

class RecipeListFragment : Fragment(), OnRecipeClickListener {

    companion object {
        private const val TAG = "RecipeListFragment"
    }

    private val recipeListViewModel: RecipeListViewModel by lazy {
        ViewModelProvider(this).get(
            RecipeListViewModel::class.java
        )
    }
    private val recipeRecyclerAdapter: RecipeRecyclerAdapter by lazy {
        RecipeRecyclerAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: RecipeListFragmentArgs by navArgs()
        val fragmentView = inflater.inflate(R.layout.recipe_list_fragment, container, false)
        val recipeList = fragmentView.findViewById(R.id.recipe_list) as RecyclerView
        recipeList.adapter = recipeRecyclerAdapter
        recipeList.addItemDecoration(VerticalSpacingItemDecorator(30))
        recipeList.layoutManager = LinearLayoutManager(context)
        recipeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_IDLE
                    && !recipeList.canScrollVertically(/* scroll down */ 1)
                    && recipeListViewModel.getLoadStatus().value
                    != RecipeListViewModel.LoadStatus.END
                ) {
                    recipeRecyclerAdapter.showLoadingBar()
                    recipeList.smoothScrollToPosition(recipeRecyclerAdapter.itemCount - 1)
                    recipeListViewModel.nextPage += 1
                    GlobalScope.launch(Dispatchers.IO) {
                        recipeListViewModel.searchRecipeApi(
                            safeArgs.query, recipeListViewModel.nextPage
                        )
                    }
                }
            }
        })
        observeLoadErrorStatus()
        observeRecipeList()

        if (savedInstanceState == null) {
            searchRecipeApi(safeArgs.query, recipeListViewModel.nextPage)
        }

        return fragmentView
    }

    override fun onRecipeClick(position: Int) {
        val action =
            RecipeListFragmentDirections.nextAction(
                recipeRecyclerAdapter.getRecipes(position).recipe_id!!)
        findNavController().navigate(action)
    }

    private fun observeRecipeList() {
        recipeListViewModel.getRecipes().observe(viewLifecycleOwner,
            Observer<List<Recipe>> { recipes ->
                run {
                    Log.d(TAG, "Update recipe list.")
                    recipeRecyclerAdapter.setRecipes(recipes)
                }
            })
    }

    private fun observeLoadErrorStatus() {
        recipeListViewModel.getLoadStatus().observe(viewLifecycleOwner,
            Observer<RecipeListViewModel.LoadStatus> { loadErrorStatus ->
                run {
                    Log.d(TAG, "Update load error status.")
                    recipeRecyclerAdapter.setLoadStatus(loadErrorStatus)
                }
            })
    }

    private fun searchRecipeApi(query: String, page: Int) {
        recipeRecyclerAdapter.showLoadingBar()
        GlobalScope.launch(Dispatchers.IO) {
            recipeListViewModel.searchRecipeApi(query, page)
        }
    }
}