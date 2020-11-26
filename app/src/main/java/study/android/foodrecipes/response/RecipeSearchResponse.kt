package study.android.foodrecipes.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import study.android.foodrecipes.models.Recipe

class RecipeSearchResponse {

    @SerializedName("count")
    @Expose()
    var count: Int = 0

    @SerializedName("recipes")
    @Expose()
    lateinit var recipes: List<Recipe>

    override fun toString(): String {
        return "RecipeSearchResponse(count=$count, recipes=$recipes)"
    }
}