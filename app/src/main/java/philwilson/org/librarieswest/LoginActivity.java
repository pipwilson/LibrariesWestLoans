package philwilson.org.librarieswest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Preferences name
    public static final String PREFS_NAME = "LibrariesWestPreferences";

    // UI references.
    EditText mLibraryCardNumberEditor;
    EditText mLibraryPinEditor;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        final Button mLogInButton = (Button) findViewById(R.id.log_in_button);
        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        LoginTextWatcher loginTextWatcher = new LoginTextWatcher(this);

        mLibraryCardNumberEditor = (EditText) findViewById(R.id.libraryCardNumber);
        mLibraryCardNumberEditor.addTextChangedListener(loginTextWatcher);

        mLibraryPinEditor = (EditText) findViewById(R.id.libraryPin);
        mLibraryPinEditor.addTextChangedListener(loginTextWatcher);

        populateEditorsFromPreferences();

        mLibraryPinEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void populateEditorsFromPreferences() {
        //getLoaderManager().initLoader(0, null, this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String libraryCardNumber = settings.getString("libraryCardNumber", "");
        String libraryPin = settings.getString("libraryPin", "");
        this.mLibraryCardNumberEditor.setText(libraryCardNumber);
        this.mLibraryPinEditor.setText(libraryPin);
    }

    private void saveLoginDetailsToPreferences() {
        // Save preferences.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("libraryCardNumber", mLibraryCardNumberEditor.getText().toString());
        editor.putString("libraryPin", mLibraryPinEditor.getText().toString());

        // Commit the edits!
        editor.commit();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        saveLoginDetailsToPreferences();

        // Reset errors.
        mLibraryCardNumberEditor.setError(null);
        mLibraryPinEditor.setError(null);

        // Store values at the time of the login attempt.
        String libraryCardNumber = mLibraryCardNumberEditor.getText().toString();
        int libraryPin = new Integer(mLibraryPinEditor.getText().toString()).intValue();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isLibraryPinValid(libraryPin)) {
            mLibraryPinEditor.setError(getString(R.string.error_invalid_password));
            focusView = mLibraryPinEditor;
            cancel = true;
        }

        // Check for a valid library card ID.
        if (!isLibraryCardNumberValid(libraryCardNumber)) {
            mLibraryCardNumberEditor.setError(getString(R.string.error_invalid_email));
            focusView = mLibraryCardNumberEditor;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(this, libraryCardNumber, libraryPin);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isLibraryCardNumberValid(String libraryCardNumber) {
        return true;
        //TODO regex to ensure it only contains numbers
        //return (libraryCardNumber == (int) libraryCardNumber);
    }

    private boolean isLibraryPinValid(int pin) {
        return (pin == (int) pin && (pin >= 1000));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        Activity mActivity;
        private final String mLibraryCardId;
        private final int mLibraryPin;

        UserLoginTask(Activity activity, String libraryCardId, int libraryPin) {
            mActivity = activity;
            mLibraryCardId = libraryCardId;
            mLibraryPin = libraryPin;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Catalogue catalogue = new Catalogue(mActivity);
            return catalogue.login(mLibraryCardId, mLibraryPin);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                mActivity.startActivity(new Intent(mActivity, BookListActivity.class));
            } else {
                mLibraryPinEditor.setError(getString(R.string.error_incorrect_password));
                mLibraryPinEditor.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

