package alex.ts.coronavirusapp.model

import com.google.gson.annotations.SerializedName

data class China(
    @SerializedName("totalConfirmed")
    val totalConfirmed: Int,

    @SerializedName("totalDeaths")
    val totalDeaths: Int,

    @SerializedName("totalRecovered")
    val totalRecovered: Int,

    @SerializedName("data")
    val data: List<Datum>
)