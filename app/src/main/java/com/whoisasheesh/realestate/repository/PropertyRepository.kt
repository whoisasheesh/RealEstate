package com.whoisasheesh.realestate.repository

import com.whoisasheesh.realestate.api.PropertyApi
import com.whoisasheesh.realestate.data.Property
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PropertyRepository {
    val propertySet = ArrayList<Property>()

    fun fetchProperty(): Observable<ArrayList<Property>> {
        return Observable.create { emitter ->
            PropertyApi.apiService.propertiesList.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    try {
                        val res = response.body()?.string() ?: ""
                        parseResponse(res)
                        emitter.onNext(propertySet)
                        emitter.onComplete()
                    } catch (exception: Exception) {
                        emitter.onError(exception)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    emitter.onError(t)
                }
            })
        }.subscribeOn(Schedulers.io())
    }

    private fun parseResponse(response: String) {
        val resObj = JSONObject(response)
        val resArray = resObj.getJSONArray("data")
        propertySet.clear()
        for (i in 0 until resArray.length()) {
            val eachObj = resArray.getJSONObject(i)
            val property = parseProperty(eachObj)
            propertySet.add(property)
        }
    }

    private fun parseProperty(eachObj: JSONObject): Property {
        val agentInfo = eachObj.getJSONObject("agent")
        val agentFirstName = agentInfo.getString("first_name")
        val agentLastName = agentInfo.getString("last_name")
        val agentImg = agentInfo.getJSONObject("avatar").getJSONObject("small").getString("url")

        var img = ""
        val propertyImages = eachObj.getJSONArray("property_images")
        for (j in 0 until propertyImages.length()) {
            val propertyImg = propertyImages.getJSONObject(j)
            img = propertyImg.getJSONObject("attachment").getJSONObject("medium").getString("url")
        }

        val ownerInfo = eachObj.getJSONObject("owner")
        val ownerFirstName = ownerInfo.getString("first_name")
        val ownerLastName = ownerInfo.getString("last_name")
        val ownerImg = ownerInfo.getJSONObject("avatar").getJSONObject("small").getString("url")

        val location = eachObj.getJSONObject("location")
        val address = location.getString("address")
        val state = location.getString("state")
        val suburb = location.getString("suburb")
        val postCode = location.optString("postcode")

        return Property(
            eachObj.getString("id"),
            eachObj.getString("bedrooms"),
            eachObj.getString("bathrooms"),
            eachObj.getString("carspaces"),
            eachObj.getString("display_price"),
            eachObj.getString("currency"),
            eachObj.getString("property_type"),
            eachObj.getString("sale_type"),
            "$agentFirstName $agentLastName",
            "$ownerFirstName $ownerLastName",
            address, suburb, state, postCode, agentImg, img, ownerImg,
            eachObj.getString("description")
        )
    }

    companion object {
        val instance: PropertyRepository by lazy { PropertyRepository() }
    }
}