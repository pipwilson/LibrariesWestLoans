package philwilson.org.librarieswest;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;


public class LoginTextWatcher implements TextWatcher {
    private Activity activity;
    private Button loginButton;
    private EditText libraryCardNumberEditor;
    private EditText libraryPinEditor;

    public LoginTextWatcher(Activity activity) {
        this.activity = activity;
        this.loginButton = (Button) this.activity.findViewById(R.id.log_in_button); // feels leaky
        this.libraryCardNumberEditor = (EditText) activity.findViewById(R.id.libraryCardNumber);
        this.libraryPinEditor = (EditText) activity.findViewById(R.id.libraryPin);
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (libraryCardNumberEditor.length() > 0 && libraryPinEditor.length() > 0) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
