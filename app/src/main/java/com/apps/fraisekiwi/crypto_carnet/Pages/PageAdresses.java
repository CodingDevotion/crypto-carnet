package com.apps.fraisekiwi.crypto_carnet.Pages;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.apps.fraisekiwi.crypto_carnet.Adapter.AdressesAdapter;
import com.apps.fraisekiwi.crypto_carnet.DataBase.SettingsDataBase;
import com.apps.fraisekiwi.crypto_carnet.Model.AdressesModel;
import com.apps.fraisekiwi.crypto_carnet.R;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Alexandre Chartrand on 4/12/18.
 * Manage and display an address list (custom TextView).
 */


public class PageAdresses extends AppCompatActivity {

    // Base de données des adresses. On crée une base de donnée qui est en fait un dictionnaire,
    // où la key est le symbole de la cryptocurrency (ex: BTC) et les valeurs, un array de toutes
    // les adresses de la cryptocurrency.
    // Ex {BTC, [adresse1, adresse2, adresse3, etc]

    // Liste des addresses vériatables
    protected Map<String, ArrayList<String>> listeAdresseParCrypto = new HashMap<String, ArrayList<String>>();

    // Liste des surnoms donnés aux addresses
    protected Map<String, ArrayList<String>> listeSurnomParCrypto = new HashMap<String, ArrayList<String>>();

    protected String string_nomCrypto;
    protected String string_lienImageCrypto;
    protected String string_symbolCrypto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPageAdresses();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPageAdresses();
    }


    /*  Permet, en appuyant sur le bouton copyToClipBoard, de copier l'adresse d'une Crypto dans
        le clipboard du cellulaire. On envoie ensuite un Toast comme Feedback pour montrer à
        l'utilisateur qu'il a bien copié l'adresse.
     */
    public void copyToClipBoard(View view) {

        Context context = getApplicationContext();

        // Les 3 premières lignes du code sont empruntées de Stackover, et adaptées par Alexandre Chartrand.
        // J'ai modifié tout le reste pour y ajouter plus de feedback à l'app (Un Toast).
        // Source : https://stackoverflow.com/questions/19253786/how-to-copy-text-to-clip-board-in-android
        // (++ Feedback)

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        TextView cryptoAdresse = findViewById(R.id.crypto_adresse);
        String stringAdress = cryptoAdresse.getText().toString();
        ClipData clip = ClipData.newPlainText("AuClipBoard", stringAdress);

        assert clipboard != null;
        clipboard.setPrimaryClip(clip);

        String toastCopiedToClipBoard = "Copied to clipboard !";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, toastCopiedToClipBoard, duration);
        toast.show();
    }

    // Lancement du bouton Share (pour share l'adresse à quelqu'un d'autre (par exemple, par email).
    public void shareContent(View view) {

        TextView cryptoAdresse = findViewById(R.id.crypto_adresse);
        String stringAdress = cryptoAdresse.getText().toString();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        // Source : Send Text Content de la documentation android Officielle
        // (https://developer.android.com/training/sharing/send.html)
        // Adapté par Alexandre Chartrand

        sendIntent.putExtra(Intent.EXTRA_TEXT, stringAdress);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    // Lance la génération d'un QrCode à partir d'un String (l'adresse qu'on veut envoyer)
    public void generateQrCode(View view){

        TextView cryptoAdresse = findViewById(R.id.crypto_adresse);
        String stringAdress = cryptoAdresse.getText().toString();

        Intent printQrCode = new Intent(this, PageGenerateQrCodeFromString.class);
        printQrCode.putExtra("stringToTransform",stringAdress);
        startActivity(printQrCode);
    }

    // Goto PageAddAdress
    public void goToPageAddAdress(View view) {

        Intent putAddAdressIntent = new Intent(view.getContext(), PageAddAdress.class);

        // On passe les informations voulues
        putAddAdressIntent.putExtra("NomCrypto", string_nomCrypto);
        putAddAdressIntent.putExtra("IconCrypto", string_lienImageCrypto);
        putAddAdressIntent.putExtra("SymbolCrypto", string_symbolCrypto);

        view.getContext().startActivity(putAddAdressIntent);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    // Fonction qui prend toutes les données de mon fichier database, et insere tous les éléments
    // dans un Hashmap.
    // Un hashmap permet la redondance de code inutile, car on retrouve l'index BTC associé à un
    // tableau dynamique contenant toutes les addresses de Bitcoin de l'utilisateur.
    // Nous n'avons donc qu'un seul index par cryptocurrency.
    // (++ Difficulté)

    public void databaseToArray(String symbol, Map<String, ArrayList<String>> listeAdresseParCrypto, Map<String, ArrayList<String>> listeSurnomParCrypto) throws FileNotFoundException {
        Scanner sc;

        ArrayList<String> contenuAdresse = new ArrayList<String>();
        ArrayList<String> contenuSurnom = new ArrayList<String>();

        InputStream inputStream = getApplicationContext().openFileInput("alladresses.txt");
        sc = new Scanner(new BufferedReader(new InputStreamReader(inputStream)));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] words = line.split(" ");

            if (symbol.equals(words[0])) {
                contenuAdresse.add(words[2]);
                contenuSurnom.add(words[1]);
            }
            listeAdresseParCrypto.put(words[0], contenuSurnom);
            listeSurnomParCrypto.put(words[0], contenuAdresse);
        }
    }


    // On place les addresses des Textviews dans la page Android.
    // Custom TextView + Holder + RecycleView + Custom Adapter + Model
    // (++ Difficulté)

    public void loadPageAdresses(){
        setContentView(R.layout.address_list_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        RecyclerView adressesRecyclerView = findViewById(R.id.adressesRecyclerView);

        List<AdressesModel> addressList = new ArrayList<>();
        AdressesAdapter adapter = new AdressesAdapter(this, addressList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        adressesRecyclerView.setLayoutManager(mLayoutManager);
        adressesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adressesRecyclerView.setAdapter(adapter);

        // On prend les datas de CurrencyListActivite (MainActivite)
        Intent activityCalled = getIntent();

        SettingsDataBase settings = new SettingsDataBase(getApplicationContext());
        String colorSetting= settings.readValue("colorSettings");
        // On se get les informations de la page des cryptocurrencies selon la crypto
        // choisie

        string_nomCrypto = activityCalled.getExtras().getString("NomCrypto");
        string_symbolCrypto = activityCalled.getExtras().getString("SymbolCrypto");
        string_lienImageCrypto = activityCalled.getExtras().getString("IconCrypto");

        TextView textView_nomCrypto = findViewById(R.id.page_adresses_crypto_name);
        ImageView imageView_iconCrypto = findViewById(R.id.page_adresses_crypto_icon);
        TextView aucuneAdresse = findViewById(R.id.aucune_adresse);


        Picasso.get().load(string_lienImageCrypto).into(imageView_iconCrypto);
        textView_nomCrypto.setText(string_nomCrypto);


        List<AdressesModel> keepingAdressesCoin = new ArrayList<>();
        try {
            databaseToArray(this.string_symbolCrypto, this.listeAdresseParCrypto, this.listeSurnomParCrypto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        List<String> adresseDUneCrypto = this.listeAdresseParCrypto.get(this.string_symbolCrypto.toUpperCase());
        List<String> surnomDUneCrypto = this.listeSurnomParCrypto.get(this.string_symbolCrypto.toUpperCase());


        if (adresseDUneCrypto != null) {
            keepingAdressesCoin = new ArrayList<>();

            for (int i = 0; i < adresseDUneCrypto.size(); i++) {
                AdressesModel adresseAjoutee = new AdressesModel(this.string_symbolCrypto, surnomDUneCrypto.get(i), adresseDUneCrypto.get(i));
                keepingAdressesCoin.add(adresseAjoutee);
            }

        // Si on a aucune addresse d'enregistré, on inscrit un message à l'écran insitant l'utilisateur
        // à inscrire une nouvelle addresse
        // (++ Feedback)

            aucuneAdresse.setVisibility(View.INVISIBLE);
            adapter.updateData(keepingAdressesCoin);
        } else {
            aucuneAdresse.setVisibility(View.VISIBLE);
        }

        // On update les données à l'aide de l'adapteur.
        adapter.updateData(keepingAdressesCoin);

    }
}