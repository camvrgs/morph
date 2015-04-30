package com.cameronvargas.app.morphanalysis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SearchContextDialog extends DialogFragment {
	
	private final String url = "http://www.lingvo-online.ru/ru/Examples/ru-en/";
	private String searchWord;
	private String grammarTxt;
	private boolean header;
	
	public void setSearch(String s){
		searchWord = s;
	}
	
	public void setGrammar(String s){
		grammarTxt = s;
	}
	
	public void setHeader(boolean b){
		header = b;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View layout = inflater.inflate(R.layout.popup_context_window, null);

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(layout)
	    
	    		.setPositiveButton(R.string.open_browser, new DialogInterface.OnClickListener() {
	    			@Override
	               	public void onClick(DialogInterface dialog, int id) {
	                   // open search in browser
	               	}
	    		})
	           
	           	.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
	           		public void onClick(DialogInterface dialog, int id) {
	           			SearchContextDialog.this.getDialog().cancel();
	           		}
	           	});
	    
	    TextView title = new TextView(getActivity());
	    title.setText(searchWord);
	 	title.setBackgroundColor(Color.DKGRAY);
	 	title.setPadding(10, 25, 10, 25);
	 	title.setGravity(Gravity.CENTER);
	 	title.setTextColor(Color.WHITE);
	 	title.setTextSize(20);
	 	
	 	TextView grammar = (TextView) layout.findViewById(R.id.context_grammar);
	 	grammar.setText(grammarTxt);

	 	builder.setCustomTitle(title);
	 	
	 	runSearch(searchWord);
	    
	    return builder.create();
	}
	
	private void runSearch(String s){
		String searchUrl = url + s;
		
		// add jsoup parsing here
	}
	
}