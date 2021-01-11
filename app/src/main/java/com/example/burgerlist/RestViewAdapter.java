package com.example.burgerlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RestViewAdapter extends ArrayAdapter<RestView> {
    private static Context mContext;
    private int mResource;

    private static class ViewHolder implements View.OnClickListener{//
        ImageView image;
        String restId;
        TextView name, dist, city;
        RatingBar ratingBar;
        Button button;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RestViewAdapter.mContext, RestPage.class);
            intent.putExtra("Owner_id", restId);
            mContext.startActivity(intent);
        }
    }

    public RestViewAdapter(@NonNull Context context, int resource, @NonNull List<RestView> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Toast.makeText(mContext, "new RestViewListAdapter", Toast.LENGTH_LONG).show();

        String restId = getItem(position).getRestId();
        String restName = getItem(position).getRestName();
        String restDist = getItem(position).getRestDist();
        String restCity = getItem(position).getRestCity();
        String currentRating = getItem(position).getCurrentRating();

        final View result;
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
//            holder.button = (Button) convertView.findViewById(R.id.childButton);
            holder.name = convertView.findViewById(R.id.restname_item);
            holder.dist = convertView.findViewById(R.id.distance_item);
            holder.city = convertView.findViewById(R.id.city_item);
            holder.image = (ImageView) convertView.findViewById(R.id.restImg);
            holder.ratingBar = convertView.findViewById(R.id.ratingStars);
            holder.restId = restId;

            convertView.setTag(holder);
            result = convertView;
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.name.setText(restName);
        holder.dist.setText(restDist);
        holder.city.setText(restCity);
        holder.image.setImageBitmap(initRestImg(restId));
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setMax(10);
        holder.ratingBar.setRating( Float.parseFloat(currentRating)/2);

        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onClick(result);
            }
        });
        return convertView;
    }

    private Bitmap initRestImg(final String restId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads")
                .child(restId).child("RestImage.jpg");
        File restImageFile = null;
        final Bitmap[] pic = new Bitmap[1];
        try {
            restImageFile = File.createTempFile("RestImg", "jpg");
            File finalRestImageFile = restImageFile;
            storageReference.getFile(restImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(mContext, "Success to get Image", Toast.LENGTH_SHORT).show();
                    pic[0] = BitmapFactory.decodeFile(finalRestImageFile.getAbsolutePath());
                }
            });
            if(pic[0] == null) pic[0] = BitmapFactory.decodeResource(MainActivity.resources, R.raw.burger);
        } catch (Exception e) {
            pic[0] = BitmapFactory.decodeResource(MainActivity.resources, R.raw.burger);
        }
        return pic[0];
    }
}
