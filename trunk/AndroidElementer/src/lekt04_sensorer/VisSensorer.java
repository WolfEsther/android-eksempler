package lekt04_sensorer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class VisSensorer extends Activity implements SensorEventListener {
  TextView textView;
  String[] senesteMålinger = new String[15];
  SensorManager sensorManager;
  MediaPlayer enLyd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    //int hyppighed = SensorManager.SENSOR_DELAY_NORMAL;
    int hyppighed = 250000; // 4 gange i sekundet

		/*
    Sensor orienteringsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (orienteringsSensor != null) {
		sensorManager.registerListener(this, orienteringsSensor, hyppighed);
		} else {
		textView.setText("Fejl. Din telefon har ikke en orienteringssensor");
		}
		/**/
    for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
      System.out.println("sensor=" + sensor);
      sensorManager.registerListener(this, sensor, hyppighed);
    }
    /**/
    enLyd = MediaPlayer.create(this, R.raw.jeg_bremser_haardt);
  }

  @Override
  protected void onPause() {
    super.onPause();
    System.out.println("nu blev onPause() kaldt");
    sensorManager.unregisterListener(this);
    enLyd.release();
  }

  public void onSensorChanged(SensorEvent e) {
    int sensortype = e.sensor.getType();

    String måling = "Type: " + sensortype + " navn: " + e.sensor.getName() + "\n"
        + "Udbyder " + e.sensor.getVendor() + "\n"
        + "Tid: " + e.timestamp + "  præcision: " + e.accuracy;

    if (sensortype == Sensor.TYPE_ORIENTATION) {
      måling = måling + "\n" + e.values[0] + " - vinkel til nord\n" + e.values[1] + " - hældning\n" + e.values[2] + " - krængning";
    } else {
      måling = måling + "\n" + e.values[0] + "\n" + e.values[1] + "\n" + e.values[2];
    }

    if (sensortype == Sensor.TYPE_ACCELEROMETER) {
      // Tjek om det er 3 * normal tyngdeaccelerationen - se http://da.wikipedia.org/wiki/Tyngdeacceleration
      double sum = Math.abs(e.values[0]) + Math.abs(e.values[1]) + Math.abs(e.values[2]);
      if (sum > 3 * SensorManager.GRAVITY_EARTH) {
        if (!enLyd.isPlaying()) {
          enLyd.start(); // BANG!
        }
        måling = måling + "\nBANG!!";
      }
    }

    //System.out.println(måling);

    if (sensortype < senesteMålinger.length) {
      senesteMålinger[sensortype] = måling;
    }

    StringBuilder tekst = new StringBuilder(måling);
    tekst.append("\n===========");

    // Tilføj alle de forskellige sensorers seneste målinger til tekst
    for (String enMåling : senesteMålinger) {
      if (enMåling != null) {
        tekst.append("\n\n").append(enMåling);
      }
    }
    textView.setText(tekst);
  }

  public void onAccuracyChanged(Sensor sensor, int præcision) {
    // ignorér - men vi er nødt til at have metoden for at implementere interfacet
  }
}