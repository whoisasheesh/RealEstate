package com.whoisasheesh.realestate

import com.google.gson.Gson
import com.whoisasheesh.realestate.api.PropertyApi
import com.whoisasheesh.realestate.api.PropertyApiService
import com.whoisasheesh.realestate.data.Property
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class PropertyAPIServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: PropertyApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // creating a instance of Retrofit with URL for MockWebServer
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PropertyApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testPropertiesListEndpoint() {
        val mockProperty = initProperty()
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("""{"data": [${Gson().toJson(mockProperty)}]}""")

        mockWebServer.enqueue(mockResponse)
        val response = PropertyApi.apiService.propertiesList.execute()

        // Assert that the request was successful
        assert(response.isSuccessful)

        val properties = response.body()
        assert(properties != null)
    }

    private fun initProperty(): Property {
        return Property(
            propertyId = "141241",
            bedroom = "3",
            bathroom = "2",
            carspace = "1",
            propertyPrice = "90000000",
            currencyType = "AUD",
            propertyType = "Apartment",
            saleType = "rent",
            agentInfo = "Ashish",
            ownerInfo = "King",
            address = "2 Bowden Street",
            suburb = "Meadowbank",
            postcode = "2114",
            state = "NSW",
            agentImg = "https://images.pexels.com/photos/937481/pexels-photo-937481",
            propertyImg = "https://images.pexels.com/photos/937481/pexels-photo-937481",
            ownerImg = "https://images.pexels.com/photos/937481/pexels-photo-937481",
            propertyDesc = "This property belong to Sentia RealEstate"
        )
    }
}