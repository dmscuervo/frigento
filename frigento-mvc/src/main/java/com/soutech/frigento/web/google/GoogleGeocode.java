package com.soutech.frigento.web.google;

import java.io.Serializable;

public class GoogleGeocode implements Serializable {

	private static final long serialVersionUID = -12167840560116486L;
	
	private Result[] results;
	private String status;
	
	public Result[] getResults() {
		return results;
	}

	public void setResults(Result[] results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
