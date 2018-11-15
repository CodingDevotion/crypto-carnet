package com.apps.fraisekiwi.crypto_carnet.Adapter;
import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.fraisekiwi.crypto_carnet.Model.AdressesModel;
import com.apps.fraisekiwi.crypto_carnet.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Alexandre Chartrand on 4/6/18.
 */

// Un deuxième adapteur pour générer le deuxième custom Textview de notre application :
// les addresses de chaque cryptocurrency
public class AdressesAdapter extends RecyclerView.Adapter <AdressesAdapter.AdressesViewHolder>{

    private LayoutInflater adressesInflater;
    // Les addresses de chaque cryptocurrencies seront stockés ici.
    private List<AdressesModel> adressesCoins = Collections.emptyList();

    public AdressesAdapter(Context context, List<AdressesModel> adressesCoins){
        this.adressesInflater=LayoutInflater.from(context);
        this.adressesCoins = adressesCoins;
    }

    @Override
    public AdressesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = adressesInflater.inflate(R.layout.address_card, parent, false);
        AdressesViewHolder adressesHolder = new AdressesViewHolder(view);
        return adressesHolder;
    }

    @Override
    public void onBindViewHolder(final AdressesViewHolder adressesholder, int position) {
        final AdressesModel currentAdress = adressesCoins.get(position);
        adressesholder.exchangeName.setText(currentAdress.nomsExchange);
        adressesholder.cryptoAdress.setText(currentAdress.adressesCoin);
    }

    @Override
    public int getItemCount() {
        return adressesCoins.size();
    }

    public void updateData(List<AdressesModel> adressesCoins) {
        this.adressesCoins = adressesCoins;
        notifyDataSetChanged();
    }


    // Informations du custom Textview des adresses de chaque cryptocurrency
    class AdressesViewHolder extends RecyclerView.ViewHolder{

        TextView exchangeName;
        TextView cryptoAdress;
        AppCompatImageButton copyToClipboard;
        AppCompatImageButton qrCode;
        AppCompatImageButton share;


        private AdressesViewHolder(View itemView) {
            super(itemView);
            exchangeName = itemView.findViewById(R.id.add_card_name);
            cryptoAdress = itemView.findViewById(R.id.crypto_adresse);
            copyToClipboard = itemView.findViewById(R.id.copy_to_clipboard_button);
            qrCode = itemView.findViewById(R.id.qr_code_button);
            share = itemView.findViewById(R.id.share_button);
        }
    }
}
