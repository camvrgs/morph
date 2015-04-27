package com.cameronvargas.app.morphanalysis;

import com.cameronvargas.app.morphanalysis.R;
import com.cameronvargas.app.morphanalysis.R.layout;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

public class DefinitionFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_definition, container, false);
	}

}
