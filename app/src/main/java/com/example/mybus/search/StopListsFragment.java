package com.example.mybus.search;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mybus.R;
import com.example.mybus.databinding.FragmentStopListsBinding;
import com.example.mybus.viewmodel.SearchViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class StopListsFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private ViewPager2 viewPager2;
    private FragmentStopListsBinding binding;
    private String getText;
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

            }
        });

//        searchViewModel.getStopLists("명동");









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
                                        searchViewModel.getStopLists(binding.searchStopInput.getText().toString());
                                    }
                                },DELAY
                        );
                    }
                }
        );

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        searchViewModel.setSharedData(binding.searchStopInput.getText().toString());
        super.onPause();
    }
}