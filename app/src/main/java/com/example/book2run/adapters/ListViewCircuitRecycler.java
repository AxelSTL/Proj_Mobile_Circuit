package com.example.book2run.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
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
import com.example.book2run.ReserveActivity;
import com.example.book2run.model.Circuit;
import com.example.book2run.ui.addcircuit.AddNameActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class  ListViewCircuitRecycler  extends RecyclerView.Adapter<ListViewCircuitRecycler.ViewHolder>{

    private List<Circuit> circuits;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private boolean isMyCircuit;

    // data is passed into the constructor
    public ListViewCircuitRecycler(Context context, List<Circuit> circuits, boolean isMyCircuit) {
        this.mInflater = LayoutInflater.from(context);
        this.circuits = circuits;
        this.context = context;
        this.isMyCircuit = isMyCircuit;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate((isMyCircuit) ? R.layout.recycler_view_mycircuit_layout : R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Circuit circuit = circuits.get(position);


        holder.prix.setText(isMyCircuit ? String.valueOf(circuit.getPrice()) + "€" : "Montant réglé : " + String.valueOf(circuit.getPrice()) + "€");
        String circuitNom = circuit.getNom().substring(0, 1).toUpperCase() + circuit.getNom().substring(1).toLowerCase();
        holder.nom.setText(circuitNom);
        holder.image.setImageBitmap(getBitmapFromBase64(circuit.getMainImg()));
        if(!isMyCircuit) holder.date.setText("Arrivé : " + circuit.getDateDebut()+ "      Départ : " + circuit.getDateFin());
        holder.code.setText(String.valueOf(circuit.getCode()));
        holder.codeResa.setText(String.valueOf(circuit.getCodeResa()));
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
        TextView prix, nom, date,code, codeResa;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_recycler);
            prix = itemView.findViewById(R.id.prix_recycler);
            nom = itemView.findViewById(R.id.name_recycler);
            date = itemView.findViewById(R.id.date_recycler);
            code = itemView.findViewById(R.id.code_txtview);
            codeResa = itemView.findViewById(R.id.codeResa_txtview);

            //myTextView = itemView.findViewById(R.id.tvAnimalName);
            if(isMyCircuit) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        PopupMenu circuitSettings = new PopupMenu(context, v);
                        circuitSettings.getMenuInflater().inflate(R.menu.circuitsetting_menu, circuitSettings.getMenu());
                        circuitSettings.show();
                        circuitSettings.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()){
                                    case R.id.deleteCircuit:
                                        new AlertDialog.Builder(context)
                                                .setTitle("Voulez vous vraiment supprimer votre annonce ?")
                                                .setMessage("Votre annonce sera dÃ©finitivement supprimer")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        try {
                                                            deleteCircuit(Integer.parseInt((String) code.getText()));
                                                            Intent intentDelete = new Intent(context, MainActivity.class);
                                                            v.getContext().startActivity(intentDelete);;
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }})
                                                .setNegativeButton(android.R.string.no, null).show();
                                        break;
                                        case R.id.modifyCircuit:
                                            Intent intentModify = new Intent(context, AddNameActivity.class);
                                            intentModify.putExtra("code",Integer.parseInt((String) code.getText()));
                                            intentModify.putExtra("isModify", true);
                                            v.getContext().startActivity(intentModify);;
                                            break;
                                            case R.id.seeCircuit:
                                                Intent intent = new Intent(context, CircuitViewActivity.class);
                                                intent.putExtra("code",Integer.parseInt((String) code.getText()));
                                                intent.putExtra("isMine", "true");
                                                v.getContext().startActivity(intent);;
                                                break;
                                }
                                return false;
                            }
                        });
                        return false;
                    }
                });


            } else {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        PopupMenu reservation = new PopupMenu(context, v);
                        reservation.getMenuInflater().inflate(R.menu.reservation_menu, reservation.getMenu());
                        reservation.show();
                        reservation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.cancel_reservationmenu:
                                        new AlertDialog.Builder(context)
                                                .setTitle("Voulez vous vraiment annuler votre reservation ?")
                                                .setMessage("Votre reservation sera définitivement annuler")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        try {
                                                            cancelReservation((String) codeResa.getText());
                                                            //TODO REFRESH LE BAIL CHACAL
                                                        } catch (IOException | JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null).show();
                                        break;
                                    case R.id.modify_reservationmenu:
                                        Intent intent = new Intent(context, ReserveActivity.class);
                                        intent.putExtra("codeCircuit", code.getText());
                                        intent.putExtra("modifyResa", true);
                                        intent.putExtra("codeResa", codeResa.getText());
                                        v.getContext().startActivity(intent);
                                        break;
                                }
                                return false;
                            }
                        });
                        return false;
                    }
                });
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context, CircuitViewActivity.class);
            intent.putExtra("code",Integer.parseInt((String) code.getText()));
            intent.putExtra("prixResa", prix.getText());
            intent.putExtra("isMine", "true");
            intent.putExtra("resa", "true");
            if(!isMyCircuit) intent.putExtra("date", date.getText());
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

    public void cancelReservation(String code) throws IOException, JSONException {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        String requestURL = "http://192.168.2.169:8180/reservation";
        URL url = new URL(requestURL);
        System.out.println("code de la resa a delete : " + code);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        JSONObject id = new JSONObject();
        id.put("code", code);
        writer.write(id.toString());
        writer.flush();
        writer.close();

        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

    }

    public void deleteCircuit(int code) throws IOException, JSONException {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        String requestURL = "http://192.168.2.169:8180/circuits";
        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

        JSONObject circuitDelete = new JSONObject();
        circuitDelete.put("code", code);

        Log.i("circuitDelete",circuitDelete.toString());
        writer.write(circuitDelete.toString());
        writer.flush();
        writer.close();

        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

    }

    /**
     * Required for setting up the Universal Image loader Library
     */
}





















