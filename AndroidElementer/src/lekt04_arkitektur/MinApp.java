/**
 Esperanto-radio por Androjd, farita de Jacob Nordfalk.
 Kelkaj partoj de la kodo originas de DR Radio 2 por Android, vidu
 http://code.google.com/p/dr-radio-android/

 Esperanto-radio por Androjd estas libera softvaro: vi povas redistribui
 ĝin kaj/aŭ modifi ĝin kiel oni anoncas en la licenco GNU Ĝenerala Publika
 Licenco (GPL) versio 2.

 Esperanto-radio por Androjd estas distribuita en la espero ke ĝi estos utila,
 sed SEN AJNA GARANTIO; sen eĉ la implica garantio de surmerkatigindeco aŭ
 taŭgeco por iu aparta celo.
 Vidu la GNU Ĝenerala Publika Licenco por pli da detaloj.

 Vi devus ricevi kopion de la GNU Ĝenerala Publika Licenco kune kun la
 programo. Se ne, vidu <http://www.gnu.org/licenses/>.
 */
package lekt04_arkitektur;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import lekt06_youtube.FilCache;

/**
 * Her kan foretages fælles initialisering.
 * Resten af programmet bliver først initialiseret efter at objektet og
 * kaldet til metoden onCreate() er afsluttet, så det er vigtigt kun at
 * udføre de allermest nødvendige ting her.
 */
public class MinApp extends Application {
  /**
   * Data kunne lige så godt være gemt i en klassevariabel andetsteds
   */
  public static SharedPreferences prefs;

  /**
   * Håndtag til forgrundstråden
   */
  public static Handler forgrundstråd = new Handler();

  @Override
  public void onCreate() {
    Log.d("MinApplicationSingleton", "onCreate() kaldt");
    super.onCreate();

    // Initialisering der kræver en Context
    prefs = PreferenceManager.getDefaultSharedPreferences(this);


    // Initialisering af hjælpeklasser, f.eks. mappen som en cache af filer
    // hentet over netværket behøver, er en fin ide at lægge her, for ellers
    // skal tjek for denne initialisering ske i alle de aktiviteter, services
    // og recievers der er afhængig af hjælpeklasserne
    FilCache.init(this.getCacheDir());
  }
}
