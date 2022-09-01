package com.example.mybus.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mybus.databinding.ActivityMyAlarmBinding;

public class MyAlarm extends AppCompatActivity {
    private ActivityMyAlarmBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}