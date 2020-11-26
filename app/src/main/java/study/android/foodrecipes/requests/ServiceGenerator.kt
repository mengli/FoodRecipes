package study.android.foodrecipes.requests

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import study.android.foodrecipes.utils.BASE_URL

class ServiceGenerator {

    companion object {
        private val retrofitBuilder =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
        private val retrofit = retrofitBuilder.build()
        private val recipeApi = retrofit.create(RecipeApi::class.java)
        
        fun getRecipeApi(): RecipeApi {
            return recipeApi
        }
    }
}