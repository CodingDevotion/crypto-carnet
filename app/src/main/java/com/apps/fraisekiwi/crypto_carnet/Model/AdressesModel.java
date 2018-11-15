package com.apps.fraisekiwi.crypto_carnet.Model;

/**
 * Created by Alexandre Chartrand on 4/10/18.
 */

// Modèle utilisé pour une adresse en particulier (Exemple : l'adresse 1 de la monnaie BTC :
// { "BTC", "Kraken", "xfGhIoMbOPKD12023023lsod"}

public class AdressesModel {

    public String symbol;
    public String nomsExchange;
    public String adressesCoin;

    public AdressesModel(String symbol, String nomsExchange, String adressesCoin){
        this.symbol=symbol;
        this.nomsExchange=nomsExchange;
        this.adressesCoin=adressesCoin;
    }
}