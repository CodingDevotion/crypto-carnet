package com.apps.fraisekiwi.crypto_carnet.Network;

import com.apps.fraisekiwi.crypto_carnet.Model.CryptoCurrencyModel;
import com.apps.fraisekiwi.crypto_carnet.Utils.constants;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Fahirah Diarra on 4/7/18.
 */

// Classe nécessaire pour trouver les prix des cryptocurrencies en lignes. Nous avons parsé le tout
// manuellement (++ difficulté).

public interface CryptoAPi {

    String BASE_URL = constants.apiUrl;

    @GET
    Call<List<CryptoCurrencyModel>> getCryptoCurrencyModel(@Url String url);
}
