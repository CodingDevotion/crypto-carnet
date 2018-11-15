package com.apps.fraisekiwi.crypto_carnet.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by Alexandre Chartrand on 4/11/18.
 */

// Classe initialisant toutes les méthodes statiques de l'application, nécessitant un contexte.
// Permet d'accéder à ces méthodes dans n'importe quelle activité.

public class MyMethods {

    private Context context;

    public MyMethods(Context context){
        this.context = context;
    }

    // Ajoute une addresse dans la database afin de sauvegarder de façon permanente les données
    // de l'utilisateur.
    // (la database est un fichier string contenu dans le téléphone

    public void adressToDataBase(String symbol, String adresse, String surnom){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.context.openFileOutput("alladresses.txt", Context.MODE_APPEND));
            outputStreamWriter.write(symbol+" "+adresse+" "+surnom+"\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    // Supprime toutes la database
    // Utile pour le Debugging et afin que l'utilisateur puisse supprimer toutes ses données
    // à partir de la page Settings.

    public void deleteAllDatabase() throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.context.openFileOutput("alladresses.txt", Context.MODE_PRIVATE));
        File file = new File(String.valueOf(outputStreamWriter));
        boolean deleted = file.delete();
    }



    // Fonction qui retourne le nombre d'addresse de l'utilisateur par cryptocurreny.
    // Ex: getNumberAdressesByCrypto("BTC") retourne 3 si l'utilisateur a 3 addresses de Bitcoin
    // d'enregistré.

    public int getNumberAdressesByCrypto(String symbol) throws IOException {

        int nombreAdresse = 0;
        Scanner sc = null;


        try {
            sc = new Scanner(new BufferedReader(new InputStreamReader(context.openFileInput("alladresses.txt"))));
        } catch (FileNotFoundException e) {
            File file = new File("alladresses.txt");
            sc = new Scanner(new BufferedReader(new InputStreamReader(context.openFileInput("alladresses.txt"))));

        }

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] words = line.split(" ");

            if (symbol.equals(words[0])) {
                nombreAdresse++;
            }
        }
        return nombreAdresse;
    }

    // Fonction qui génère un nom d'addresse par défaut si l'utilisateur n'en met pas.
    public String defaultAddressName(String address){
        return address.substring(0,3)+"•••"+address.substring(address.length()-3,address.length());
    }
}
