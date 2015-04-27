package com.cameronvargas.app.morphanalysis;

public class DefinitionData {
	private boolean isHeader;
	private String data;

	DefinitionData(boolean h, String d) {
		setHeader(h);
		setData(d);
	}

	DefinitionData(String d) {
		setHeader(false);
		setData(d);
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
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
}
