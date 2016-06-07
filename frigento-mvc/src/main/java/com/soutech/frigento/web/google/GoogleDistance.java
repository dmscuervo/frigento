package com.soutech.frigento.web.google;

import java.io.Serializable;

public class GoogleDistance implements Serializable {

	private static final long serialVersionUID = 3790653708183588868L;
	
	private String[] origin_addresses;
	private String[] destination_addresses;
	private Row[] rows;
	private String status;
	
	public String[] getOrigin_addresses() {
		return origin_addresses;
	}
	public void setOrigin_addresses(String[] origin_addresses) {
		this.origin_addresses = origin_addresses;
	}
	public String[] getDestination_addresses() {
		return destination_addresses;
	}
	public void setDestination_addresses(String[] destination_addresses) {
		this.destination_addresses = destination_addresses;
	}
	public Row[] getRows() {
		return rows;
	}
	public void setRows(Row[] rows) {
		this.rows = rows;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}