package study.android.foodrecipes.requests

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import study.android.foodrecipes.utils.BASE_URL
import java.util.concurrent.TimeUnit

class ServiceGenerator {

    companion object {

        var okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()

        private val retrofitBuilder =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
        private val retrofit = retrofitBuilder.build()
        private val recipeApi = retrofit.create(RecipeApi::class.java)

        fun getRecipeApi(): RecipeApi {
            return recipeApi
        }
    }
}