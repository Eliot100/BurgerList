package com.example.burgerlist;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import androidx.annotation.NonNull;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by User on 3/14/2017.
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView username;
        TextView message;
        //TextView
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CommentListAdapter(Context context, int resource, ArrayList<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        //String name = getItem(position).getRess().getName();
        //double rate = getItem(position).getRetValue();

        //Create the person object with the information
        //RatingPreview r = new RatingPreview(name ,rate);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            //holder.name = (TextView) convertView.findViewById(R.id.BurgerName);
            //holder.rate = (TextView) convertView.findViewById(R.id.Score);
            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        //holder.name.setText(r.getResName());
        //holder.rate.setText(String.valueOf(rate));
        return convertView;
    }
}


