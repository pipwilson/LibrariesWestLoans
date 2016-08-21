package philwilson.org.librarieswest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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
    private static String DEVICE_ID = "0f248feb-7c64-4123-b6ca-c233e7aeb182"; // fictional
    //UUID.randomUUID().toString();
    private static String CATALOGUE_SERVICE_URL = "https://m.solus.co.uk/catalogue/CatService.asmx/";
    private final Activity mActivity;

    public static String getAppId() {
        return APP_ID;
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public Catalogue(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    String getLoginUrl() {
        Random random = new Random();
        return new StringBuilder(CATALOGUE_SERVICE_URL)
                .append("CatLogin?rnd=")
                .append(random.nextInt())
                .toString();
    }

    String getLoginData(String libraryCardId, int libraryPin) {
        JSONObject data = new JSONObject();
        try {
            data.put("appid", APP_ID);
            data.put("udid", DEVICE_ID);
            data.put("account", libraryCardId);
            data.put("PIN", libraryPin);
        } catch (JSONException jsonException) {
            Log.e("Error with Login data", jsonException.getMessage());
        }
        Log.i("login.data", data.toString());
        return data.toString();
    }

    public boolean login(String libraryCardId, int libraryPin) {

        String loginUrl = getLoginUrl();
        String data = null;

        data = getLoginData(libraryCardId, libraryPin);

        if (data == null) {
            return false;
        }

        return sendCredentials(data);
        //return true;

    }

    private boolean sendCredentials(String loginData) {
        final String data = loginData;
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        String url = this.getLoginUrl();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                createSuccessfulRequstListener(), createErrorRequestListener()) {

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return data.getBytes();
            }

        };

        // Add the request to the RequestQueue.
        // TODO: deal with timeouts
        queue.add(stringRequest);
        return true;
    }

    private Response.Listener<String> createSuccessfulRequstListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // happy state
                Log.i("login.success", response);
            }
        };
    }

    private Response.ErrorListener createErrorRequestListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // sad state
                Log.w("login.error", error.toString());
                Log.w("login.error", new Integer(error.networkResponse.statusCode).toString());
            }
        };
    }


}
