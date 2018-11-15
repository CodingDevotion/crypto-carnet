package com.apps.fraisekiwi.crypto_carnet.Model;

/**
 * Created by Fahirah Diarra on 4/6/18.
 */

// Modèle utilisé pour un bouton de cryptocurrency (Main page) (Exemple : bouton Bitcoin:
// { "Bitcoin", "BTC", "7800usd", "-0.24%", "5"} où 5 est le nombre d'adresse de BTC dans la base de
// donnée.


public class CryptoCurrencyModel {

    public String name;
    public String symbol;
    public String price_usd;
    public String percent_change_24h;
    public int numberAdresses;

   public CryptoCurrencyModel(String name, String symbol, String price_usd,String percent_change_24h){
       this.name=name;
       this.symbol=symbol;
       this.price_usd=price_usd;
       this.percent_change_24h=percent_change_24h;
       this.numberAdresses=0;
    }
}
