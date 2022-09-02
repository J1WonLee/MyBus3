package com.example.mybus.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.databinding.SearchListItemBinding;

import java.util.List;

// 정류장 검색 어뎁터
public class StopSearchListAdapter  extends RecyclerView.Adapter<StopSearchListAdapter.StopSearchListViewHolder> {
    private List<StopSchList> lists;

    @NonNull
    @Override
    public StopSearchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchListItemBinding binding = SearchListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false );
        return new StopSearchListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StopSearchListViewHolder holder, int position) {
        StopSchList stopSchList = lists.get(position);
        if (position == 0 && stopSchList.getStId().startsWith("1")){
            holder.binding.cityName.setText("서울");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(stopSchList.getStNm());
            holder.binding.listDesc.setText(stopSchList.getArsId() + setListDir(stopSchList.nextDir));
        } else if (position == 0 && stopSchList.getStId().startsWith("2")){
            // 검색결과 경기도 정류장만 존재할 경우
            holder.binding.cityName.setText("경기");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(stopSchList.getStNm());
            holder.binding.listDesc.setText(stopSchList.getArsId() + setListDir(stopSchList.nextDir));
        }else if (position > 0 && stopSchList.getStId().charAt(0) != lists.get(position-1).getStId().charAt(0)){
                // 서울 -> 경기
            holder.binding.cityName.setText("경기");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(stopSchList.getStNm());
            holder.binding.listDesc.setText(stopSchList.getArsId() + setListDir(stopSchList.nextDir));
        }else{
            holder.binding.cityName.setVisibility(View.GONE);
            holder.binding.listName.setText(stopSchList.getStNm());
            holder.binding.listDesc.setText(stopSchList.getArsId() + setListDir(stopSchList.nextDir));
        }

    }

    @Override
    public int getItemCount() {
       if (lists != null){
           return lists.size();
       }else{
           return 0;
       }
    }

    public void updateLists(List<StopSchList> newLists){
        lists = newLists;
        notifyDataSetChanged();
    }

    public String setListDir(String dir){
        if (dir == null){
            return " | 정보가 없습니다";
        }else{
            return " | " + dir;
        }
    }

    class StopSearchListViewHolder extends RecyclerView.ViewHolder {
        private SearchListItemBinding binding;
        public StopSearchListViewHolder(SearchListItemBinding searchListItemBinding) {
            super(searchListItemBinding.getRoot());
            binding =  searchListItemBinding;
        }
    }
}
