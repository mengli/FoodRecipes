package study.android.foodrecipes.requests

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import study.android.foodrecipes.response.RecipeResponse
import study.android.foodrecipes.response.RecipeSearchResponse

interface RecipeApi {

    @GET("api/get")
    suspend fun getRecipe(@Query("rId") rId: String): Response<RecipeResponse>

    @GET("api/search")
    suspend fun searchRecipe(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Response<RecipeSearchResponse>
}