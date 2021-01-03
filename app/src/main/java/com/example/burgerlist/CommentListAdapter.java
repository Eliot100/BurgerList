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

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView userId;
        TextView username;
        TextView message;
        TextView date;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String username = getItem(position).getName();
        String message = getItem(position).getMessage();
        String date = getItem(position).getDate();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.username_text);
            holder.message = (TextView) convertView.findViewById(R.id.message_text);
            holder.date = (TextView) convertView.findViewById(R.id.date_text);
            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.username.setText(username);
        holder.message.setText(message);
        holder.date.setText(date);

        return convertView;
    }
}


