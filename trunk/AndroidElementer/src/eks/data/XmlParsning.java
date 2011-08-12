package eks.data;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;
import java.io.BufferedInputStream;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

/**
 *
 * @author Jacob Nordfalk
 */
public class XmlParsning extends Activity {
//Ku også parse f.eks http://www.dmi.dk/dmi/rss-nyheder.xml ?
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv=new TextView(this);


    try {
      /*
      XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser xpp=factory.newPullParser();
       */
      XmlPullParser xpp=android.util.Xml.newPullParser();

      InputStream is=getResources().openRawResource(R.raw.data_xmleksempel);
      // Det kan være nødvendigt at hoppe over BOM mark - se http://android.forums.wordpress.org/topic/xml-pull-error?replies=2
      //is.read(); //is.read(); //is.read();  - dette virker kun hvis der ALTID er en BOM

      // Hop over BOM - hvis den er der!
      is = new BufferedInputStream(is);  // bl.a. FileInputStream understøtter ikke mark, så brug BufferedInputStream
      is.mark(1); // vi har faktisk kun brug for at søge én byte tilbage
      if (is.read() == 0xef) { is.read(); is.read(); } // Der var en BOM! Læs de sidste 2 byte
      else is.reset(); // Der var ingen BOM - hop tilbage til start

      xpp.setInput(is, "UTF-8"); // evt "ISO-8859-1"
      String kundenavn=null;
      double totalKredit=0;

      int eventType=xpp.getEventType();
      while (eventType!=XmlPullParser.END_DOCUMENT) {
        if (eventType==XmlPullParser.START_TAG) {
          String tag=xpp.getName();
          if ("bank".equals(tag)) {
            String banknavn=xpp.getAttributeValue(null, "navn");
            tv.append("\n=== Oversigt over "+banknavn+"s kunder ===\n");
          } else if ("kunde".equals(tag)) {
            kundenavn=xpp.getAttributeValue(null, "navn");
          } else if ("kredit".equals(tag)) {
            double kredit=Double.parseDouble(xpp.nextText());
            tv.append(kundenavn+" med "+kredit+" kr.\n");
            totalKredit=totalKredit+kredit;
          }
        }
        eventType=xpp.next();
      }
      is.close();
      tv.append("\n\nTotal kredit er "+totalKredit+" kr.");
    } catch (Exception ex) {
      ex.printStackTrace();
      tv.append("FEJL:"+ex.toString());
    }

    setContentView(tv);
  }
}