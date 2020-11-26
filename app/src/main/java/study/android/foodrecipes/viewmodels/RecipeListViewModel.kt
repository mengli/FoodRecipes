package study.android.foodrecipes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import study.android.foodrecipes.models.Recipe
import study.android.foodrecipes.models.ResultWrapper
import study.android.foodrecipes.repositories.RecipeRepository
import study.android.foodrecipes.response.RecipeSearchResponse

class RecipeListViewModel : ViewModel() {

    companion object {
        private const val TAG = "RecipeApiClient"
    }

    enum class LoadStatus {
        LOADING,
        DONE,
        END,
        ERROR,
        TIME_OUT
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
        when(val resultWrapper = recipeRepository.searchRecipeApi(query, page)) {
            is ResultWrapper.Success<RecipeSearchResponse> -> {
                if (resultWrapper.value.recipes.isNullOrEmpty()) {
                    loadStatus.postValue(LoadStatus.END)
                } else {
                    loadStatus.postValue(LoadStatus.DONE)
                    if (page == 1) {
                        recipes.postValue(resultWrapper.value.recipes)
                    } else {
                        val currentRecipeList = recipes.value?.toMutableList()
                        resultWrapper.value.recipes?.let { currentRecipeList?.addAll(it) }
                        recipes.postValue(currentRecipeList)
                    }
                }
            }
            is ResultWrapper.GenericError -> {
                Log.e(TAG, "HTTP error: ${resultWrapper.error}")
                loadStatus.postValue(LoadStatus.ERROR)
            }
            is ResultWrapper.NetworkError -> {
                Log.e(TAG, "Network error")
                loadStatus.postValue(LoadStatus.TIME_OUT)
            }
        }
    }
}