package com.example.mylibrary.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarouselItem (

  @SerializedName("vote_average"      ) var voteAverage      : Double?        = null,
  @SerializedName("vote_count"        ) var voteCount        : Int?           = null,
  @SerializedName("id"                ) var id               : Int?           = null,
  @SerializedName("video"             ) var video            : Boolean?       = null,
  @SerializedName("media_type"        ) var mediaType        : String?        = null,
  @SerializedName("title"             ) var title            : String?        = null,
  @SerializedName("popularity"        ) var popularity       : Double?        = null,
  @SerializedName("poster_path"       ) var posterPath       : String?        = null,
  @SerializedName("profile_path"       ) var profilePath       : String?        = null,
  @SerializedName("original_language" ) var originalLanguage : String?        = null,
  @SerializedName("original_title"    ) var originalTitle    : String?        = null,
  @SerializedName("name"              ) var name             : String?        = null,
  @SerializedName("original_name"    ) var originalName      : String?          = null,
  @SerializedName("genre_ids"         ) var genreIds         : ArrayList<Int> = arrayListOf(),
  @SerializedName("backdrop_path"     ) var backdropPath     : String?        = null,
  @SerializedName("adult"             ) var adult            : Boolean?       = null,
  @SerializedName("overview"          ) var overview         : String?        = null,
  @SerializedName("release_date"      ) var releaseDate      : String?        = null

) : Parcelable