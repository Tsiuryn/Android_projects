package alex.ts.coronavirusapp.viewmodel.model

data class DataForInfoFragment(val lastUpdate: String,
val confirmed: String? ,
val death: String?,
val recovered: String?,
val listSortedCountries: List<Countries>?)