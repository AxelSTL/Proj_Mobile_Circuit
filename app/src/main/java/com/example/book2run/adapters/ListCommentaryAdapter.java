package com.example.book2run.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.book2run.R;
import com.example.book2run.model.Circuit;
import com.example.book2run.model.Commentary;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListCommentaryAdapter extends ArrayAdapter<Commentary> {
    private static final String TAG = "CommentaryListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView nom;
        TextView message;
        ImageView etoile1, etoile2, etoile3, etoile4, etoile5;


    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public ListCommentaryAdapter(Context context, int resource, ArrayList<Commentary> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View result;

        //ViewHolder object
        ListCommentaryAdapter.ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ListCommentaryAdapter.ViewHolder();
            holder.nom = (TextView) convertView.findViewById(R.id.nom_adapter_circuit);
            holder.message = convertView.findViewById(R.id.message_commentary);
            holder.etoile1 = convertView.findViewById(R.id.stars1);
            holder.etoile2 = convertView.findViewById(R.id.stars2);
            holder.etoile3 = convertView.findViewById(R.id.stars3);
            holder.etoile4 = convertView.findViewById(R.id.stars4);
            holder.etoile5 = convertView.findViewById(R.id.stars5);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ListCommentaryAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        return convertView;
    }

    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
    }
}
