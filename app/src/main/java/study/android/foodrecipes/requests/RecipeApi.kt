package study.android.foodrecipes.requests

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import study.android.foodrecipes.response.RecipeResponse
import study.android.foodrecipes.response.RecipeSearchResponse

interface RecipeApi {

    @GET("api/get")
    fun getRecipe(@Query("rid") rId: String): Call<RecipeResponse>

    @GET("api/search")
    fun searchRecipe(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Call<RecipeSearchResponse>
}