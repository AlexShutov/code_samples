package com.lodoss.examples.service;

/**
 * Container for network-related dependencies
 */

public class ApiModule {

    /**
     * Provides middleware class
     * @return instance of ApiRequestInterceptor
     */
    @Provides
    public ApiRequestInterceptor provideDbqRequestInterceptor(){
        return new ApiRequestInterceptor();
    }


    /**
     * Provides instance of Retrofit for making network calls
     * @param client - instance of http client, provided via DI
     * @param environmentUtil - utility for dynamic changing target endpoint
     * @return instance of Retrofit
     */
    @Provides
    @Singleton
    public Retrofit provideRestAdapter(@Named("okHtppClient") OkHttpClient client, ApiRequestInterceptor interceptor){
        Retrofit API = new Retrofit.Builder()
                .client(client)
                .baseUrl(environmentUtil.getCurrentCoreActiveEnvironment())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addNetworkInterceptor(interceptor)
                .build();
        return API;
    }
}
