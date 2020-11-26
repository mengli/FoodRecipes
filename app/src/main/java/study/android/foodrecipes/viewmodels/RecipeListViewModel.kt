package study.android.foodrecipes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import retrofit2.Response
import study.android.foodrecipes.models.Recipe
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

    fun searchRecipeApi(query: String, page: Int) {
        loadStatus.postValue(LoadStatus.LOADING)
        val searchResponseFuture = recipeRepository.searchRecipeApi(query, page)
        Futures.addCallback(
            searchResponseFuture,
            object : FutureCallback<Response<RecipeSearchResponse>> {
                override fun onSuccess(result: Response<RecipeSearchResponse>?) {
                    Log.d(TAG, "Receive response.")
                    when (result?.code()) {
                        200 -> {
                            if (result.body()?.recipes.isNullOrEmpty()) {
                                loadStatus.postValue(LoadStatus.END)
                            } else {
                                loadStatus.postValue(LoadStatus.DONE)
                                if (page == 1) {
                                    recipes.postValue(result.body()?.recipes)
                                } else {
                                    val currentRecipeList = recipes.value?.toMutableList()
                                    result.body()?.recipes?.let { currentRecipeList?.addAll(it) }
                                    recipes.postValue(currentRecipeList)
                                }
                            }
                        }
                        else -> {
                            Log.e(TAG, "Network error: ${result?.errorBody()}")
                            loadStatus.postValue(LoadStatus.ERROR)
                        }
                    }
                }

                override fun onFailure(t: Throwable) {
                    Log.d(TAG, "Fail $t")
                    loadStatus.postValue(LoadStatus.TIME_OUT)
                }
            },
            MoreExecutors.directExecutor()
        )
    }
}