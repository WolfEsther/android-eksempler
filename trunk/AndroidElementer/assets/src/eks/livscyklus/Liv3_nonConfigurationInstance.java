package eks.livscyklus;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Når skærmen vendes eller tastaturet bliver skubbet ind eller ud
 * bliver aktiviteten normalt kasseret og en ny aktivitet oprettes
 * med de nye skærmdimensioner etc.
 *
 * Dette kan slås fra ved at sætte denne attribut på aktiviteten i AndroidManifest.xml:
 *             android:configChanges="orientation|keyboardHidden"
 *
 * Men det er normalt bedre at huske data_liste når skærmen vendes.
 * 
 * Det gøres ved at definere onRetainNonConfigurationInstance(),
 * som systemet vil kalde på den gamle aktivitet når telefonen vendes.
 * 
 * Derefter kan man i onCreate() i den nye aktivitet kalde
 * getLastNonConfigurationInstance() som vil give objektet tilbage
 *
 * @author Jacob Nordfalk
 */
public class Liv3_nonConfigurationInstance extends LogAktivitet {

  Programdata data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Programdata dataFraForrigeAkrivitet = (Programdata) getLastNonConfigurationInstance();

    if (dataFraForrigeAkrivitet == null) {
      data = new Programdata();
      data.liste.add("første element");
    } else {
      data = dataFraForrigeAkrivitet;
      data.liste.add("dataFraForrigeAkrivitet "+data.liste.size());
    }

    EditText tv = new EditText(this);
    tv.setText( data.toString() );
    setContentView(tv);
  }

  @Override
  public Object onRetainNonConfigurationInstance() {
    return data;
  }

  @Override
  protected void onRestoreInstanceState (Bundle savedInstanceState) {
     // her genskabes indhold for alle views med id
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState); // gem indhold for alle views med id
    outState.putInt("etTal", data.etTal++);
    outState.putInt("etAndetTal", data.etAndetTal);
    outState.putStringArrayList("liste", data.liste);
  }
}
