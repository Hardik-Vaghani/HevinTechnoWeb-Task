package com.hardik.hevintechnowebtask.di


import android.content.Context
import com.hardik.hevintechnowebtask.common.Constants
import com.hardik.hevintechnowebtask.data.database.AppDatabase
import com.hardik.hevintechnowebtask.data.database.dao.UserDao
import com.hardik.hevintechnowebtask.data.remote.api.ApiInterface
import com.hardik.hevintechnowebtask.data.repository.UserRepositoryImpl
import com.hardik.hevintechnowebtask.domain.repository.UserRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppModule{
    val apiInterface: ApiInterface
    val userRepository: UserRepository

    val userDao: UserDao
}

class AppModuleImpl( private val appContext: Context): AppModule{

    override val apiInterface: ApiInterface by lazy {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)//HEADERS,BODY,BASIC,NONE

        //region Interceptors

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Change to BASIC or HEADERS as needed
        }
        val applicationInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
//                .addHeader("Device-hash", "DEA1B88_3526_4619_B2A4_8315BC404567")
//                .addHeader("X-File-Name", "image_picker_2D8EEF0F-14F6-4D54-9273-92F87EFAD09A-56029-00001326BFA321FE.jpg")
//                .addHeader("Authorization", "Bearer IzKLBlsMIVsIUoIU3vNj4Sx8Oo317y5Nq5PIYJ2lfDOXrNWbo6SId2ElWYov2USD") // Set your authorization token here
                .addHeader("Authorization", "Bearer your_token")
                .build()
            chain.proceed(request)
        }
        val networkInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            // Modify the response
            response
        }
        val retryInterceptor = Interceptor { chain ->
            var response = chain.proceed(chain.request())
            var tryCount = 0
            while (!response.isSuccessful && tryCount < 3) {
                tryCount++
                response = chain.proceed(chain.request())
            }
            response
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // Logging interceptor
            .addInterceptor{chain ->
                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $token") // Add your token here
                    .build()
                chain.proceed(request) // Proceed with the request
            } // Authentication interceptor
//            .addInterceptor(retryInterceptor)// Retry interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Set write timeout
            .build()
        //endregion

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

//    override val userRepository: UserRepository by lazy { UserRepositoryImpl(apiInterface) }
    override val userRepository: UserRepository by lazy { UserRepositoryImpl(appContext, apiInterface) }


    override val userDao : UserDao by lazy { AppDatabase.getDatabase(appContext).userDao() }
}