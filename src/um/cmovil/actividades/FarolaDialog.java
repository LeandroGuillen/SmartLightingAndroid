package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.recursos.Farola;
import um.cmovil.util.DownloadListener;
import um.cmovil.util.HTTPAsyncTask;
import um.cmovil.util.HTTPRequest;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FarolaDialog extends Dialog {
	Farola farola;
	DownloadListener listener;

	ToggleButton toggleButton;
	SeekBar seekBar;
	TextView name;
	TextView dim;
	ImageView bombilla;
	Button bOk;
	Button bCancel;

	public FarolaDialog(Context context, Farola f, DownloadListener listener) {
		super(context);
		this.farola = f;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_farola);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

		// Actualizar con los valores de la farola
		name = (TextView) findViewById(R.id.dfName);

		dim = (TextView) findViewById(R.id.dfDim);

		bombilla = (ImageView) findViewById(R.id.dfBombilla);

		toggleButton = (ToggleButton) findViewById(R.id.dfToggle);

		seekBar = (SeekBar) findViewById(R.id.dfSeekBar);
		seekBar.setMax(100);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			private int progressAlmacenado;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				progressAlmacenado = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (progressAlmacenado == 0) {
					toggleButton.setChecked(false);
					bombilla.setImageResource(R.drawable.bombilla_off);
				} else if (!farola.isEncendida() && progressAlmacenado > 0) {
					toggleButton.setChecked(true);
					bombilla.setImageResource(R.drawable.bombilla_on);
				}
				dim.setText(Integer.toString(progressAlmacenado));
			}
		});

		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					farola.encender();
//					seekBar.setProgress(100);
					bombilla.setImageResource(R.drawable.bombilla_on);
					if(seekBar.getProgress()==0){
						dim.setText(Integer.toString(100));
						seekBar.setProgress(100);
					}
					else
						dim.setText(Integer.toString(seekBar.getProgress()));
				} else {
					farola.apagar();
					seekBar.setProgress(0);
					bombilla.setImageResource(R.drawable.bombilla_off);
					dim.setText(Integer.toString(0));
				}
			}
		});

		Button bOk = (Button) findViewById(R.id.bOk);
		Button bCa = (Button) findViewById(R.id.bCancelar);

		bOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Actualizar la farola localmente
				Farola f = FarolaDialog.this.farola;
				f.setDim(seekBar.getProgress());
				f.setEncendida(toggleButton.isChecked());

				// Preparar datos para actualizar el servidor
				String comando = "/resources/streetlight";
				comando += "?farola=" + f.getNombre();
				comando += "&encendida=" + f.isEncendida();
				comando += "&dim=" + f.getDim();

				// Enviar peticion de actualizacion
				HTTPRequest request = new HTTPRequest(FarolaDialog.this.getContext(), comando, listener);
				new HTTPAsyncTask().execute(request);
				// Cerrar el dialogo
				FarolaDialog.this.dismiss();
			}
		});

		bCa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// No hay que guardar los cambios
				FarolaDialog.this.cancel();
			}
		});
	}

	public void onStart() {
		seekBar.setProgress(farola.getDim());
		toggleButton.setChecked(farola.isEncendida());

		if (farola.isEncendida())
			bombilla.setImageResource(R.drawable.bombilla_on);
		else
			bombilla.setImageResource(R.drawable.bombilla_off);

		dim.setText(farola.getDim().toString());

		name.setText(farola.getNombre());
	}

}
