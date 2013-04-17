package um.cmovil.actividades;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

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
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";

	private EditText user, password, server;

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
		// Cumplir con servidor, usuario y contraseña.

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

		// FIXME : Se podria indicar alguna validacion más, por ejemplo la
		// longitud del password o el formato de direccion del servidor

		Toast.makeText(LoginActivity.this, "VALIDANDO FORMULARIO",
				Toast.LENGTH_SHORT).show();
		return user.getTextSize() != 0 && password.getTextSize() != 0
				&& server.getTextSize() != 0;
	}

	private class MyDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			Context context = (Context) LoginActivity.this;
			// TODO : Coger la respuesta y extraer datos de ella
			System.out.println("LoginActivity.MyDownloadListener.downloadOk()");
			Toast.makeText(LoginActivity.this, "Conexión aceptada",
					Toast.LENGTH_SHORT).show();
			Controlador.setCookie(response.getHeaders("Cookie")[0].getValue());

			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Information");

			HttpEntity httpEntity = response.getEntity();

			String result = null;

			// TODO : Verificar que hace falta este código, es posible que valga
			// con response.toSTring();
			try {
				result = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dialog.setMessage((String) result);
			dialog.setPositiveButton("OK", null);
			dialog.show();

			/*
			 * // Get response
			 */

			Toast.makeText(LoginActivity.this, "Respuesta http recibida",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(LoginActivity.this,
					"No se pudo realizar la conexi√≥n", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
