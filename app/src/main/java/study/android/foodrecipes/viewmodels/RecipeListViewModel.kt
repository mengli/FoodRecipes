package study.android.foodrecipes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import study.android.foodrecipes.models.Recipe
import study.android.foodrecipes.repositories.RecipeRepository

class RecipeListViewModel : ViewModel() {

    companion object {
        private const val TAG = "RecipeApiClient"
    }

    enum class LoadStatus {
        LOADING,
        DONE,
        END,
        ERROR
    }

    private val recipeRepository = RecipeRepository
    private val recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    private val loadStatus: MutableLiveData<LoadStatus> = MutableLiveData()
    var nextPage = 1

    fun getLoadStatus(): LiveData<LoadStatus> {
        return loadStatus
    }

    fun getRecipes(): LiveData<List<Recipe>> {
        return recipes
    }

    suspend fun searchRecipeApi(query: String, page: Int) {
        loadStatus.postValue(LoadStatus.LOADING)
        val searchResponse = recipeRepository.searchRecipeApi(query, page)
        if (searchResponse?.isSuccessful) {
            Log.d(TAG, "Receive response successfully.")
            if (searchResponse.body()?.recipes.isNullOrEmpty()) {
                loadStatus.postValue(LoadStatus.END)
            } else {
                loadStatus.postValue(LoadStatus.DONE)
                if (page == 1) {
                    recipes.postValue(searchResponse.body()?.recipes)
                } else {
                    val currentRecipeList = recipes.value?.toMutableList()
                    searchResponse.body()?.recipes?.let { currentRecipeList?.addAll(it) }
                    recipes.postValue(currentRecipeList)
                }
            }
        } else {
            Log.e(TAG, "Network error: ${searchResponse?.errorBody()}")
            loadStatus.postValue(LoadStatus.ERROR)
        }
    }
}