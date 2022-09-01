package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.apisearch.msgBody.StopSearchUid;
import com.example.mybus.apisearch.wrapper.RouteSearchWrap;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SearchViewModel extends ViewModel {
    private List<StopSearchUid> lists = new ArrayList<>();
    private List<StopUidSchList> uidSchLists = new ArrayList<>();
    private Observable observable;

    public static String serviceKey;
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<String> sharedData = new MutableLiveData<>();    // 프레그먼트간 데이터 공유

    private RetrofitRepository retrofitRepository;
    private RetrofitGbusRepository retrofitGbusRepository;
    // 버스 번호 조회 검색 목록을 담을 livedata
    public MutableLiveData<List<BusSchList>> busLists = new MutableLiveData<List<BusSchList>>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<List<StopUidSchList>> stopLists = new MutableLiveData<>();


    private boolean isDisposed = false;

    @Inject
    public SearchViewModel(RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository) {
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusRepository = retrofitGbusRepository;
    }



    public void getBusLists(String keyword){
        compositeDisposable.add(retrofitRepository.schBusKeyword(serviceKey, keyword, "json")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RouteSearchWrap>(){
                    @Override
                    public void onSuccess(@NonNull RouteSearchWrap routeSearchWrap) {
                        if (routeSearchWrap.getBusRouteSearch().getItemList() != null){
                            Collections.sort(routeSearchWrap.getBusRouteSearch().getItemList());
                            busLists.setValue(routeSearchWrap.getBusRouteSearch().getItemList());
                        }

                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("kkang" , " 버스 목록 조회 에러  : "  + e.getMessage());
                        busLists.setValue(null);
                    }
                })
        );
    }
    /*
    public void getStopLists(String keyword) {
        Log.d("kkang", "getStopLists!");
        observable.merge(
                        retrofitRepository.schStopKeyword(serviceKey, keyword, "json")
                                .concatMap(list -> Observable.fromIterable(list.getStopSearchUid().getItemLists()))
                                .filter(item -> item.getArsId().startsWith("1"))
                                .concatMap(item -> retrofitRepository.schStopUid(serviceKey, item.arsId, "json")),
                        retrofitRepository.schStopKeyword(serviceKey, keyword, "json")
                                .concatMap(list -> Observable.fromIterable(list.getStopSearchUid().getItemLists()))
                                .filter(item -> item.getArsId().startsWith("2"))
                                .concatMap(item -> retrofitRepository.schStopKeyword(serviceKey, keyword, "json"))
                ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StopSearchUidWrap>(){
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("kkang", "called on subscribe");
                    }

                    @Override
                    public void onNext(@NonNull StopSearchUidWrap stopSearchUidWrap) {
                        lists.add(stopSearchUidWrap.getStopSearchUid());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("kkang", "getStopLists" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        for (int i=0; i< lists.size(); i++){
                            for (int j=0; j<lists.get(i).getItemLists().size(); j++){
                                Log.d("kkang", lists.get(i).getItemLists().get(j).arsId);
                            }
                        }
                        stopLists.setValue(uidSchLists);
                    }
                });
    }

     */




    public void getStopLists(String keyword) {
        Log.d("kkang", "getStopLists!");
//        disposable.add(
        Disposable tempDisposable =
                retrofitRepository.schStopKeyword(serviceKey, keyword, "json")
                        .concatMap(list -> Observable.fromIterable(list.getStopSearchUid().getItemLists()))
//                        .filter(item -> (item.getStId().startsWith("1")))
                        .concatMap(item -> retrofitRepository.schStopUid(serviceKey, item.arsId, "json"))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<StopSearchUidWrap>() {
                            @Override
                            public void onNext(@NonNull StopSearchUidWrap stopSearchUidWrap) {
//                                Log.d("kkang", "called on next!");
//                                Log.d("kkang onNext", stopSearchUidWrap.getStopSearchUid().getItemLists().size()+"  || " + isDisposed());
                                Log.d("kkang onNext","");

                                /*for (int i=0; i< stopSearchUidWrap.getStopSearchUid().getItemLists().size(); i++){
                                    Log.d("kkang", stopSearchUidWrap.getStopSearchUid().getItemLists().get(i).arsId +" ");
                                }*/
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang    onError", "called on onError!");
                            }

                            @Override
                            public void onComplete() {
                                Log.d("kkang    onComplete", "called on onComplete!");
                            }
                        });
//        );
        compositeDisposable.add(tempDisposable);

    }



 //   public void getStopLists3(String keyword) {
//        disposable.add(
 //               retrofitRepository.schStopKeyword2(serviceKey, keyword, "json")
 //                       .toObservable()
 //                       .concatMap(item -> item.getStopSearchUid().getItemLists())
  //                      .concatMap()
 //       );



//    }




    public void dispose(){
        Log.d("kkang    dispose()", "");
        compositeDisposable.dispose();
        isDisposed = compositeDisposable.isDisposed();
    }

    public void newDispose(){
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<String> getSharedData() {
        return sharedData;
    }

    public void setSharedData(String str) {
        sharedData.setValue(str);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }




}
