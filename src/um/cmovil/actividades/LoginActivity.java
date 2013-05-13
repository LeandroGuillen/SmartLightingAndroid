package um.cmovil.actividades;

import org.apache.http.HttpResponse;

import um.cmovil.R;
import um.cmovil.modelo.Controlador;
import um.cmovil.modelo.HashType;
import um.cmovil.util.DownloadListener;
import um.cmovil.util.HTTPAsyncTask;
import um.cmovil.util.HTTPRequest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnItemSelectedListener {

	public static final String PREFS_NAME = "MyPrefsFile";

	private EditText user, password, server;

	SharedPreferences formStore;

	boolean submitSuccess = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hashtypes, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		spinner.setSelection(0);

		// Recover resources of the layout

		user = (EditText) findViewById(R.id.UserEditText);
		password = (EditText) findViewById(R.id.PasswordEditText);
		server = (EditText) findViewById(R.id.ServerEditText);

		// Comprobamos si se ha podido leer el servidor

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

		user.setText("sr4");
		password.setText("soy el 4");
		server.setText("192.168.1.10:8080");
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
			HTTPRequest httpRequest = new HTTPRequest(this, "/auth",
					new MyDownloadListener());

			new HTTPAsyncTask().execute(httpRequest);

		} else {

			Toast.makeText(getApplicationContext(),
					"Revisa los datos de login", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Valida el formulario de login
	 * 
	 * @return True si es valido, false en caso contrario
	 */
	private boolean validar() {

		// FIXME : Se podria indicar alguna validacion m�s, por ejemplo la
		// longitud del password o el formato de direccion del servidor

		Toast.makeText(LoginActivity.this, "VALIDANDO FORMULARIO",
				Toast.LENGTH_SHORT).show();
		return user.getTextSize() != 0 && password.getTextSize() != 0
				&& server.getTextSize() != 0;
	}

	private class MyDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			// TODO : Coger la respuesta y extraer datos de ella
			System.out.println("LoginActivity.MyDownloadListener.downloadOk()");
			Toast.makeText(LoginActivity.this, "Conexi�n aceptada",
					Toast.LENGTH_SHORT).show();
			Controlador.setCookie(response.getHeaders("Cookie")[0].getValue());
			// TODO : Almacenar cookie en shared
			SharedPreferences.Editor editor = formStore.edit();
			editor.putString("Cookie",
					response.getHeaders("Cookie")[0].getValue());
			editor.commit();
			goToMainActivity(null);
			finish();
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(LoginActivity.this,
					"No se pudo realizar la conexión", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void goToMainActivity(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (position == 0) {
			Controlador.setHashType(HashType.MD5);
		} else {
			Controlador.setHashType(HashType.SHA1);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Controlador.setHashType(HashType.MD5);
	}
}
