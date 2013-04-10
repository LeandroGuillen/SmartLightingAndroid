package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.recursos.Farola;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FarolaDialog extends Dialog {
	Farola f;

	protected FarolaDialog(Context context, Farola f) {
		super(context);

		this.f = f;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_farola);

		// Actualizar con los valores de la farola
		TextView name = (TextView) findViewById(R.id.dfName);
		name.setText(f.getNombre());

		final TextView dim = (TextView) findViewById(R.id.dfDim);
		dim.setText(f.getDim().toString());

		final ImageView bombilla = (ImageView) findViewById(R.id.dfBombilla);
		if (f.isEncendida())
			bombilla.setImageResource(R.drawable.bombilla_on);
		else
			bombilla.setImageResource(R.drawable.bombilla_off);

		final ToggleButton tb = (ToggleButton) findViewById(R.id.dfToggle);
		final SeekBar sb = (SeekBar) findViewById(R.id.dfSeekBar);

		sb.setMax(100);
		sb.setProgress(f.getDim());
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			private int progressAlmacenado;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (progress == 0) {
					f.apagar();
					bombilla.setImageResource(R.drawable.bombilla_off);
					tb.setChecked(false);
				} else if (!f.isEncendida() && progress > 0) {
					f.encender();
					bombilla.setImageResource(R.drawable.bombilla_on);
					tb.setChecked(true);
				}
				dim.setText(Integer.toString(progress));
				progressAlmacenado = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				f.setDim(progressAlmacenado);
			}
		});

		tb.setChecked(f.isEncendida());
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					f.encender();
					sb.setProgress(100);
					bombilla.setImageResource(R.drawable.bombilla_on);
				} else {
					f.apagar();
					sb.setProgress(0);
					bombilla.setImageResource(R.drawable.bombilla_off);
				}
			}
		});

	}
}
