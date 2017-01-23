package com.lodoss.examples.service;

/**
 * Middleware class for dynamic headers modifying
 */
public class ApiRequestInterceptor extends Interceptor {
    public ApiRequestInterceptor(){
        super();
    }

    @Override
    public void intercept(RequestFacade facade) throws IOException {
        /* Adds authorization header to every request */
        facade.addHeader("Authorization", "Bearer " + AuthUtil.getInstance().getAuthToken());
    }
}
