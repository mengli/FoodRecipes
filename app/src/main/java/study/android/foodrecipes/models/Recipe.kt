package study.android.foodrecipes.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val title: String?,
    val publisher: String?,
    val publisher_url: String?,
    val ingredients: List<String>?,
    val recipe_id: String?,
    val image_url: String?,
    val social_rank: Float
) : Parcelable