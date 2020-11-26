package study.android.foodrecipes.requests

import android.util.Log
import com.google.common.util.concurrent.Futures.withTimeout
import com.google.common.util.concurrent.ListenableFuture
import retrofit2.Call
import retrofit2.Response
import study.android.foodrecipes.response.RecipeSearchResponse
import study.android.foodrecipes.utils.AppExecutors
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

object RecipeApiClient {

    private const val TAG = "RecipeApiClient"

    private val appExecutors = AppExecutors

    fun searchRecipeApi(
        query: String,
        page: Int
    ): ListenableFuture<Response<RecipeSearchResponse>> {
        Log.d(TAG, "searchRecipeApi")
        val retrieveRecipeSearchCallable = RetrieveRecipeSearchCallable(query, page)
        return withTimeout(
            appExecutors.networkIo.submit(retrieveRecipeSearchCallable),
            5000,
            TimeUnit.MILLISECONDS,
            appExecutors.scheduledExecutorService
        )
    }

    private class RetrieveRecipeSearchCallable(private val query: String, private val page: Int) :
        Callable<Response<RecipeSearchResponse>> {

        private lateinit var responseCall: Call<RecipeSearchResponse>

        override fun call(): Response<RecipeSearchResponse> {
            responseCall = ServiceGenerator.getRecipeApi().searchRecipe(query, page)
            return responseCall.execute()
        }
    }
}