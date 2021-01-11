package com.example.burgerlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    private ArrayList<String> rest_id_list;
    private ArrayList<String> rest_name_list;
    private ArrayList<String> rating_list;
    private ArrayList<String> city_list;
    private ArrayList<String> distance_list;
    private ArrayList<Bitmap> logo_list;
    private OnRestClickListner monRestClickListner;


    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView logo;
        TextView rest_name;
        TextView city;
        TextView distance;
        OnRestClickListner onRestClickListner;

        public SearchViewHolder(@NonNull View itemView, OnRestClickListner onRestClickListner) {
            super(itemView);
            logo = (ImageView)itemView.findViewById(R.id.rest_logo_image);
            rest_name = (TextView)itemView.findViewById(R.id.restname_item);
            city = (TextView)itemView.findViewById(R.id.city_item);
            distance = (TextView)itemView.findViewById(R.id.distance_item);
            this.onRestClickListner = onRestClickListner;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onRestClickListner.onRestClick(getAdapterPosition());
        }
    }

    public SearchAdapter(Context context, ArrayList<String> rest_id_list, ArrayList<String> rest_name_list,
                         ArrayList<String> rating_list ,ArrayList<String> city_list, ArrayList<String> distance_list ,
                         OnRestClickListner onRestClickListner) {
        this.context = context;
        this.rest_id_list = rest_id_list;
        this.rest_name_list = rest_name_list;
        this.rating_list = rating_list;
        this.city_list = city_list;
        this.distance_list = distance_list;
        this.monRestClickListner = onRestClickListner;
    }

    public SearchAdapter(Context context, ArrayList<String> rest_id_list, ArrayList<String> rest_name_list,
                         ArrayList<String> rating_list ,ArrayList<String> city_list, ArrayList<String> distance_list ,
                         OnRestClickListner onRestClickListner , ArrayList<Bitmap> logo_list) {
        this.context = context;
        this.rest_id_list = rest_id_list;
        this.rest_name_list = rest_name_list;
        this.rating_list = rating_list;
        this.city_list = city_list;
        this.distance_list = distance_list;
        this.monRestClickListner = onRestClickListner;
        this.logo_list = logo_list;
    }





    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item,parent,false);
        return new SearchAdapter.SearchViewHolder(view , monRestClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.rest_name.setText(rest_name_list.get(position));
        holder.city.setText(city_list.get(position));
        holder.distance.setText(distance_list.get(position)+" km");
        holder.logo.setImageBitmap(logo_list.get(position));

        // need picture for logo and distance here.
    }



    @Override
    public int getItemCount() {
        return rest_name_list.size();
    }

    public interface OnRestClickListner{
        void onRestClick(int position);
    }
}
