package alex.ts.coronavirusapp.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("lastCheckTimeMilli")
    val lastCheckTimeMilli: Long,

    @SerializedName("china")
    val china: China
)