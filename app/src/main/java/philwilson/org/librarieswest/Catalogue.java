package philwilson.org.librarieswest;

import android.support.annotation.NonNull;

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
    private String getLoginUrl() {
        return new StringBuilder(CATALOGUE_SERVICE_URL)
                .append("CatLogin?rnd=")
                .append(Math.random())
                .toString();
    }

    public boolean login(int libraryCardId, int libraryPin) {
        getLoginUrl();
        return false;
    }
}
