package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.wrapper.RouteSearchWrap;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.BusStopList;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.apisearch.wrapper.StopSearchWrap;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class RetrofitRepository implements RetrofitService {
    RetrofitService retrofitService;

    @Inject
    public RetrofitRepository( RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }


    @Override
    public Single<RouteSearchWrap> schBusKeyword(String serviceKey, String keyword, String json) {
        return retrofitService.schBusKeyword(serviceKey, keyword, "json");
    }


    @Override
    public Single<BusStopList> schBusStopList(String serviceKey, String budId, String json) {
        return null;
    }

    @Override
    public Single<BusSchList> schBusPosition(String serviceKey, String budId, String json) {
        return null;
    }

    @Override
    public Observable<StopSearchUidWrap> schStopKeyword(String servieKey, String keyword, String json) {
        return retrofitService.schStopKeyword(servieKey, keyword, json);
    }

    @Override
    public Observable<StopSearchUidWrap> schStopUid(String servieKey, String ardId, String json) {

        return retrofitService.schStopUid(servieKey, ardId, json);
    }

    @Override
    public Single<StopSearchUidWrap> schStopKeyword2(String servieKey, String keyword, String json) {
        return retrofitService.schStopKeyword2(servieKey, keyword, json);
    }

    @Override
    public Single<StopSearchUidWrap> schStopUid2(String servieKey, String ardId, String json) {
        return retrofitService.schStopUid2(servieKey, ardId, json);
    }
}
