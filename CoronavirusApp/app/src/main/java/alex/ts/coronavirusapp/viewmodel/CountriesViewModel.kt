package alex.ts.coronavirusapp.viewmodel

import alex.ts.coronavirusapp.const.URL
import alex.ts.coronavirusapp.model.Datum
import alex.ts.coronavirusapp.retrofit.JsonPlaceHolderApi
import alex.ts.coronavirusapp.utils.changeTime
import alex.ts.coronavirusapp.utils.changingNumbers
import alex.ts.coronavirusapp.utils.sortListForInfoFragment
import alex.ts.coronavirusapp.viewmodel.model.DataForInfoFragment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountriesViewModel : ViewModel() {
    private lateinit var jsonPlaceHolderApi: JsonPlaceHolderApi

    private val mutableDataForListInfo = MutableLiveData<DataForInfoFragment>()
    val dataForListInfo: LiveData<DataForInfoFragment> = mutableDataForListInfo

    private val mutableListCountriesForMap = MutableLiveData<List<Datum>>()
    val listCountriesForMap: LiveData<List<Datum>> = mutableListCountriesForMap

    init {
        getRetrofit()
        getPost()
    }

    private fun getRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
    }

    private fun getPost() {
        val service = jsonPlaceHolderApi
        GlobalScope.launch(Dispatchers.Main) {
            val postRequest = service.getPost() // Making Network call
            try {
                val post = postRequest.await()
                val china = post.china
                val updateTime = changeTime(post.lastCheckTimeMilli)
                mutableDataForListInfo.value = DataForInfoFragment(
                    changeTime(post.lastCheckTimeMilli),
                    changingNumbers(china.totalConfirmed),
                    changingNumbers(china.totalDeaths),
                    changingNumbers(china.totalRecovered),
                    sortListForInfoFragment(china.data as ArrayList<Datum>)
                )
                Log.d("Tga", "getPost")
                mutableListCountriesForMap.value = china.data
            }catch (e: Exception){

            }
        }
    }
}