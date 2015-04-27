package com.cameronvargas.app.morphanalysis;

import java.util.ArrayList;

public class DefinitionTable {
	private int columns;
	private ArrayList<DefinitionData> tableData = new ArrayList<DefinitionData>();

	DefinitionTable(int c) {
		if (c != 0) {
			columns = c;
		} else {
			columns = 1;
		}
	}

	public void pushData(DefinitionData dd) {
		tableData.add(dd);
	}

	public DefinitionData[] getDefDataArray() {
		return (DefinitionData[]) tableData.toArray();
	}

	public String[] getRawStringArray() {
		String[] strArray = new String[tableData.size()];
		for (int i = 0; i < tableData.size(); i++) {
			strArray[i] = tableData.get(i).getData();
		}
		return strArray;
	}

	public String[] getStringArray() {
		String[] strArray = new String[tableData.size()];
		for (int i = 0; i < tableData.size(); i++) {
			strArray[i] = tableData.get(i).getProcessedData();
		}
		return strArray;
	}

}
