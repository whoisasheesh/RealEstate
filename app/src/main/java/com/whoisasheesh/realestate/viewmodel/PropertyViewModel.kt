package com.whoisasheesh.realestate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whoisasheesh.realestate.data.Property
import com.whoisasheesh.realestate.repository.PropertyRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class PropertyViewModel : ViewModel() {
    private var mPropertyLists: MutableLiveData<ArrayList<Property>>? = MutableLiveData()
    private val isLoading = MutableLiveData<Boolean>()
    private var cachedProperties: ArrayList<Property>? = null
    var propertyRepository: PropertyRepository? = PropertyRepository.instance

    val properties: Unit
        get() {
            if (cachedProperties.isNullOrEmpty()) {
                fetchDataFromAPI()
            } else {
                mPropertyLists!!.value = cachedProperties
            }
        }

    fun fetchDataFromAPI() {
        isLoading.value = true
        propertyRepository?.fetchProperty()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ propertyArrayList ->
                isLoading.value = false
                if (!propertyArrayList.isNullOrEmpty()) {
                    cachedProperties = propertyArrayList
                    mPropertyLists?.value = propertyArrayList
                }
            }, { error ->
                isLoading.value = false
                Log.e("Exception", error.message ?: "Unknown Error")
            })
    }

    fun clearPropertiesCache() {
        cachedProperties?.clear()
        mPropertyLists?.value = ArrayList()
    }


    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    override fun onCleared() {
        super.onCleared()
        mPropertyLists = null
        propertyRepository!!.propertySet.clear()
        propertyRepository = null
    }

    val listener: LiveData<ArrayList<Property>>?
        get() = mPropertyLists

    val value: ArrayList<Property>
        get() = mPropertyLists!!.value ?: ArrayList()
}