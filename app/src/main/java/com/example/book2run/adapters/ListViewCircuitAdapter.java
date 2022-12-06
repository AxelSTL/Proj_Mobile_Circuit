package com.example.book2run.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ListViewCircuitAdapter extends ArrayAdapter<Circuit> {

    private static final String TAG = "CircuitListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView nom;
        TextView description;
        TextView ville;
        TextView codePostal;
        TextView adresse;
        ImageView image;
        TextView prix;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public ListViewCircuitAdapter(Context context, int resource, ArrayList<Circuit> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //sets up the image loader library
        //get the circuit information
        String nom = getItem(position).getNom();
        String adresse = getItem(position).getAdresse();
        String description = getItem(position).getDescription();
        String ville = getItem(position).getVille();
        String codePostal = String.valueOf(getItem(position).getCodePostal());
        String mainImage = getItem(position).getMainImg();
        String prix = String.valueOf(getItem(position).getPrice());

        /*String img2Url = getItem(position).getImage1();
        String img3Url = getItem(position).getImage1();
        String img4Url = getItem(position).getImage1();*/

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.nom = (TextView) convertView.findViewById(R.id.nom_adapter_circuit);
//            holder.adresse = (TextView) convertView.findViewById(R.id.adresse_adapter_circuit);
//            holder.description = (TextView) convertView.findViewById(R.id.description_adapter_circuit);
            holder.ville = (TextView) convertView.findViewById(R.id.ville_adapter_circuit);
            holder.codePostal = (TextView) convertView.findViewById(R.id.codepostal_adapter_circuit);
            holder.image = (ImageView) convertView.findViewById(R.id.image_adapter);

            holder.prix = (TextView) convertView.findViewById(R.id.prix_adapter_circuit);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.nom.setText(nom);
//        holder.adresse.setText(adresse);
//        holder.description.setText(description);
        holder.ville.setText(ville);
        holder.codePostal.setText(codePostal);
        holder.prix.setText(prix+"€ / jour");

        ImageView img = convertView.findViewById(R.id.image_adapter);
        img.setImageBitmap(getBitmapFromBase64(mainImage));
        /*File imgFile = new  File("C:/image_projet_mobile/8/11/15.png");
        System.out.println(imgFile.exists());
        if(imgFile.exists()){

            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(imgFile));
            img.setImageBitmap(bitmap);

        }*/
        //holder.prix.setText(prix);

        //create the imageloader object


        return convertView;
    }

    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
    }

    /**
     * Required for setting up the Universal Image loader Library
     */


}