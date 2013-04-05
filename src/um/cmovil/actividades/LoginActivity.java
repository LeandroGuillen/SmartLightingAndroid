package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.Controlador;
import um.cmovil.util.DownloadListener;
import um.cmovil.util.HTTPAsyncTask;
import um.cmovil.util.HTTPRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{

	public static final String PREFS_NAME = "MyPrefsFile";

	private EditText user, password, server;
	private Button login;

	SharedPreferences formStore;

	boolean submitSuccess = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Recover resources of the layout
		user = (EditText) findViewById(R.id.UserEditText);
		password = (EditText) findViewById(R.id.PasswordEditText);
		server = (EditText) findViewById(R.id.ServerEditText);

	
		// Retrieve or create the preferences object
		// File defined in FormActivity.xml
		// formStore = getPreferences(Activity.MODE_PRIVATE);

		// File defined in PREFS_NAME
		formStore = getSharedPreferences(PREFS_NAME, 0);

	}

	@Override
	public void onResume() {
		super.onResume();
		// Set a Toast to notify that it is onResume
		Context context = getApplicationContext();
		CharSequence text = "onResume!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		// Restore the form data
		user.setText(formStore.getString("user", ""));
		password.setText(formStore.getString("passwrod", ""));
		server.setText(formStore.getString("server", ""));
	}

	@Override
	public void onPause() {
		super.onPause();

		// Set a Toast to notify that it is onResume
		Context context = getApplicationContext();
		CharSequence text = "onPause!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		if (submitSuccess) {
			// Editor calls can be chained together
			formStore.edit().clear().commit();
		} else {
			// Store the form data
			SharedPreferences.Editor editor = formStore.edit();
			editor.putString("user", user.getText().toString());
			editor.putString("password", password.getText().toString());

			editor.putString("server", server.getText().toString());
			editor.commit();
		}
	}

	public void logIn(View v) {

		// TODO : Validar el formulario
		// Cumplir con servidor, usuario y contrase�a.

		if (validar()) {

			Controlador.setUserAgent(user.getText().toString());
			Controlador.setKey(password.getText().toString());
			Controlador.setServer(server.getText().toString());
			HTTPRequest httpRequest = new HTTPRequest(this,"/testauth",new MyDownloadListener());
			new HTTPAsyncTask().execute(httpRequest);
			
				
		} else {

			Toast.makeText(getApplicationContext(),
					"Revisa los datos de login", Toast.LENGTH_SHORT).show();
		}

		// Close
		finish();
	}

	/**
	 * Valida el formulario de login
	 * 
	 * @return True si es valido, false en caso contrario
	 */
	private boolean validar() {

		// FIXME : Se podria indicar alguna validacion m�s, por ejemplo la
		// longitud del password o el formato de direccion del servidor

		return user.getTextSize() != 0 && password.getTextSize() != 0
				&& server.getTextSize() != 0;
	}

	

	private class MyDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(Object result) {
			Context context = (Context) LoginActivity.this;

			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Information");
			dialog.setMessage((String) result);
			dialog.setPositiveButton("OK", null);
			dialog.show();
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(LoginActivity.this, "No se pudo realizar la conexión", Toast.LENGTH_SHORT).show();
		}
	}
}
