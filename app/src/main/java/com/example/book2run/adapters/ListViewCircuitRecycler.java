package com.example.book2run.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book2run.CircuitViewActivity;
import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.model.Circuit;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ListViewCircuitRecycler  extends RecyclerView.Adapter<ListViewCircuitRecycler.ViewHolder>{

    private List<Circuit> circuits;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public ListViewCircuitRecycler(Context context, List<Circuit> circuits) {
        this.mInflater = LayoutInflater.from(context);
        this.circuits = circuits;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Circuit circuit = circuits.get(position);


        holder.prix.setText(String.valueOf(circuit.getPrice()));
        holder.nom.setText(circuit.getNom());
        holder.image.setImageBitmap(getBitmapFromBase64(circuit.getMainImg()));
        holder.date.setText("Arrivé : " + circuit.getDateDebut()+ "      Départ : " + circuit.getDateFin());
        holder.code.setText(String.valueOf(circuit.getCode()));
       // holder.myTextView.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return circuits.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView prix, nom, date,code;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_recycler);
            prix = itemView.findViewById(R.id.prix_recycler);
            nom = itemView.findViewById(R.id.name_recycler);
            date = itemView.findViewById(R.id.date_recycler);
            code = itemView.findViewById(R.id.code_txtview);

            //myTextView = itemView.findViewById(R.id.tvAnimalName);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu reservation = new PopupMenu(context, v);
                    reservation.getMenuInflater().inflate(R.menu.reservation_menu, reservation.getMenu());
                    reservation.show();
                    reservation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.cancel_reservationmenu:
                                    new AlertDialog.Builder(context)
                                            .setTitle("Voulez vous vraiment annuler votre reservation ?")
                                            .setMessage("Votre reservation sera définitivement annuler")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    //cancelReservation(circuits.get(whichButton).getCode());
                                                }})
                                            .setNegativeButton(android.R.string.no, null).show();
                                    break;
                                case R.id.modify_reservationmenu:
                                    break;
                            }
                            return false;
                        }
                    });
                    return false;
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context, CircuitViewActivity.class);
            intent.putExtra("code",Integer.parseInt((String) code.getText()));
            intent.putExtra("prixResa", prix.getText());
            intent.putExtra("isMine", "true");
            intent.putExtra("resa", "true");
            intent.putExtra("date", date.getText());
            view.getContext().startActivity(intent);
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }


    }

    // convenience method for getting data at click position
    public Circuit getItem(int id) {
        return circuits.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
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





















