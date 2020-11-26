package study.android.foodrecipes.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import study.android.foodrecipes.models.Recipe

class RecipeResponse {

    @SerializedName("recipe")
    @Expose()
    lateinit var recipe: Recipe

    override fun toString(): String {
        return "RecipeResponse(recipe=$recipe)"
    }
}