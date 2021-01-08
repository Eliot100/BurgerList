package com.example.burgerlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class restViewAdapter extends ArrayAdapter<RestView> {
    private Context mContext;
    private int mResource;

    private static class ViewHolder {
        ImageView image;
        TextView name, dist, city;
        RatingBar ratingBar;
    }

    public restViewAdapter(@NonNull Context context, int resource, @NonNull List<RestView> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toast.makeText(mContext, "new RestViewListAdapter", Toast.LENGTH_LONG).show();

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
            holder.name = convertView.findViewById(R.id.restname_item);
            holder.dist = convertView.findViewById(R.id.distance_item);
            holder.city = convertView.findViewById(R.id.city_item);
            holder.image = convertView.findViewById(R.id.rest_img);
            holder.ratingBar = convertView.findViewById(R.id.ratingStars);

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
//        holder.image = initRestImg(restId);
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setMax(10);
        holder.ratingBar.setRating( Float.parseFloat(currentRating));
        holder.ratingBar.cancelDragAndDrop();

        return convertView;
    }

    private ImageView initRestImg(final String restId) {
        ImageView rest_img = null;
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads")
//                .child(restId).child("RestImage.jpg");
//        try {
//            File restImageFile = File.createTempFile("RestImg", "jpg");
//            storageReference.getFile(restImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(mContext.getApplicationContext(), "Success to get Image", Toast.LENGTH_SHORT).show();
//                    Bitmap pic = BitmapFactory.decodeFile(restImageFile.getAbsolutePath());
//                    rest_img.setImageBitmap(pic);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        } catch (IOException e) {}
//        rest_img.setImageBitmap(BitmapFactory.decodeResource(MainActivity.resources, R.raw.burger));
        return rest_img;
    }
}
