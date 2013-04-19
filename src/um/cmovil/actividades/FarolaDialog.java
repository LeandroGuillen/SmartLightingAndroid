package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.recursos.Farola;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FarolaDialog extends AlertDialog implements OnClickListener {
	Farola farola;

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
		TextView name = (TextView) findViewById(R.id.dfName);
		name.setText(farola.getNombre());

		final TextView dim = (TextView) findViewById(R.id.dfDim);
		dim.setText(farola.getDim().toString());

		final ImageView bombilla = (ImageView) findViewById(R.id.dfBombilla);
		if (farola.isEncendida())
			bombilla.setImageResource(R.drawable.bombilla_on);
		else
			bombilla.setImageResource(R.drawable.bombilla_off);

		final ToggleButton tb = (ToggleButton) findViewById(R.id.dfToggle);
		final SeekBar sb = (SeekBar) findViewById(R.id.dfSeekBar);

		sb.setMax(100);
		sb.setProgress(farola.getDim());
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			private int progressAlmacenado;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (progress == 0) {
					tb.setChecked(false);
					bombilla.setImageResource(R.drawable.bombilla_off);
				} else if (!farola.isEncendida() && progress > 0) {
					tb.setChecked(true);
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

		tb.setChecked(farola.isEncendida());
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					farola.encender();
					sb.setProgress(10);
					bombilla.setImageResource(R.drawable.bombilla_on);
				} else {
					farola.apagar();
					sb.setProgress(0);
					bombilla.setImageResource(R.drawable.bombilla_off);
				}
			}
		});

		setButton(BUTTON_POSITIVE, "OK", this);
		setButton(BUTTON_NEGATIVE, "Cancelar", this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_POSITIVE) {
			// Guardar el estado en el servidor
			Toast.makeText(this.getContext(), "Guardar el estado en el servidor", Toast.LENGTH_SHORT).show();
			dismiss();
		}
		else {
			// Cancelar el cambio
			dismiss();
		}
	}
}
