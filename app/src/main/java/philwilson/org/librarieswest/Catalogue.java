package philwilson.org.librarieswest;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Random;
import java.util.UUID;

/**
 * API calls for the CatalogueService endpoint
 */
public class Catalogue {

    private static String APP_ID = "e89f85e6-c6d5-47d0-a4e2-a3da0355f8e6";
    private static String DEVICE_ID = UUID.randomUUID().toString();
    private static String CATALOGUE_SERVICE_URL = "https://m.solus.co.uk/catalogue/CatService.asmx/";

    @NonNull
    String getLoginUrl() {
        Random random = new Random();
        return new StringBuilder(CATALOGUE_SERVICE_URL)
                .append("CatLogin?rnd=")
                .append(random.nextInt())
                .toString();
    }

    JSONObject getLoginData(int libraryCardId, int libraryPin) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("appid", APP_ID);
        data.put("udid", DEVICE_ID);
        data.put("account", libraryCardId);
        data.put("PIN", libraryPin);
        System.out.println(data.toString());
        return data;
    }

    public boolean login(int libraryCardId, int libraryPin) {

        getLoginUrl();

        return false;
    }
}
