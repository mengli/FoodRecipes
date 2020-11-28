package study.android.foodrecipes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import study.android.foodrecipes.models.Recipe
import study.android.foodrecipes.models.ResultWrapper
import study.android.foodrecipes.repositories.RecipeRepository
import study.android.foodrecipes.response.RecipeResponse

class RecipeViewModel : ViewModel() {

    companion object {
        private const val TAG = "RecipeViewModel"
    }

    enum class LoadStatus {
        LOADING,
        DONE,
        ERROR,
        TIME_OUT
    }

    val loadStatus: MutableLiveData<LoadStatus> = MutableLiveData()
    private val recipeRepository = RecipeRepository
    private val recipe: MutableLiveData<Recipe> = MutableLiveData()

    suspend fun getRecipeApi(rid: String) {
        loadStatus.postValue(LoadStatus.LOADING)
        when(val resultWrapper = recipeRepository.getRecipeApi(rid)) {
            is ResultWrapper.Success<RecipeResponse> -> {
                loadStatus.postValue(LoadStatus.DONE)
                recipe.postValue(resultWrapper.value.recipe)
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

    fun getRecipe(): LiveData<Recipe> {
        return recipe
    }
}