package com.cameronvargas.app.morphanalysis;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class DefinitionTable implements Parcelable {
	private int columns;
	private ArrayList<DefinitionData> tableData = new ArrayList<DefinitionData>();

	DefinitionTable(int c) {
		if (c != 0) {
			columns = c;
		} else {
			columns = 1;
		}
	}
	DefinitionTable(){
		columns = 1;
	}
	
	public int getCols(){
		return this.columns;
	}
	
    private void setCols(int cols) {
    	columns = cols;
	}
	
	public int getDataSize(){
		return this.tableData.size();
	}

	public void pushData(DefinitionData dd) {
		tableData.add(dd);
	}
	
	public DefinitionData popData(int index) {
		return this.tableData.get(index);
	}
	
	public DefinitionData popData() {
		return this.tableData.get(0);
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
	
	
	// parcelable interface
	private DefinitionTable(Parcel in) {
        // This order must match the order in writeToParcel()
		this.setCols(in.readInt());
		in.readTypedList(this.tableData, DefinitionData.CREATOR);
    }

	public void writeToParcel(Parcel out, int flags) {
        // Again this order must match the Question(Parcel) constructor
        out.writeInt(this.getCols());
        out.writeTypedList(this.tableData);
    }

    // Just cut and paste this for now
    public int describeContents() {
        return 0;
    }

    // Just cut and paste this for now
    public static final Parcelable.Creator<DefinitionTable> CREATOR = new Parcelable.Creator<DefinitionTable>() {
        public DefinitionTable createFromParcel(Parcel in) {
            return new DefinitionTable(in);
        }

        public DefinitionTable[] newArray(int size) {
            return new DefinitionTable[size];
        }
    };
}
