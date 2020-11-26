package study.android.foodrecipes.repositories

import retrofit2.Response
import study.android.foodrecipes.requests.RecipeApiClient
import study.android.foodrecipes.response.RecipeSearchResponse

object RecipeRepository {

    private val recipeApiClient: RecipeApiClient = RecipeApiClient

    suspend fun searchRecipeApi(
        query: String,
        page: Int
    ): Response<RecipeSearchResponse> {
        return recipeApiClient.searchRecipeApi(query, if (page <= 0) 1 else page)
    }
}