package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.apisearch.msgBody.StopSearchUid;
import com.example.mybus.apisearch.wrapper.RouteSearchWrap;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.apisearch.wrapper.StopSearchWrap;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<StopSearchUidWrap> lists = new ArrayList<>();
    public MutableLiveData<List<StopSearchUidWrap>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> sharedData = new MutableLiveData<>();    // 프레그먼트간 데이터 공유
    private List<StopSchList> searchlists = new ArrayList<>();   // 정류소 검색 목록 (키워드만)
    private List<StopUidSchList> uidSchLists = new ArrayList<>();   // 정류소 중 진행방향 나와 있는 목록
    public MutableLiveData<List<StopSchList>> searchorderLists = new MutableLiveData<>();

    public static String serviceKey;
    private  static int total = 0;
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

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

    public void stopListsKeyword(String keyword){
        compositeDisposable.add(
                retrofitRepository.schStopKeywordv2(serviceKey, keyword, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<StopSearchWrap>(){
                            @Override
                            public void onSuccess(@NonNull StopSearchWrap stopSearchWrap) {
                                Log.d("kkang", "onSuccess on stopListKeyword!");
                                total  = stopSearchWrap.getStopSearch().getItemList().size() -1;
                                Log.d("kkang", "total is = " + total);
                                for (int i=0; i<stopSearchWrap.getStopSearch().getItemList().size(); i++){
                                    if (stopSearchWrap.getStopSearch().getItemList().get(i).getArsId().equals("0") ||
                                            stopSearchWrap.getStopSearch().getItemList().get(i).getStId().equals("0")
                                        ){
                                        total --;
                                        Log.d("kkang", "stopListsKeyword is = " + total);
                                        continue;
                                    }else{
                                        stopListsUid(stopSearchWrap.getStopSearch().getItemList().get(i), i);
                                    }
                                }
                                searchlists = stopSearchWrap.getStopSearch().getItemList();
                                Collections.sort(searchlists);
//                              searchorderLists.setValue(searchlists);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        })
        );
    }

    public void stopListsUid (StopSchList lists, int pos){
        if (lists != null){
            compositeDisposable.add(
                    retrofitRepository.schStopUidv2(serviceKey, lists.getArsId(), "json")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<StopSearchUidWrap>(){
                                @Override
                                public void onSuccess(@NonNull StopSearchUidWrap stopSearchUidWrap) {
                                    total--;
                                    Log.d("kkang", "stopListsUid total is : " + total);
                                    if (stopSearchUidWrap.getStopSearchUid().getItemLists() != null){
//                                        uidSchLists = stopSearchUidWrap.getStopSearchUid().getItemLists();
                                       for (int i=0; i<stopSearchUidWrap.getStopSearchUid().getItemLists().size(); i++){
                                           Log.d("kkang", stopSearchUidWrap.getStopSearchUid().getItemLists().get(i).getArsId() +"  is ars id of " + i + "st");
                                           Log.d("kkang", stopSearchUidWrap.getStopSearchUid().getItemLists().get(i).getAdirection() +" is direction!");
                                           break;
                                       }
                                    }else{
                                        searchlists.get(pos).setNextDir("다음정류장입니다!");
                                    }

                                    if (total == -1){
                                        searchorderLists.setValue(searchlists);
                                        // 진행방향 정류장 , 검색 결과 맵핑 후 setValue();
                                        // 진행방향 옵저버 달아서, setValue() 했을 때 맵핑 해주기
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.d("kkang", "stopListsUid error message = " + e.getMessage());
                                }
                            })
            );
        }

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
        lists.clear();
        Disposable tempDisposable =
                retrofitRepository.schStopKeyword(serviceKey, keyword, "json")
                        .concatMap(list -> Observable.fromIterable(list.getStopSearchUid().getItemLists()))
//                        .filter(item -> (item.getStId().startsWith("1")))
//                        .concatMap(item -> retrofitRepository.schStopUid(serviceKey, item.arsId, "json"))
                        .concatMap(item -> retrofitRepository.getStopLists(serviceKey, keyword, item.getArsId(), item.getStId()))
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
                                Log.d("kkang    onError", "called on onError!" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d("kkang    onComplete", "called on onComplete!");
                                Log.d("kkang total lists : " , lists.size()+" is total size");
                                mutableLiveData.setValue(lists);
                            }
                        });
//        );
        compositeDisposable.add(tempDisposable);

    }


    public void getStopListsMerge(String keyword) {
        lists.clear();
        Log.d("kkang", "getStopLists!");
        Disposable tempDisposable =
                (Disposable) Observable.merge(
                        retrofitRepository.schStopKeyword(serviceKey, keyword, "json")
                                .concatMap(list -> Observable.fromIterable(list.getStopSearchUid().getItemLists()))
                                .filter(item -> (item.getStId().startsWith("1")))
                                .concatMap(item -> retrofitRepository.schStopUid(serviceKey, item.arsId, "json")),

                        retrofitRepository.schStopKeyword(serviceKey, keyword, "json")
                                .concatMap(list -> Observable.fromIterable(list.getStopSearchUid().getItemLists()))
                                .filter(item -> (item.getStId().startsWith("2")))
                                .concatMap(item -> retrofitRepository.schStopKeyword(serviceKey, keyword, "json"))
                ).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<StopSearchUidWrap>() {
                            @Override
                            public void onNext(@NonNull StopSearchUidWrap stopSearchUidWrap) {
//                                Log.d("kkang", "called on next!");
//                              Log.d("kkang onNext", stopSearchUidWrap.getStopSearchUid().getItemLists().size()+"  || " + isDisposed());
 //                               Log.d("kkang onNext","");

                                /*for (int i=0; i< stopSearchUidWrap.getStopSearchUid().getItemLists().size(); i++){
                                    Log.d("kkang", stopSearchUidWrap.getStopSearchUid().getItemLists().get(i).arsId +" ");
                                }*/
                                lists.add(stopSearchUidWrap);
                                try{
                                    for (int i=0; i< stopSearchUidWrap.getStopSearchUid().getItemLists().size(); i++){
                                        Log.d("kkang lists contents = " , stopSearchUidWrap.getStopSearchUid().getItemLists().get(i).getArsId() + " st");
                                    }
                                }catch (Exception e){
                                    Log.d("kkang exception " , e.getMessage());
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang    onError", "called on onError!");

                            }

                            @Override
                            public void onComplete() {
                                Log.d("kkang    onComplete", "called on onComplete!");
                                Log.d("kkang total lists : " , lists.size()+" is total size");
                                mutableLiveData.setValue(lists);
                            }
                        });
//        );
        compositeDisposable.add(tempDisposable);

    }




    public void dispose(){
        Log.d("kkang    dispose()", "");
        compositeDisposable.dispose();
        isDisposed = compositeDisposable.isDisposed();
        lists.clear();
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
