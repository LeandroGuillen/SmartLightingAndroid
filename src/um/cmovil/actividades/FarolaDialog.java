package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.recursos.Farola;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FarolaDialog extends AlertDialog implements OnClickListener {
	Farola farola;
	ToggleButton toggleButton;
	SeekBar seekBar;
	TextView name;
	TextView dim;
	ImageView bombilla;
	Button bOk;
	Button bCancel;

	protected FarolaDialog(Context context, Farola f) {
		super(context);
		this.farola = f;
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
				if (progress == 0) {
					toggleButton.setChecked(false);
					bombilla.setImageResource(R.drawable.bombilla_off);
				} else if (!farola.isEncendida() && progress > 0) {
					toggleButton.setChecked(true);
					bombilla.setImageResource(R.drawable.bombilla_on);
				}
				dim.setText(Integer.toString(progress));
				progressAlmacenado = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				farola.setDim(progressAlmacenado);
			}
		});

		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					farola.encender();
					seekBar.setProgress(10);
					bombilla.setImageResource(R.drawable.bombilla_on);
				} else {
					farola.apagar();
					seekBar.setProgress(0);
					bombilla.setImageResource(R.drawable.bombilla_off);
				}
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

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_POSITIVE) {
			// Guardar el estado en el servidor
			Toast.makeText(this.getContext(), "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
		} else {
			// Cancelar el cambio
			Toast.makeText(this.getContext(), "No se han guardado los cambios", Toast.LENGTH_SHORT).show();
		}
	}

}
