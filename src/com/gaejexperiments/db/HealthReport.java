package com.gaejexperiments.db;

import java.util.Date;
import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class HealthReport {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	private String pinCode;
	@Persistent
	private String healthIncident;
	@Persistent
	private String status;
	@Persistent
	private Date reportDateTime;

	public HealthReport(String pinCode, String healthIncident, String status, Date reportDateTime) {
		super();
		this.pinCode = pinCode;
		this.healthIncident = healthIncident;
		this.status = status;
		this.reportDateTime = reportDateTime;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getHealthIncident() {
		return healthIncident;
	}

	public void setHealthIncident(String healthIncident) {
		this.healthIncident = healthIncident;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}
}