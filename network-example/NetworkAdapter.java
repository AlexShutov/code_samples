package com.lodoss.examples.service;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Adapter for client - server communication
 */

public class NetworkAdapter {
    /**
     * Allows a client to fetch application information.
     * @param gameId
     * @return
     */
    @GET("application/{id}")
    Single<BaseResponse<Game>> getGameInfo(@Path("id") String gameId);

    /**
     * A new login mechanism can be added for a user using this api.
     * @param request {@link AddEmailLoginAddressRequest}
     * @return {@link UserResponse}
     */
    @POST("user/auth")
    Single<BaseResponse<User>> addEmailLoginAddress(@Body AddEmailLoginAddressRequest request);

    /**
     * Allows a user to delete a payment method
     * @param userId
     * @param paymentId id of {@link PaymentMethod}
     * @return
     */
    @DELETE("/user/{user_id}/paymentinfo/{payment_id}")
    Single<BaseResponse<User>> deletePaymentMethod(@Path("user_id") String userId,
             @Path("payment_id") String paymentId);

    /**
     * Allows to user to modify default PM
     * @param userId
     * @param paymentId
     * @param request
     * @return
     */
    @PUT("user/{id}/paymentinfo/{payment_id}/default")
    Single<AddPaymentMethodResponse> setDefaultPaymentMethod(@Path("id") String userId,
                                                             @Path("payment_id") String paymentId,
                                                             @Body SetDefaultPaymentMethodRequest request);
}
