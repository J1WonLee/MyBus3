package com.example.mybus.search;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.databinding.FragmentStopListsBinding;
import com.example.mybus.viewmodel.SearchViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StopListsFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private ViewPager2 viewPager2;
    private FragmentStopListsBinding binding;
    private String getText;
    private RecyclerView recyclerView;
    private StopSearchListAdapter stopListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stop_lists, container, false);
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        searchViewModel.getSharedData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // 좌우 스와이프시 자동검색이지만 자동검색 기능 고려해봐야함(트래픽 다씀 0902)
            }
        });

        initRecycler();

//        searchViewModel.mutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<StopSearchUidWrap>>() {
//            @Override
//            public void onChanged(List<StopSearchUidWrap> stopSearchUidWraps) {
//                if (stopSearchUidWraps != null){
//                    for (int i=0; i< stopSearchUidWraps.size(); i++){
//                        Log.d("kkang stopwraps  ",  i + " st " + stopSearchUidWraps.get(i).getStopSearchUid().getItemLists().size() +" ");
//                        for (int j=0; j<stopSearchUidWraps.get(i).getStopSearchUid().getItemLists().size(); j++ ){
//                            Log.d("kkang stopwraps  ",  i + " st " + stopSearchUidWraps.get(i).getStopSearchUid().getItemLists().get(j).getStId() +" ");
//                        }
//                    }
//                }
//            }
//        });

        searchViewModel.searchorderLists.observe(getViewLifecycleOwner(), new Observer<List<StopSchList>>() {
            @Override
            public void onChanged(List<StopSchList> stopSchLists) {
                if (stopSchLists != null){
                    stopListAdapter.updateLists(stopSchLists);
                }
            }
        });
        setAutoResult();











        return binding.getRoot();
    }

    public void initRecycler(){
        recyclerView = binding.searchStopLists;
        stopListAdapter = new StopSearchListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stopListAdapter);
    }

    @Override
    public void onPause() {
        searchViewModel.setSharedData(binding.searchStopInput.getText().toString());
        super.onPause();
    }
    
    // 자동검색 기능 삭제 고려
    public void setAutoResult(){
        binding.searchStopInput.addTextChangedListener(
                new TextWatcher() {
                    private Timer timer = new Timer();
                    private final long DELAY = 800;
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }


                    @Override
                    public void afterTextChanged(Editable editable) {
                        timer.cancel();
                        searchViewModel.dispose();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        searchViewModel.newDispose();
                                        searchViewModel.stopListsKeyword(binding.searchStopInput.getText().toString());
                                    }
                                },DELAY
                        );
                    }
                }
        );

    }
}