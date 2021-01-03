package com.example.burgerlist;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */

public class MenuListAdapter extends ArrayAdapter<Dish> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView decription;
        TextView price;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public MenuListAdapter(Context context, int resource, ArrayList<Dish> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String product_name = getItem(position).getName();
        String decription = getItem(position).getDec();
        String price = getItem(position).getPrice();

        //Create the person object with the information

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.productName);
            holder.decription = (TextView) convertView.findViewById(R.id.productDescrption);
            holder.price = (TextView) convertView.findViewById(R.id.productPrice);
            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.name.setText(product_name);
        holder.decription.setText(decription);
        holder.price.setText(price);

        return convertView;
    }
}


