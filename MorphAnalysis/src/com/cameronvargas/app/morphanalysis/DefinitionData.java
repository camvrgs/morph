package com.cameronvargas.app.morphanalysis;

import android.os.Parcel;
import android.os.Parcelable;


public class DefinitionData implements Parcelable {
	private int isHeader;	// change to int?
	private String data;

	DefinitionData(int h, String d) {
		setHeader(h);
		setData(d);
	}

	DefinitionData(boolean b, String d) {
		setHeader(b);
		setData(d);
	}
	
	DefinitionData(String d) {
		this(0, d);
	}

	public boolean isHeader() {
		if (getHeader() == 1){
			return true;
		} else{
			return false;
		}
	}

	public void setHeader(int is) {
		this.isHeader = is;
	}
	
	public int getHeader() {
		return isHeader;
	}
	
	public void setHeader(boolean is) {
		this.isHeader = is ? 1 : 0;
	}

	public String getData() {
		return data;
	}

	public String getProcessedData() {
		return removeExtras(data);
	}

	public void setData(String data) {
		if (data != null) {
			this.data = data;
		} else {
			this.data = "";
		}
		this.data = data;
	}

	private String removeExtras(String d) {
		return d.replace("\'", "").replace("\"", "");
	}
	
	
	// Parcelable interface
		public DefinitionData(Parcel in) {
	        // This order must match the order in writeToParcel()
			setHeader(in.readInt());
			setData(in.readString());
	    }

	    public void writeToParcel(Parcel out, int flags) {
	        // must match the Question(Parcel) constructor
	        out.writeInt(getHeader());
	        out.writeString(getData());
	    }

	    // 
	    public int describeContents() {
	        return 0;
	    }

	    // 
	    public static final Parcelable.Creator<DefinitionData> CREATOR = new Parcelable.Creator<DefinitionData>() {
	        public DefinitionData createFromParcel(Parcel in) {
	            return new DefinitionData(in);
	        }
	        public DefinitionData[] newArray(int size) {
	            return new DefinitionData[size];
	        }
	    };
}
