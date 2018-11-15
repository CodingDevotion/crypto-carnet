package com.apps.fraisekiwi.crypto_carnet.DataBase;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fahirah Diarra on 4/6/18.
 */

public class SettingsDataBase extends Activity{

    public String settingsfile = "/settings.json";
    public ObjectMapper mapper = new ObjectMapper();
    public Map<String,Object> map = new HashMap<String, Object>();
    private Context context;

    public SettingsDataBase(Context context){

        this.context=context;
    }

    public void initSettings(){
        if (readValue("colorSettings")== null){
        putValue("colorSettings","color");
        }

    }


    public void putValue(String key ,String value) {
        try {
            map.put(key, value);
            Log.d("outch",map.toString());
            Log.d("outch",mapper.writeValueAsString(map).toString());
            mapper.writeValue(new File(context.getFilesDir(),settingsfile),map);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readValue(String key){

        String obtainedKey= "";
        try {
            Map<String, Object> tempMap = mapper.readValue(
                    new File(context.getFilesDir(),settingsfile),
                    new TypeReference<Map<String, Object>>() {

                    });
            try {
                obtainedKey=tempMap.get(key).toString();
            }
            catch (NullPointerException e){
                Log.d("yeah",obtainedKey);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return obtainedKey;

    }


}