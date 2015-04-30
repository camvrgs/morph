package com.cameronvargas.app.morphanalysis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.cameronvargas.app.morphanalysis.R;
import com.cameronvargas.app.morphanalysis.R.id;
import com.cameronvargas.app.morphanalysis.R.layout;
import com.cameronvargas.app.morphanalysis.R.menu;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

	// CONSTANTS
	public final static String EXTRACTED_DOC = "com.cameronvargas.app.morphanalysis.MESSAGE";
	public final static String URL_DEST = "http://starling.rinet.ru/cgi-bin/morph.cgi?flags=endnnnnn&root=config&word=";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			intentTxt(intent);
		}

		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// menu actions
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_info:
			// startDefFrag();
			return true;
		case R.id.action_settings:
			//
			return true;
		default:
			return true;
		}
	}

	public void sendMessage(View view) {

		Intent intent = new Intent(this, DsiplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		processMessage(message);
	}

	public static String urlReplace(String replac) {
		return replac.replace(" ", "+").replace("А", "%C0").replace("Б", "%C1")
				.replace("В", "%C2").replace("Г", "%C3").replace("Д", "%C4")
				.replace("Е", "%C5").replace("Ё", "%C5").replace("Ж", "%C6")
				.replace("З", "%C7").replace("И", "%C8").replace("Й", "%C9")
				.replace("К", "%CA").replace("Л", "%CB").replace("М", "%CC")
				.replace("Н", "%CD").replace("О", "%CE").replace("П", "%CF")
				.replace("Р", "%D0").replace("С", "%D1").replace("Т", "%D2")
				.replace("У", "%D3").replace("Ф", "%D4").replace("Х", "%D5")
				.replace("Ц", "%D6").replace("Ч", "%D7").replace("Ш", "%D8")
				.replace("Щ", "%D9").replace("Ъ", "%DA").replace("Ы", "%DB")
				.replace("Ь", "%DC").replace("Э", "%DD").replace("Ю", "%DE")
				.replace("Я", "%DF")

				.replace("а", "%E0").replace("б", "%E1").replace("в", "%E2")
				.replace("г", "%E3").replace("д", "%E4").replace("е", "%E5")
				.replace("ё", "%E5").replace("ж", "%E6").replace("з", "%E7")
				.replace("и", "%E8").replace("й", "%E9").replace("к", "%EA")
				.replace("л", "%EB").replace("м", "%EC").replace("н", "%ED")
				.replace("о", "%EE").replace("п", "%EF").replace("р", "%F0")
				.replace("с", "%F1").replace("т", "%F2").replace("у", "%F3")
				.replace("ф", "%F4").replace("х", "%F5").replace("ц", "%F6")
				.replace("ч", "%F7").replace("ш", "%F8").replace("щ", "%F9")
				.replace("ъ", "%FA").replace("ы", "%FB").replace("ь", "%FC")
				.replace("э", "%FD").replace("ю", "%FE").replace("я", "%FF");
	}

	public class urlParseThread extends AsyncTask<String, Integer, Document> {
		private ArrayList<DefinitionTable> elem = new ArrayList<DefinitionTable>();
		private ProgressDialog progressDialog;

		public Document doInBackground(String... params) {
			Document doc = null;
			try {

				doc = Jsoup.connect(params[0]).get();

				/*
				 * // grab source form of word (no tags in html) Element a =
				 * doc.select("br").first(); Node node = a.nextSibling();
				 * elem.add(node.toString());
				 * 
				 * // create for loop to fill array Elements para =
				 * doc.select("p"); for (int i=0; i<para.size(); i++) {
				 * elem.add(para.get(i).text()); }
				 */

				Elements text = doc.select("br, p, h2, h3, table");
				for (int i = 0; i < text.size(); i++) {
					if (text.get(i).tag().getName() == "br") {
						Node node = text.get(i).nextSibling();
						if (node instanceof TextNode) {
							TextNode txt = (TextNode) node;
							if(txt.text().contains(":")){
								int colon = txt.text().indexOf(":");
								DefinitionData dh = new DefinitionData(true, txt.text().substring(0, colon));
								DefinitionData dd = new DefinitionData(false, txt.text().substring(colon+1, txt.text().length()-1));
								DefinitionTable dt = new DefinitionTable(2);
								
								dt.pushData(dh);
								dt.pushData(dd);
								
								elem.add(dt);
								
							}
							else{
								DefinitionData dh = new DefinitionData(true, txt.text());
								DefinitionTable dt = new DefinitionTable();
								
								dt.pushData(dh);
								
								elem.add(dt);
							}
						}
					} else {
						if (text.get(i).tag().getName() == "p"
								|| text.get(i).tag().getName() == "h2"
								|| text.get(i).tag().getName() == "h3") {
							
							if(text.get(i).text().contains(":")){
								int colon = text.get(i).text().indexOf(":");
								DefinitionData dh = new DefinitionData(true, text.get(i).text().substring(0, colon));
								DefinitionData dd = new DefinitionData(false, text.get(i).text().substring(colon+1, text.get(i).text().length()-1));
								DefinitionTable dt = new DefinitionTable(2);
								
								dt.pushData(dh);
								dt.pushData(dd);
								
								elem.add(dt);
								
							}
							else{
							
								DefinitionData dh = new DefinitionData(true, text.get(i).text());
								DefinitionTable dt = new DefinitionTable();
								
								dt.pushData(dh);
								
								elem.add(dt);
							}
							
						} else {
							if (text.get(i).tag().getName() == "table") {
										
								int tdim = text.get(i)
										.select("tr:first-child > th").size();
								
								DefinitionTable dt = new DefinitionTable(tdim);
								DefinitionData dd;
								
								Elements table = text.get(i).select("th, td");
								for (int j = 0; j < table.size(); j++) {
									if(table.get(j).tag().getName() == "th"){
										dd = new DefinitionData(true, table.get(j).text());
									} else {
										dd = new DefinitionData(false, table.get(j).text());
									}
									dt.pushData(dd);
								}
								
								elem.add(dt);
								
								//String tsize = Integer.toString(tdim);

								//elem.add("<table:" + tsize + ">");
								/*
								Elements table = text.get(i).select("th, td");
								for (int j = 0; j < table.size(); j++) {
									elem.add(table.get(j).text());
								}

								elem.add("</table>");
								*/
							} else {
								// elem.add(text.get(i).text());
							}
						}
					}
				}

			} catch (Exception e) {
				doc = null;
				e.printStackTrace();
			}
			return doc;
		}

		public void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivity.this,
					"Please Wait", "Searching for data...", true, false);
		}

		public void onPostExecute(Document result) {
			if (result != null) {
				// ((EditText)findViewById(R.id.edit_message)).setText(out);
				
				DefinitionTable[] out = new DefinitionTable[elem.size()];
				for(int i=0; i<elem.size(); i++){
					out[i] = elem.get(i);
				}
				
				//String[] out = elem.toArray(new String[elem.size()]);
				// for(int i=0; i<out.length; i++){ System.out.println(out[i]);
				// }
				Intent intent = new Intent(MainActivity.this,
						DsiplayMessageActivity.class);
				intent.putParcelableArrayListExtra(EXTRACTED_DOC, elem);
				startActivity(intent);
			} else {
				((EditText) findViewById(R.id.edit_message)).setText("Error");
			}
			progressDialog.dismiss();
		}
	}

	private void startDefFrag() {
		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		if (findViewById(R.id.fragment_container) != null) {

			// Create a new Fragment to be placed in the activity layout
			DefinitionFragment firstFragment = new DefinitionFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments

			// Add the fragment to the 'fragment_container' FrameLayout
			Intent intent = new Intent(MainActivity.this,
					DefinitionFragment.class);
			intent.putExtra(EXTRACTED_DOC, "hello world");
			firstFragment.setArguments(getIntent().getExtras());

			getFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
	}

	private void processMessage(String str) {
		String destUrl = URL_DEST + urlReplace(str.trim());
		new urlParseThread().execute(destUrl);
	}

	private void intentTxt(Intent intent) {
		String text = intent.getStringExtra(Intent.EXTRA_TEXT.toString());
		System.out.println(text);
		if (text != null) {
			// Toast.makeText(this, text, Toast.LENGTH_LONG).show();
			processMessage(text);
		}
	}

}
