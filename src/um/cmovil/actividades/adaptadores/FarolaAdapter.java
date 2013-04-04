package um.cmovil.actividades.adaptadores;

import java.util.List;

import um.cmovil.R;
import um.cmovil.modelo.recursos.Farola;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FarolaAdapter extends ArrayAdapter<Farola> {
	private final Activity context;
	private final List<Farola> farolas;

	public FarolaAdapter(Activity context, List<Farola> farolas) {
		super(context, R.layout.fila_farolas, farolas);
		this.context = context;
		this.farolas = farolas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		// Para cachear los objetos java y no tener que reconstruirlos cada fila
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.fila_farolas, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.labelFarolaName = (TextView) rowView.findViewById(R.id.labelFarolaName);
			viewHolder.labelDistancia = (TextView) rowView.findViewById(R.id.labelDistValue);
			viewHolder.labelDim = (TextView) rowView.findViewById(R.id.labelDimValue);
			viewHolder.imageBombilla = (ImageView) rowView.findViewById(R.id.iconBombilla);
			rowView.setTag(viewHolder);
		}

		// Asignar los valores de la farola a los labels de la fila
		ViewHolder holder = (ViewHolder) rowView.getTag();
		Farola farola = farolas.get(position);
		holder.labelFarolaName.setText(farola.getNombre());
		holder.labelDistancia.setText(farola.getDistancia().toString() + "m");
		holder.labelDim.setText(farola.getDim().toString());

		// Segun la farola este encendida se muestra una imagen u otra
		if (farola.isEncendida()) {
			holder.imageBombilla.setImageResource(R.drawable.bombilla_on);
		} else {
			holder.imageBombilla.setImageResource(R.drawable.bombilla_off);
		}

		return rowView;
	}

	static class ViewHolder {
		public TextView labelFarolaName;
		public TextView labelDistancia;
		public TextView labelDim;
		public ImageView imageBombilla;
	}

}
