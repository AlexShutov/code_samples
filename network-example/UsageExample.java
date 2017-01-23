package com.lodoss.examples.service;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Example of intercating with {@link NetworkAdapter}
 */

public class UsageExample extends ApiModule {

    @Inject
    private NetworkAdapter mApiClient;

    public static void main(String[] args){
        /* initializing dependencies */
        //...
        /* end of initializing dependencies */

        /* Request will be executed in background, and {@link Game} object will be returned */
        mApiClient.getGameInfo("some_game_id")
                .map(new Function<BaseResponse<Game>, Game>() {
                    @Override
                    public Game apply(BaseResponse<Game> gameBaseResponse) throws Exception {
                        return gameBaseResponse.getResponse();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
