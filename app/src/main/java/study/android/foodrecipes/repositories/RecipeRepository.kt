package study.android.foodrecipes.repositories

import study.android.foodrecipes.models.ResultWrapper
import study.android.foodrecipes.requests.RecipeApiClient
import study.android.foodrecipes.response.RecipeResponse
import study.android.foodrecipes.response.RecipeSearchResponse

object RecipeRepository {

    private val recipeApiClient: RecipeApiClient = RecipeApiClient

    suspend fun searchRecipeApi(
        query: String,
        page: Int
    ): ResultWrapper<RecipeSearchResponse> {
        return recipeApiClient.searchRecipeApi(query, if (page <= 0) 1 else page)
    }

    suspend fun getRecipeApi(
        rid: String
    ): ResultWrapper<RecipeResponse> {
        return recipeApiClient.getRecipeApi(rid)
    }
}