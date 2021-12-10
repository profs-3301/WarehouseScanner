package  com.goodswarehouse.scanner.data.di

import android.content.Context
import android.content.SharedPreferences
import BASE_ENDPOINT
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.App
import com.goodswarehouse.scanner.BuildConfig.DEBUG
import com.goodswarehouse.scanner.PREF_NAME
import com.goodswarehouse.scanner.data.addHeadersWithDefault
import com.goodswarehouse.scanner.data.bodyToString
import com.goodswarehouse.scanner.data.buildResponse
import com.goodswarehouse.scanner.data.toJsonObject
import com.goodswarehouse.scanner.domain.repo.RepoLocal
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class BaseModule {

    @Provides
    @Singleton
    fun provideApp(): App = App.get()

    @Provides
    @Singleton
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    @Provides
    @Rest(REST_SERVER)
    @Singleton
    internal fun provideOkHttpClient(tokenRepo: RepoLocal): OkHttpClient = OkHttpClient.Builder()
        .addHeadersWithDefault(tokenRepo)
        .addInterceptor {
            return@addInterceptor it.proceed(it.request()).run {
                if (!isSuccessful) {
                    bodyToString().toJsonObject()?.let {
                        when {
                            it.has("message") -> buildResponse(it.get("message").toString())
                            it.has("errorMessage") -> buildResponse(it.get("errorMessage").toString())
                            it.has("error") -> buildResponse(it.get("error").asJsonObject.get("message").toString())
                            else -> this@run
                        }
                    } ?: this
                } else {

                    this
                }
            }

        }
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(HttpLoggingInterceptor().setLevel(DEBUG({ HttpLoggingInterceptor.Level.BODY },
            { HttpLoggingInterceptor.Level.BODY })))
        .build()


    @Provides
    @Rest(REST_SERVER)
    @Singleton
    internal fun provideApiRetrofit(@Rest(REST_SERVER) okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Rest(REST_GENERAL)
    @Singleton
    internal fun provideApiRetrofitGeneral(): Retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder()
            .addInterceptor {
                //here you can parse the response explicitly
                return@addInterceptor it.proceed(it.request())
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(DEBUG({ HttpLoggingInterceptor.Level.BODY },
                { HttpLoggingInterceptor.Level.BODY })))
            .build())
        .baseUrl(BASE_ENDPOINT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    
}