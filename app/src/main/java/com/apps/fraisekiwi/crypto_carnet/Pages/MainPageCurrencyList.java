package com.apps.fraisekiwi.crypto_carnet.Pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.apps.fraisekiwi.crypto_carnet.DataBase.SettingsDataBase;
import com.apps.fraisekiwi.crypto_carnet.Model.CryptoCurrencyModel;
import com.apps.fraisekiwi.crypto_carnet.Network.CryptoAPi;
import com.apps.fraisekiwi.crypto_carnet.Utils.constants;
import com.apps.fraisekiwi.crypto_carnet.Adapter.CryptoCurrencyAdapter;
import com.apps.fraisekiwi.crypto_carnet.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Fahirah Diarra on 4/10/18.
 */

public class MainPageCurrencyList extends AppCompatActivity {

    // Les cryptocurrencies affichées dans l'application sont ici. Si on en ajoute ou supprime,
    // l'application s'ajuste automatiquement.

    public static final String[] idCryptoQuOnGarde = {"BTC", "ETH", "LTC", "ZEC", "XMR", "DASH", "LRC"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivity();
    }

    private void mainActivity() {

        SettingsDataBase settings = new SettingsDataBase(getApplicationContext());
        settings.initSettings();

        setContentView(R.layout.currency_list_activity);

        TextView networkMsg = findViewById(R.id.aucun_network);
        RecyclerView cryptoRecyclerView = findViewById(R.id.cryptoRecyclerView);
        cryptoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CryptoCurrencyAdapter adapter = new CryptoCurrencyAdapter(this);
        cryptoRecyclerView.setAdapter(adapter);
        networkMsg.setVisibility(View.INVISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(constants.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CryptoAPi client = retrofit.create(CryptoAPi.class);

        retrofit2.Call<List<CryptoCurrencyModel>> call = client.getCryptoCurrencyModel("?convert=USD&limit=100");

        call.enqueue(new retrofit2.Callback<List<CryptoCurrencyModel>>() {

            @Override
            public void onResponse(retrofit2.Call<List<CryptoCurrencyModel>> call, retrofit2.Response<List<CryptoCurrencyModel>> response) {

                List<CryptoCurrencyModel> cryptoCoins = response.body();
                List<CryptoCurrencyModel> keepingCryptoCoins = new ArrayList<>();

                // On compare le symbole des cryptocurrencies de notre liste avec les symboles de toutes
                // les cryptocurrencies de l'api, en ligne.
                // On prend ainsi les informations des seules cryptocurrencies que nous désirons.

                for (int i = 0; i < cryptoCoins.size(); i++) {
                    for (String anIdCryptoQuOnGarde : idCryptoQuOnGarde) {
                        if (cryptoCoins.get(i).symbol.equals(anIdCryptoQuOnGarde)) {
                            keepingCryptoCoins.add(cryptoCoins.get(i));
                        }
                    }
                }

                // On update l'affichage à l'écran à l'aide de l'adapter.
                adapter.updateData(keepingCryptoCoins);
            }

            @Override
            public void onFailure(retrofit2.Call<List<CryptoCurrencyModel>> call, Throwable t) {
                buildDialog(MainPageCurrencyList.this).show();
                TextView networkMsg = findViewById(R.id.aucun_network);
                networkMsg.setVisibility(View.VISIBLE);
            }
        });
    }

    // Goto PageSettings
    public void openSettingsPage(View view) {
        Intent getSettingsIntent = new Intent(this, PageSettings.class);
        view.getContext().startActivity(getSettingsIntent);
    }


    // L'application nécessite internet. On affiche un message d'erreur si on n'a pas de
    // connection internet, et on ferme l'application.
    // (++ Feedback)
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please try later");
        builder.setPositiveButton("Ok", (dialog, which) -> finish());
        return builder;
    }
}
