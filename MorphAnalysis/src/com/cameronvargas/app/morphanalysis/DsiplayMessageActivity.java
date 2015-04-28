package com.cameronvargas.app.morphanalysis;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.cameronvargas.app.morphanalysis.R;
import com.cameronvargas.app.morphanalysis.MainActivity.urlParseThread;
import com.cameronvargas.app.morphanalysis.R.id;
import com.cameronvargas.app.morphanalysis.R.layout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DsiplayMessageActivity extends Activity {
	
	private enum DisplaySizes{
		DEFINITION(15),
		TABLE(10),
		HEADER(15),
		PARAGRAPH(12);
		private int size;
		DisplaySizes(int s){
			this.size = s;
		}
		public int size(){
			return this.size;
		}
		
		public void set(int i){
			this.size = i;
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dsiplay_message);
		// Show the Up button in the action bar.
		setupActionBar();
		setViewTextSize();

		Intent intent = getIntent();
		ArrayList<DefinitionTable> elem = intent.getParcelableArrayListExtra(MainActivity.EXTRACTED_DOC);
		
		// new async thread start
		TableLayout tl = (TableLayout) findViewById(R.id.forms_table);
		try {
			TableLayout generated = new buildDisplayThread().execute(elem).get();
			new mergeViewsThread(tl, generated).execute();
				
			
			//tl.addView(generated);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void setViewTextSize() {
		int screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;

		String toastMsg;
		switch (screenSize) {
		case 4:
			DisplaySizes.DEFINITION.set(30);
			DisplaySizes.HEADER.set(30);
			DisplaySizes.TABLE.set(22);
			toastMsg = "XLarge screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			DisplaySizes.DEFINITION.set(25);
			DisplaySizes.HEADER.set(25);
			DisplaySizes.TABLE.set(20);
			toastMsg = "Large screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			DisplaySizes.DEFINITION.set(15);
			DisplaySizes.HEADER.set(15);
			DisplaySizes.TABLE.set(10);
			toastMsg = "Normal screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			DisplaySizes.DEFINITION.set(12);
			DisplaySizes.HEADER.set(12);
			DisplaySizes.TABLE.set(8);
			toastMsg = "Small screen";
			break;
		default:
			DisplaySizes.DEFINITION.set(15);
			DisplaySizes.HEADER.set(15);
			DisplaySizes.TABLE.set(10);
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

	public class buildDisplayThread extends AsyncTask<ArrayList<DefinitionTable>, Integer, TableLayout> {
		private ProgressDialog progressDialog;

		public TableLayout doInBackground(ArrayList<DefinitionTable>... params) {
			TableLayout tl = (TableLayout) findViewById(R.id.forms_table);
			//TableLayout tl = null;
			ArrayList<DefinitionTable> elem = params[0];

			DisplayMetrics mDisplayMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
			
			try {
				// doc = not null
				
				// build tables
				DefinitionTable[] message = new DefinitionTable[elem.size()];
				for(int i=0; i<elem.size(); i++){
					message[i] = elem.get(i);
				}

				tl = new TableLayout(DsiplayMessageActivity.this);

				// layout for rows (only used for headers at this point)
				TableLayout.LayoutParams rowlayout = new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT);
				rowlayout.setMargins(20, 10, 20, 10);

				for (int i = 0; i < message.length; i++) {
					
					if (message[i].getCols() > 1) {			// if there is more than 1 col : a table or definition
						

						TableLayout.LayoutParams tlayout1 = new TableLayout.LayoutParams(
								TableLayout.LayoutParams.WRAP_CONTENT,
								TableLayout.LayoutParams.WRAP_CONTENT);
						tlayout1.setMargins(20, 10, 20, 10);
						TableRow.LayoutParams trowlayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
								TableRow.LayoutParams.WRAP_CONTENT, 
								1f);
						
						TableLayout table1 = new TableLayout(DsiplayMessageActivity.this);
						
						if(message[i].getCols() == 2 && message[i].getDataSize() == 2){		// magic number - CHANGE TO CONST
							table1.setColumnShrinkable(0, false);		// no shrink on definition
						} else {
							table1.setColumnShrinkable(0, true);		// do shrink first col for tables
						}
						
						for(int j=0; j<message[i].getDataSize(); ){		// iterate thru table obj to populate table
																		// by row
							TableRow trow1 = new TableRow(DsiplayMessageActivity.this);
							
							for(int k=0; k<message[i].getCols(); k++, j++){		// by col
								
								TextView tv1 = new TextView(DsiplayMessageActivity.this);
								
								if(message[i].getCols() == 2 && message[i].getDataSize() == 2){
									tv1.setLayoutParams(trowlayout);
									tv1.setMinimumWidth(mDisplayMetrics.widthPixels/2);
								}
								else{
									tv1.setLongClickable(true);
									tv1.setOnLongClickListener(listener);
									tv1.setTag(message[i].popData(j));
								}
								tv1.setText(message[i].popData(j).getData().replace(',', '\n'));
								tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySizes.TABLE.size());
								if(message[i].popData(j).isHeader()){
									tv1.setTextColor(Color.RED);
								} else {
									tv1.setTextColor(Color.BLACK);
								}
								tv1.setPadding(10, 5, 10, 5);
								tv1.setTextIsSelectable(true);

								trow1.addView(tv1);
								trow1.setLayoutParams(tlayout1);
								trow1.setGravity(Gravity.CENTER_HORIZONTAL);
								
							}
							
							table1.addView(trow1);
							//table1.setColumnShrinkable(0, true);
							table1.setPadding(0, 10, 0, 30);
						}
						
						tl.addView(table1);
					}
					
					else {		// if not a table or def, must be a heading or paragraph

						TableRow trow0 = new TableRow(DsiplayMessageActivity.this);
						TextView tv0 = new TextView(DsiplayMessageActivity.this);

						tv0.setText(message[i].popData().getData());
						tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySizes.HEADER.size());
						tv0.setGravity(Gravity.CENTER_HORIZONTAL);
						tv0.setWidth(mDisplayMetrics.widthPixels);
						if(message[i].popData().isHeader()){
							tv0.setTextColor(Color.GREEN);
						} else {
							tv0.setTextColor(Color.BLACK);
						}
						tv0.setTextIsSelectable(true);

						trow0.setLayoutParams(rowlayout);
						trow0.addView(tv0);

						tl.addView(trow0);
						
						//view.addView(tl);
					}
					
				}
				
				
			} catch (Exception e) {
				tl = null;
				e.printStackTrace();
			}
			return tl;
		}

		public void onPreExecute() {
			super.onPreExecute();
			//progressDialog = ProgressDialog.show(DsiplayMessageActivity.this,"Please Wait", "Building results...", true, true);
		}

		public void onPostExecute(TableLayout result) {
			if (result != null) {
				
			} else {
				Toast.makeText(DsiplayMessageActivity.this, "ERROR", Toast.LENGTH_LONG).show();
				// print error stack
			}
			//progressDialog.dismiss();
		}
	}
	
	public class mergeViewsThread extends AsyncTask<TableLayout, Integer, TableLayout>{
		private TableLayout origin;
		private TableLayout generated;
		mergeViewsThread(TableLayout o, TableLayout g){
			super();
			this.origin = o;
			this.generated = g;
		}
		private ProgressDialog progressDialog;
		public TableLayout doInBackground(TableLayout... params) {
			//origin = null;
			try{
				//origin = params[0];
				//generated = params[1];
				
				origin.addView(generated);
			}
			catch(Exception e){
				origin = null;
			}
			return origin;
		}
		public void onPreExecute() {
			super.onPreExecute();
			//progressDialog = ProgressDialog.show(DsiplayMessageActivity.this,"Please Wait", "Displaying...", true, true);
		}
		public void onPostExecute(TableLayout result) {
			if (result != null) {
				
			} else {
				Toast.makeText(DsiplayMessageActivity.this, "ERROR", Toast.LENGTH_LONG).show();
			}
			//progressDialog.dismiss();
		}
	}
	
	private View.OnLongClickListener listener = new View.OnLongClickListener() {
	    public boolean onLongClick(View v) {
	        TextView clickedWord = (TextView) v;
	        //clickedWord.sets
	        DefinitionData def = (DefinitionData) clickedWord.getTag();
	        Toast.makeText(DsiplayMessageActivity.this, def.getProcessedData(), Toast.LENGTH_LONG).show();
	        //showPopup(DsiplayMessageActivity.this, v);
	        
	       
	        
	        int[] location = new int[2];  
            //clickedWord.getLocationOnScreen(location);
            v.getLocationInWindow(location);
            
            

            Point point = new Point();
            point.x = location[0];
            point.y = location[1];
	        
            //Toast.makeText(DsiplayMessageActivity.this, point.toString(), Toast.LENGTH_LONG).show();
            
	       showPopup(DsiplayMessageActivity.this, point, v);
	        return true;
	    }
	};
	
	@SuppressWarnings("deprecation")
	private void showPopup(Activity context, Point p, View v) {
		
		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_window);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_window, null);
		
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		
	    PopupWindow popup = new PopupWindow(context);
	    
	    popup.setContentView(layout);
	    popup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
	    popup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
	    popup.setFocusable(true);
	    
	    int OFFSET_X = 0;
	    int OFFSET_Y = -196;
	    
	    if(p.x + (v.getWidth()) >= (mDisplayMetrics.widthPixels/7)*6){
	    	layout.setBackgroundResource(R.drawable.balloon_right);
	    } else { 
	    	if(p.x <= (mDisplayMetrics.widthPixels/7)*1) {
	    	layout.setBackgroundResource(R.drawable.balloon_left);
	    }	else{
		    OFFSET_X = -120; // use other constant here to center
	    }
	    }
	    
	    popup.setBackgroundDrawable(new BitmapDrawable());

	    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
	    
	    /*
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.definition_popup, popup.getMenu());
	    popup.show();*/
	}
}