package com.cameronvargas.app.morphanalysis;

import com.cameronvargas.app.morphanalysis.R;
import com.cameronvargas.app.morphanalysis.R.id;
import com.cameronvargas.app.morphanalysis.R.layout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DsiplayMessageActivity extends Activity {

	private int SIZE_DEFINITION = 15;
	private int SIZE_TABLE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dsiplay_message);
		// Show the Up button in the action bar.
		setupActionBar();
		setViewTextSize();

		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

		Intent intent = getIntent();
		String[] message = intent
				.getStringArrayExtra(MainActivity.EXTRACTED_DOC);

		TableLayout tl = (TableLayout) findViewById(R.id.forms_table);

		// layout for rows
		TableLayout.LayoutParams rowlayout = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		rowlayout.setMargins(20, 10, 20, 10);

		for (int i = 0; i < message.length; i++) {
			if (message[i].contains("<table")) {
				int tableDim = Integer.valueOf(message[i].substring(7,
						message[i].length() - 1));
				TableLayout.LayoutParams tlayout1 = new TableLayout.LayoutParams(
						TableLayout.LayoutParams.WRAP_CONTENT,
						TableLayout.LayoutParams.WRAP_CONTENT);
				TableLayout table1 = new TableLayout(this);
				i++;
				while (!message[i].contains("</table>")) {
					TableRow trow1 = new TableRow(this);
					trow1.setLayoutParams(rowlayout);
					for (int j = 0; j < tableDim
							&& !message[i].contains("</table>"); j++, i++) {
						TextView tv1 = new TextView(this);
						tv1.setText(message[i].replace(',', '\n'));
						tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE_TABLE);
						tv1.setTextColor(Color.BLACK);
						tv1.setPadding(10, 5, 10, 5);
						tv1.setTextIsSelectable(true);

						trow1.addView(tv1);
						trow1.setLayoutParams(tlayout1);
						trow1.setGravity(Gravity.CENTER_HORIZONTAL);
					}

					table1.addView(trow1);
					// table1.setShrinkAllColumns(true);
					table1.setColumnShrinkable(0, true);
					table1.setPadding(0, 10, 0, 30);
				}
				tl.addView(table1);
			} else {

				TableRow trow0 = new TableRow(this);
				TextView tv0 = new TextView(this);

				tv0.setText(message[i]);
				tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE_DEFINITION);
				tv0.setGravity(Gravity.CENTER_HORIZONTAL);
				tv0.setMaxWidth(mDisplayMetrics.widthPixels - 50);
				tv0.setTextColor(Color.BLACK);
				tv0.setTextIsSelectable(true);

				trow0.setLayoutParams(rowlayout);
				trow0.addView(tv0);

				tl.addView(trow0);

			}
		}

	}

	private void setViewTextSize() {
		int screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;

		String toastMsg;
		switch (screenSize) {
		case 4:
			SIZE_DEFINITION = 30;
			SIZE_TABLE = 22;
			toastMsg = "XLarge screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			SIZE_DEFINITION = 25;
			SIZE_TABLE = 20;
			toastMsg = "Large screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			SIZE_DEFINITION = 15;
			SIZE_TABLE = 10;
			toastMsg = "Normal screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			SIZE_DEFINITION = 12;
			SIZE_TABLE = 8;
			toastMsg = "Small screen";
			break;
		default:
			SIZE_DEFINITION = 15;
			SIZE_TABLE = 10;
			toastMsg = "Screen could not be detected";
		}

		// Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}