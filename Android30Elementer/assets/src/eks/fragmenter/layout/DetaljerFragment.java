/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.fragmenter.layout;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import eks.livscyklus.LogFragment;

/**
 * This is the secondary fragment, displaying the details of a particular
 * item.
 */
/**
 *
 * @author j
 */
public class DetaljerFragment extends LogFragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (container == null) {
      // We have different layouts, and in one of them this
      // fragment's containing frame doesn't exist.  The fragment
      // may still be created from its saved state, but there is
      // no reason to try to create its view hierarchy because it
      // won't be displayed.  Note this is not needed -- we could
      // just run the code below, where we would create and return
      // the view hierarchy; it would just never be used.
      return null;
    }
    ScrollView scroller = new ScrollView(getActivity());
    TextView text = new TextView(getActivity());
    int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources().getDisplayMetrics());
    text.setPadding(padding, padding, padding, padding);
    scroller.addView(text);
    if (getArguments()==null) text.setText("Mangler argumenter - start "+LayoutAktivitet.class.getSimpleName()+" i stedet.");
    else text.setText(ShakespeareData.DIALOGUE[getArguments().getInt("index", 0)]);
    return scroller;
  }

}