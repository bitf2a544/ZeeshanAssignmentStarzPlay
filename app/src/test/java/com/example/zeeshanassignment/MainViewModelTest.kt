package com.example.zeeshanassignment

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mylibrary.data.model.Carousels
import com.example.zeeshanassignment.BuildConfig.BASE_URL
import com.example.mylibrary.data.remote.api.ApiHelperImpl
import com.example.mylibrary.data.remote.api.ApiService
import com.example.mylibrary.repository.MainRepository
import com.example.mylibrary.utils.NetworkHelper
import com.example.mylibrary.utils.Resource
import com.example.zeeshanassignment.viewmodel.MainViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: MainRepository
    private lateinit var networkHelper: NetworkHelper
    private lateinit var apiHelper: ApiHelperImpl

    @Mock
    private lateinit var cardListingObserver: Observer<Resource<Carousels>?>

    private lateinit var mockWebServer: MockWebServer

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope1 = TestCoroutineScope(testDispatcher)
    private var context: Context? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        var okHttpClient: OkHttpClient
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            okHttpClient = OkHttpClient
                .Builder()
                .build()
        }

        var requestBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        apiHelper = ApiHelperImpl(requestBuilder.create(ApiService::class.java))
        context = mock(Context::class.java)
        networkHelper = NetworkHelper(context!!)

        repository = MainRepository(apiHelper, networkHelper)
        viewModel = MainViewModel(repository)
        viewModel.getCarouselsMutableLiveData().observeForever(cardListingObserver)
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun `read api success json file`() {
        val reader = MockJsonResponseFileReader("success_response.json")
        assertNotNull(reader.strContent)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetch data from api and check response Code 200 returned`() = testScope1.runTest {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockJsonResponseFileReader("success_response.json").strContent)
        mockWebServer.enqueue(response)

        val actualResponse = apiHelper.getLatestResultsFromAPI(BuildConfig.API_KEY,"jobs")

        assertEquals(
            response.toString().contains("200"),
            actualResponse.code().toString().contains("200")
        )
    }

    @Test
    fun `fetch details and check response success returned`() = testScope1.runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockJsonResponseFileReader("success_response.json").strContent)
        mockWebServer.enqueue(response)
        val actualResponse = apiHelper.getLatestResultsFromAPI(BuildConfig.API_KEY,"jobs")
        // System.out.println("actualResponse.body()?.success=" + actualResponse.body()?.success);
      //  assertEquals(true, actualResponse.body()?.results?.size!! > 0)
        assertEquals(true, actualResponse.isSuccessful)
    }

    @After
    fun tearDown() {
        viewModel.getCarouselsMutableLiveData().removeObserver(cardListingObserver)
        mockWebServer.shutdown()
    }
}