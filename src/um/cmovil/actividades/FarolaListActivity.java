package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.actividades.adaptadores.FarolaAdapter;
import um.cmovil.modelo.ControladorFarolas;
import um.cmovil.modelo.recursos.Farola;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FarolaListActivity extends Activity {
	FarolaAdapter farolaAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_farola_list);

		prepararLista();
	}

	private void prepararLista() {

		// We get the ListView component from the layout
		final ListView lv = (ListView) findViewById(R.id.listViewFarolas);

		farolaAdapter = new FarolaAdapter(this, ControladorFarolas.getListaFarolas());
		lv.setAdapter(farolaAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Farola f = (Farola) lv.getAdapter().getItem(position);

				 new FarolaDialog(FarolaListActivity.this, f).show();
//				View v = getLayoutInflater().inflate(R.layout.dialog_farola, null);
//				AlertDialog.Builder adb = new AlertDialog.Builder(FarolaListActivity.this);
//
//				// set title
//				adb.setTitle("Cambiar farola");
//				adb.setView(v);
//
//				// set dialog message
//				adb.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						// if this button is clicked, close
//						// current activity
//						
//					}
//				}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//						dialog.cancel();
//					}
//				});
//				
//				// create alert dialog
//				AlertDialog alertDialog = adb.create();
//
//				// show it
//				alertDialog.show();
//				
//				farolaAdapter.notifyDataSetChanged();
			}
		});

		

	}

}
