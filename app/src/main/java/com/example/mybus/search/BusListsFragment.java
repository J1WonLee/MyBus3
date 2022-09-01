package com.example.mybus.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.FragmentBusListsBinding;
import com.example.mybus.viewmodel.SearchViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dagger.hilt.android.AndroidEntryPoint;


public class BusListsFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private FragmentBusListsBinding binding;
    private SearchListAdapter searchListAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_lists, container, false);
        initRecycler();
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        searchViewModel.busLists.observe(getViewLifecycleOwner(), new Observer<List<BusSchList>>() {
            @Override
            public void onChanged(List<BusSchList> busSchLists) {
                if (busSchLists != null){
                    searchListAdapter.updateLists(busSchLists);
                }else{
                    Log.d("kkang", "busschlists is null");
                }
            }
        });

        searchViewModel.getSharedData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.searchBusInput.setText(s);
                searchViewModel.getBusLists(s);
            }
        });
        setAutoResult();




        binding.searchBusInput.setOnClickListener(view -> {
            searchViewModel.getBusLists(binding.searchBusInput.getText().toString());
        });

        return binding.getRoot();
    }

    public void initRecycler(){
        recyclerView = binding.searchBusLists;
        searchListAdapter = new SearchListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchListAdapter);
    }

    public void setAutoResult(){
        binding.searchBusInput.addTextChangedListener(
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
                                        searchViewModel.getBusLists(binding.searchBusInput.getText().toString());
                                    }
                                },DELAY
                        );
                    }
                }
        );
    }

    @Override
    public void onPause() {
        searchViewModel.setSharedData(binding.searchBusInput.getText().toString());
        super.onPause();
    }
}