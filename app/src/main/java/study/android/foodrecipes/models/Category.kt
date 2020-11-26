package study.android.foodrecipes.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val title: String?,
    val image_url: String?
) : Parcelable