package rau.sebastian.almacenapp_sebastian.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rau.sebastian.almacenapp_sebastian.R;
import rau.sebastian.almacenapp_sebastian.interfaces.ApiService;
import rau.sebastian.almacenapp_sebastian.models.ResponseMessage;
import rau.sebastian.almacenapp_sebastian.models.Usuario;
import rau.sebastian.almacenapp_sebastian.singletons.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);

    }



    public void callLogin(View view) {
        final String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty() || password .isEmpty()) {
            Toast.makeText(this, "Completar todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call = null;
        call = service.login(username, password);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        //ResponseMessage responseMessage = response.body();
                        Usuario usuario = response.body();
                        Log.d(TAG, "Bienvenido " + usuario.getNombres());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", usuario.getNombres());
                        startActivity(intent);
                        finish();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Usuario o Clave incorrectos");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });





    }






    public void callSingup(View view) {
    }
}
