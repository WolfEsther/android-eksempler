package lekt02_intents;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.InputType;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class BenytIntents extends Activity implements OnClickListener {
  EditText tekstfelt, nummerfelt;
  Button ringOp, ringOpDirekte, sendSms, delApp, sendEpost, websøgning, webadresse, wifiIndstillinger;


  /**
   * Ofte har man som udvikler brug for info om den telefon brugeren har.
   * Denne metode giver telefonmodel, Androidversion og programversion etc.
   */
  public String lavTelefoninfo() {
    String version = "(ukendt)";
    try {
      PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
      version = pi.versionName;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "\nProgram: " + getPackageName() + " version " + version + "\nTelefonmodel: " + Build.MODEL + "\n" + Build.PRODUCT + "\nAndroid v" + Build.VERSION.RELEASE + "\nsdk: r" + Build.VERSION.SDK_INT
        + "\nAndroid ID: " + Secure.getString(getContentResolver(), Secure.ANDROID_ID);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    tekstfelt = new EditText(this);
    tekstfelt.setText("Skriv tekst her");
    tl.addView(tekstfelt);

    nummerfelt = new EditText(this);
    nummerfelt.setHint("evt telefonnummer her");
    nummerfelt.setInputType(InputType.TYPE_CLASS_PHONE);
    tl.addView(nummerfelt);

    ringOp = new Button(this);
    ringOp.setText("Ring op");
    tl.addView(ringOp);

    ringOpDirekte = new Button(this);
    ringOpDirekte.setText("Ring op - direkte");
    tl.addView(ringOpDirekte);

    sendSms = new Button(this);
    sendSms.setText("Åbn send SMS");
    tl.addView(sendSms);

    sendEpost = new Button(this);
    sendEpost.setText("Åbn og send epostbesked");
    tl.addView(sendEpost);

    delApp = new Button(this);
    delApp.setText("Del app...");
    tl.addView(delApp);

    websøgning = new Button(this);
    websøgning.setText("Websøgning");
    tl.addView(websøgning);

    webadresse = new Button(this);
    webadresse.setText("Webadresse (javabog)");
    tl.addView(webadresse);

    TextView tv = new TextView(this);
    tv.setText("Man kan også bruge klassen Linkify til at putte intents ind i tekst.\n" + "Mit telefonnummer er 26206512, min e-post er jacob.nordfalk@gmail.com og " + "jeg har en hjemmeside på http://javabog.dk.");
    Linkify.addLinks(tv, Linkify.ALL);
    tl.addView(tv);

    wifiIndstillinger = new Button(this);
    wifiIndstillinger.setText("Indstillinger (f.eks. wifi)");
    tl.addView(wifiIndstillinger);


    ringOp.setOnClickListener(this);
    ringOpDirekte.setOnClickListener(this);
    sendSms.setOnClickListener(this);
    delApp.setOnClickListener(this);
    sendEpost.setOnClickListener(this);
    websøgning.setOnClickListener(this);
    webadresse.setOnClickListener(this);
    wifiIndstillinger.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }


  public void onClick(View v) {
    String nummer = nummerfelt.getText().toString();
    String tekst = tekstfelt.getText().toString();
    if (v == ringOp) {
      Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + nummer));
      startActivity(intent);
      // eller blot:
      //     startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+nummer)));
    } else if (v == ringOpDirekte) {
      try {
        // Bemærk: Kræver <uses-permission android:name="android.permission.CALL_PHONE" /> i manifestet
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nummer)));
      } catch (Exception e) {
        Toast.makeText(this, "Du mangler <uses-permission android:name=\"android.permission.CALL_PHONE\" /> i manifestet\n" + e, Toast.LENGTH_LONG).show();
      }
    } else if (v == sendSms) {
      // Åbner et SMS-vindue og lader brugeren sende SMS'en
      // Kilde: http://andmobidev.blogspot.com/2010/01/launching-smsmessages-activity-using.html
      //Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+number));
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setType("vnd.android-dir/mms-sms");
      intent.putExtra("sms_body", tekst + lavTelefoninfo());
      intent.putExtra("address", nummer);
      startActivity(intent);
    } else if (v == sendEpost) {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("message/rfc822");
      i.putExtra(Intent.EXTRA_EMAIL, new String[]{tekst});
      i.putExtra(Intent.EXTRA_SUBJECT, nummer);
      i.putExtra(Intent.EXTRA_TEXT, lavTelefoninfo());
      i.putExtra(Intent.EXTRA_CC, new String[]{"jacob.nordfalk@gmail.com"});
      startActivity(Intent.createChooser(i, "Send e-post..."));
    } else if (v == delApp) {
      Intent i = new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_SUBJECT, "Prøv AndroidElementer").putExtra(Intent.EXTRA_TEXT, "Hej!\n\n" +
          "Hvis du programmerer til Android så prøv denne her eksempelsamling\n" +
          "AndroidElementer\n" +
          "https://market.android.com/details?id=dk.nordfalk.android.elementer").setType("text/plain");
      startActivity(Intent.createChooser(i, "Del via"));
    } else if (v == websøgning) {
      startActivity(new Intent(Intent.ACTION_WEB_SEARCH).putExtra(SearchManager.QUERY, tekst));
    } else if (v == webadresse) {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javabog.dk"));
      startActivity(intent);
    } else if (v == wifiIndstillinger) {
      startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }
  }
}
