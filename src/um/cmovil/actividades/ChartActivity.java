package um.cmovil.actividades;

import java.util.List;

import um.cmovil.R;
import um.cmovil.modelo.ControladorTiempo;
import um.cmovil.modelo.recursos.Tiempo;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class ChartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		// init example series data
		List<Tiempo> tiempos = ControladorTiempo.getUltimosTiempos();
		GraphViewData[] gvdArray = new GraphViewData[tiempos.size()];
		int x = 0;
		for (Tiempo t : tiempos) {
			GraphViewData gvd = new GraphViewData(x, t.getTemperatura());
			gvdArray[x] = gvd;
			x++;
		}
		GraphViewSeries tempSeries = new GraphViewSeries(gvdArray);
		
//		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] { new GraphViewData(1, 2.0d), new GraphViewData(2, 1.5d), new GraphViewData(3, 2.5d), new GraphViewData(4, 1.0d) });

		GraphView graphView = new LineGraphView(this, "Temperatura");
		graphView.addSeries(tempSeries);

		LinearLayout layout = (LinearLayout) findViewById(R.id.chartLinearLayout);
		layout.addView(graphView);

	}

}
