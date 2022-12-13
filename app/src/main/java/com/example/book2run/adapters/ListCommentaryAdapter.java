package com.example.book2run.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.StrictMode;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListCommentaryAdapter extends ArrayAdapter<Commentary> {
    private static final String TAG = "CommentaryListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private JSONObject userInfos;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView pseudo;
        TextView message;
        ImageView etoile1, etoile2, etoile3, etoile4, etoile5;
        ImageView imageUser;
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

        String pseudo = getItem(position).getNom();
        String message = "\" " + getItem(position).getMessage() + " \"";
        int etoiles = getItem(position).getEtoiles();

        this.getUsersInfo(Integer.parseInt(pseudo));

        final View result;

        //ViewHolder object
        ListCommentaryAdapter.ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ListCommentaryAdapter.ViewHolder();
            holder.pseudo = (TextView) convertView.findViewById(R.id.commentaryadapter_pseudo);
            holder.message = convertView.findViewById(R.id.commentaryadapter_message);
            holder.etoile1 = convertView.findViewById(R.id.stars1);
            holder.etoile2 = convertView.findViewById(R.id.stars2);
            holder.etoile3 = convertView.findViewById(R.id.stars3);
            holder.etoile4 = convertView.findViewById(R.id.stars4);
            holder.etoile5 = convertView.findViewById(R.id.stars5);
            holder.imageUser = convertView.findViewById(R.id.commentaryadapter_imageview);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ListCommentaryAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        try {
            System.out.println(userInfos);
            holder.pseudo.setText(userInfos.getString("pseudo"));
            holder.message.setText(message);
            holder.imageUser.setImageBitmap(getBitmapFromBase64(userInfos.getString("image")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(etoiles == 0){

            holder.etoile1.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile2.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile3.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile4.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile5.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));

        }
        if(etoiles == 1){
            holder.etoile1.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile2.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile3.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile4.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile5.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));;
        }
        if(etoiles == 2){
            holder.etoile1.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile2.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile3.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile4.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile5.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));

        }
        if(etoiles == 3){
            holder.etoile1.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile2.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile3.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile4.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
            holder.etoile5.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));

        }
        if(etoiles == 4){
            holder.etoile1.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile2.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile3.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile4.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile5.setBackground(mContext.getResources().getDrawable(R.drawable.ic_star_vide));
        }
        if(etoiles == 5){
            holder.etoile1.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile2.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile3.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile4.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
            holder.etoile5.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
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

    public void getUsersInfo(int code){
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.169:8180/utilisateur/" + code;
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            userInfos = new JSONObject(String.valueOf(buffer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
