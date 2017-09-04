package br.ufc.quixada.backontrack.activities;

        import android.os.Bundle;
        import android.os.PersistableBundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import br.ufc.quixada.backontrack.R;

/**
 * Created by samue on 03/09/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText loginField;
    private EditText passwordField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        loginField = (EditText) findViewById(R.id.edit_login_loginField);
        passwordField = (EditText) findViewById(R.id.edit_login_passwordField);
    }

    private void login(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginField.getText().toString();
                String password = passwordField.getText().toString();
            }
        });
    }


}
