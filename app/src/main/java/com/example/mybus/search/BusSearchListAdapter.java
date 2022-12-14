package com.example.mybus.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.SearchListItemBinding;

import java.util.List;

public class BusSearchListAdapter extends RecyclerView.Adapter<BusSearchListAdapter.SearchViewHolder> {
    private List<BusSchList> lists;

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchListItemBinding binding = SearchListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        BusSchList b = lists.get(position);
        // 첫번째 아이템 중 서울 버스인 경우
        if (position == 0 && b.getBusRouteId().startsWith("1")){
            holder.binding.cityName.setText("서울");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(b.getBusRouteNm());
            holder.binding.listDesc.setText(setListDesc(b.getRouteType()));
        }else if (position == 0 && b.getBusRouteId().startsWith("16")){
            // 검색결과 인천 버스만 존재할 경우
            holder.binding.cityName.setText("인천");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(b.getBusRouteNm());
            holder.binding.listDesc.setText(setListDesc(b.getRouteType()));
        }else if (b.getBusRouteId().startsWith("16") && !(lists.get(position-1).getBusRouteId().startsWith("16")) ){
            // 인천인 경우
            holder.binding.cityName.setText("인천");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(b.getBusRouteNm());
            holder.binding.listDesc.setText(setListDesc(b.getRouteType()));
        }
        else if (position == 0 && b.getBusRouteId().startsWith("2")){
            // 검색결과 경기 버스만 존재할 경우
            holder.binding.cityName.setText("경기");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(b.getBusRouteNm());
            holder.binding.listDesc.setText(setListDesc(b.getRouteType()));
        }else if (position > 0 && b.getBusRouteId().charAt(0) != lists.get(position-1).getBusRouteId().charAt(0)){
            // 서울 -> 경기
            holder.binding.cityName.setText("경기");
            holder.binding.cityName.setVisibility(View.VISIBLE);
            holder.binding.listName.setText(b.getBusRouteNm());
            holder.binding.listDesc.setText(setListDesc(b.getRouteType()));
        }else{
            holder.binding.cityName.setVisibility(View.GONE);
            holder.binding.listName.setText(b.getBusRouteNm());
            holder.binding.listDesc.setText(setListDesc(b.getRouteType()));
        }
    }

    @Override
    public int getItemCount() {
        if (lists!=null){
            return lists.size();
        }else{
            return 0;
        }
    }

    public BusSchList getPositionItem(int position){
        return lists.get(position);
    }



    public void updateLists(List<BusSchList> newLists){
        lists = newLists;
        notifyDataSetChanged();
    }

    public String setListDesc(String type){
        switch (type){
            case "1":
                return "공항 버스";
            case "2":
                return "마을 버스";
            case "3":
                return "간선 버스";
            case "4":
                return "지선 버스";
            case "5":
                return "순환 버스";
            case "7":
                return "인천 버스";
            case "6":
                return "광역 버스";
            case "8":
                return "경기  버스";
            default:
                return "공용 버스";
        }
    }




    class SearchViewHolder extends RecyclerView.ViewHolder {
        SearchListItemBinding binding;
        public SearchViewHolder(SearchListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
