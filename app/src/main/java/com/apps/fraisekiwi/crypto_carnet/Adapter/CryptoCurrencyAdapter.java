package com.apps.fraisekiwi.crypto_carnet.Adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.fraisekiwi.crypto_carnet.DataBase.SettingsDataBase;
import com.apps.fraisekiwi.crypto_carnet.Model.CryptoCurrencyModel;
import com.apps.fraisekiwi.crypto_carnet.Pages.PageAdresses;
import com.apps.fraisekiwi.crypto_carnet.R;
import com.apps.fraisekiwi.crypto_carnet.Utils.MyMethods;
import com.apps.fraisekiwi.crypto_carnet.Utils.constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fahirah Diarra on 4/6/18.
 */

public class CryptoCurrencyAdapter extends RecyclerView.Adapter <CryptoCurrencyAdapter.CryptoCurrencyViewHolder>{

    private LayoutInflater inflater;
    private MyMethods methods;
    private List<CryptoCurrencyModel> cryptoCoins = Collections.emptyList();

    // Les 100 premières cryptocurrencies avec les markets caps les plus élevés seront stockés ici.
    public CryptoCurrencyAdapter(Context context){
        inflater=LayoutInflater.from(context);
    }

    @Override
    public CryptoCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.crypto_currency_layout,parent,false);
        CryptoCurrencyViewHolder holder = new CryptoCurrencyViewHolder(view);
        Context context = view.getContext();
        methods = new MyMethods(context);
        return holder;
    }

    @Override
    // L'holder permet à l'adapteur de générer le customTextView (++ pour la difficulté) des cryptocurrencies.
    public void onBindViewHolder(final CryptoCurrencyViewHolder holder, int position) {

        int nombreAdresses=0;
        final CryptoCurrencyModel currentCoin= cryptoCoins.get(position);
        try {
            nombreAdresses = methods.getNumberAdressesByCrypto(currentCoin.symbol);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Context context = holder.itemView.getContext();

        //Objet Permettant d'avoir l'etat des settings sauvegarder.

        SettingsDataBase settings = new SettingsDataBase(context);

        // Update de l'holder
        holder.coinName.setText(currentCoin.name);
        holder.coinSymbol.setText(currentCoin.symbol);
        holder.coinPrice.setText(currentCoin.price_usd);
        holder.percentChange24h.setText(currentCoin.percent_change_24h);
        holder.nbAdresse.setText(String.valueOf(nombreAdresses));

        // On met le pourcentage d'augmentation (24h) en vers s'il y a eu une hausse, et en
        // rouge, s'il y a eu une baisse.

        String colorSetting= settings.readValue("colorSettings");
        if (colorSetting.equals("color")) {
            if (holder.percentChange24h.getText().toString().contains("-")) {
                holder.percentChange24h.setTextColor(Color.parseColor("#ff0000"));
            } else {
                holder.percentChange24h.setTextColor(Color.parseColor("#32CD32"));
            }
        }

        if (!colorSetting.equals("color")){

            CardView RoundCard = (CardView)holder.itemView.findViewById(R.id.pastille);
            RoundCard.setCardBackgroundColor(Color.BLACK);
            TextView cardText = (TextView) holder.itemView.findViewById(R.id.nb_address);
            cardText.setTextColor(Color.WHITE);
        }

        // Nos image sont stockées en ligne sur un serveur personnel, afin d'éviter à l'application
        // d'avoir tous les icones de cryptocurrency en mémoire.
        Picasso.get().load(constants.imageUrl+colorSetting+"/" + currentCoin.symbol.toLowerCase() + ".png").into(holder.coinIcon);

        // Si on appuie sur une cryptocurrency, on va vers la page de ses addresses.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getAdressesIntent = new Intent(view.getContext(), PageAdresses.class);

                // On envoit les données de la premiere activite (page des prix) a la deuxieme (page des adresses)
                getAdressesIntent.putExtra("NomCrypto", holder.coinName.getText().toString());
                getAdressesIntent.putExtra("SymbolCrypto", holder.coinSymbol.getText().toString());
                getAdressesIntent.putExtra("PrixCrypto", holder.coinPrice.getText().toString());
                getAdressesIntent.putExtra("IconCrypto", constants.imageUrl+colorSetting+"/" + currentCoin.symbol.toLowerCase()+".png");

                // On lance l'activité vers la nouvelle page (Goto PageAdresses)
                view.getContext().startActivity(getAdressesIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cryptoCoins.size();
    }

    public void updateData(List<CryptoCurrencyModel> cryptoCoins) {
        this.cryptoCoins = cryptoCoins;
        notifyDataSetChanged();
    }

    // Informations du custom Textview des cryptocurrencies.
    class CryptoCurrencyViewHolder extends RecyclerView.ViewHolder{

        TextView coinName;
        TextView coinSymbol;
        TextView coinPrice;
        TextView percentChange24h;
        ImageView coinIcon;
        TextView nbAdresse;

        private CryptoCurrencyViewHolder(View itemView) {
            super(itemView);
            coinName= itemView.findViewById(R.id.coinName);
            coinSymbol= itemView.findViewById(R.id.coinSymbol);
            coinPrice= itemView.findViewById(R.id.priceUsd);
            percentChange24h= itemView.findViewById(R.id.percentChange24hText);
            coinIcon= itemView.findViewById(R.id.coinIcon);
            nbAdresse = itemView.findViewById(R.id.nb_address);

        }

    }

}
