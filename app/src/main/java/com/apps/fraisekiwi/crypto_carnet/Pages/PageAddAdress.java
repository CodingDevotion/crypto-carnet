package com.apps.fraisekiwi.crypto_carnet.Pages;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.apps.fraisekiwi.crypto_carnet.R;
import com.apps.fraisekiwi.crypto_carnet.Utils.MyMethods;
import com.squareup.picasso.Picasso;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

/**
 * Created by Alexandre Chartrand on 4/12/18.
 */

public class PageAddAdress extends FragmentActivity
        implements PageManuallyFragment.OnAcceptListener {

    private String string_symboleCrypto;
    private String nom_adresse = "Nom d'adresse";
    private String nouvelle_adresse = "";
    private EditText edit_adresse_field;
    private MyMethods methods;
    private Boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_adresse);

        methods = new MyMethods(getApplicationContext());

        // On prend les datas de CurrencyListActivite (MainActivite)
        Intent activityCalled = getIntent();

        // On prend les informations de la page des cryptocurrencies selon la crypto
        // choisie
        String string_nomCrypto = activityCalled.getExtras().getString("NomCrypto");
        String string_lienImageCrypto = activityCalled.getExtras().getString("IconCrypto");
        string_symboleCrypto = activityCalled.getExtras().getString("SymbolCrypto");

        TextView textView_nomCrypto = findViewById(R.id.page_add_adresses_crypto_name);
        ImageView imageView_iconCrypto = findViewById(R.id.page_add_adresses_crypto_icon);

        Picasso.get().load(string_lienImageCrypto).into(imageView_iconCrypto);
        textView_nomCrypto.setText(string_nomCrypto);

        Button boutonEnterManually = findViewById(R.id.button_enter_manually);
        Button bouton_paste_from_clipboard = findViewById(R.id.button_paste_from_clipboard);
        final Button boutonAdd = findViewById(R.id.save_address_added);


        // Listener qui lance le paste du clipboard en appuyant sur un bouton.
        // Après avoir appuyé, un Toast apparaît pour montrer que le paste a bien fonctionné.
        // (++ Feedback).

        bouton_paste_from_clipboard.setOnClickListener(v -> {
            String from_clipboard = getStringFromClipboard();
            onAccept(from_clipboard);
            Toast.makeText(getApplicationContext(), "Pasted from clipboard", Toast.LENGTH_LONG).show();
        });

        boutonEnterManually.setOnClickListener(v -> showNoticeDialog());

        boutonAdd.setOnClickListener(v -> {
        Toast.makeText(getApplicationContext(), "Adress added", Toast.LENGTH_SHORT).show();
            saveChangesToDB();
            finish();
        });

        boutonAdd.setClickable(false);
    }


    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new PageManuallyFragment();
        dialog.show(getFragmentManager(), "Adding Manually");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onAccept(String address_out) {
        nouvelle_adresse = address_out;
        edit_adresse_field = findViewById(R.id.address_new_name);
        TextView textView_adresse = findViewById(R.id.address_added);
        String a = edit_adresse_field.getText().toString();

        //met à jour les deux champs
        if (!nouvelle_adresse.equals("")){
            textView_adresse.setText(nouvelle_adresse);
            // default name uniqement l'utilisateur n'en a pas entré un
            if (nom_adresse.equals("Nom d'adresse")) {
                String nouveauNom = methods.defaultAddressName(nouvelle_adresse);
                edit_adresse_field.setText(nouveauNom);
                nom_adresse = nouveauNom;
                changed = true;
                addresseSavable();
        }}
    }

    private void addresseSavable(){
        Button boutonAdd = findViewById(R.id.save_address_added);
        boutonAdd.setBackground(getResources().getDrawable( R.drawable.btn_border_save ));
        boutonAdd.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable( R.drawable.ic_check ), null, null, null);
        findViewById(R.id.save_address_added).setClickable(true);
    }

    private void saveChangesToDB(){
        if (!nouvelle_adresse.equals("") && string_symboleCrypto !=null && nouvelle_adresse !=null && nom_adresse !=null){
             methods.adressToDataBase(string_symboleCrypto, nouvelle_adresse, edit_adresse_field.getText().toString() );
    }}


    // Warning si des changements ne sont pas modifiés, on mentionne à l'utilisateur que les changements
    // ne seront pas sauvegardé.
    // (++ Feedback)

    @Override
    public void onBackPressed() {
        if (changed || !nom_adresse.equals("Nom d'adresse")) {
            new AlertDialog.Builder(this)
                    .setMessage("Changes will not take effect")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }else{
            super.onBackPressed();
        }
    }


    // Permet de retourner en String le contenu du clipboard du cellulaire android.
    // Tiré du site https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
    // et adapté par Alexandre Chartrand, afin de fonctionner correctement avec notre application.

    public String getStringFromClipboard(){

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";

        // If it does contain data, decide if you can handle the data.
        assert clipboard != null;
        if (!(clipboard.hasPrimaryClip())) {

        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

            // Si l'information n'est pas du texte, on ne paste rien.

        } else {

            //Si l'information du clipboard est du texte, on le retourne.
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            pasteData = item.getText().toString();
        }
        return pasteData;
    }

    // Goto PageOpenQrCodeScanner
    public void openQrCodeRetreive(View view) {
        Intent getOpenQrCodeRetreiveIntent = new Intent(this, QrCode.class);
        view.getContext().startActivity(getOpenQrCodeRetreiveIntent);
    }
}
