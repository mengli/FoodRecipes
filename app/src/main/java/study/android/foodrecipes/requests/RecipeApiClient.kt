package study.android.foodrecipes.requests

import android.util.Log
import retrofit2.Response
import study.android.foodrecipes.response.RecipeSearchResponse

object RecipeApiClient {

    private const val TAG = "RecipeApiClient"

    suspend fun searchRecipeApi(
        query: String,
        page: Int
    ): Response<RecipeSearchResponse> {
        Log.d(TAG, "searchRecipeApi")
        return ServiceGenerator.getRecipeApi().searchRecipe(query, page)
    }
}