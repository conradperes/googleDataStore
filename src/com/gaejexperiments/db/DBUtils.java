package com.gaejexperiments.db;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class DBUtils {
	public static final Logger _logger = Logger.getLogger(DBUtils.class.getName());

	// Currently we are hardcoding this list. But this could also be retrieved
	// from
	// database
	public static String getHealthIncidentMasterList() throws Exception {
		return "Flu,Cough,Cold";
	}

	/**
	 * This method persists a record to the database.
	 */
	public static void saveHealthReport(HealthReport healthReport) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(healthReport);
			_logger.log(Level.INFO, "Health Report has been saved");
		} catch (Exception ex) {
			_logger.log(Level.SEVERE, "Could not save the Health Report. Reason : " + ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}
	}

	/**
	 * This method gets the count all health incidents in an area
	 * (Pincode/Zipcode) for the current month
	 * 
	 * @param healthIncident
	 * @param pinCode
	 * @return A Map containing the health incident name and the number of cases
	 *         reported for it in the current month
	 */
	public static Map<String, Integer> getHealthIncidentCountForCurrentMonth(String healthIncident, String pinCode) {
		Map<String, Integer> _healthReport = new HashMap<String, Integer>();
		PersistenceManager pm = null;
		// Get the current month and year
		Calendar c = Calendar.getInstance();
		int CurrentMonth = c.get(Calendar.MONTH);
		int CurrentYear = c.get(Calendar.YEAR);
		try {
			// Determine if we need to generate data for only one health
			// Incident or ALL
			String[] healthIncidents = {};
			if (healthIncident.equalsIgnoreCase("ALL")) {
				String strHealthIncidents = getHealthIncidentMasterList();
				healthIncidents = strHealthIncidents.split(",");
			} else {
				healthIncidents = new String[] { healthIncident };
			}
			pm = PMF.get().getPersistenceManager();
			Query query = null;
			// If Pincode (Zipcode) is ALL, we need to retrieve all the records
			// irrespective of Pincode
			if (pinCode.equalsIgnoreCase("ALL")) {
				// Form the query
				query = pm.newQuery(HealthReport.class,
						" healthIncident == paramHealthIncident && reportDateTime >= paramStartDate && reportDateTime < paramEndDate && status == paramStatus");
				// declare parameters used above
				query.declareParameters(
						"String paramHealthIncident, java.util.Date paramStartDate, java.util.Date paramEndDate, String paramStatus");
			} else {
				query = pm.newQuery(HealthReport.class,
						" healthIncident == paramHealthIncident && pinCode == paramPinCode && reportDateTime >= paramStartDate && reportDateTime <paramEndDate && status == paramStatus");
				// declare params used above
				query.declareParameters(
						"String paramHealthIncident, String paramPinCode, java.util.Date paramStartDate, java.util.Date paramEndDate, String paramStatus");
			}
			// For each health incident (i.e. Cold Flu Cough), retrieve the
			// records
			for (int i = 0; i < healthIncidents.length; i++) {
				int healthIncidentCount = 0;
				// Set the From and To Dates i.e. 1st of the month and 1st day
				// of next month
				Calendar _cal1 = Calendar.getInstance();
				_cal1.set(CurrentYear, CurrentMonth, 1);
				Calendar _cal2 = Calendar.getInstance();
				_cal2.set(CurrentYear, CurrentMonth + 1, 1);
				List<HealthReport> codes = null;
				if (pinCode.equalsIgnoreCase("ALL")) {
					// Execute the query by passing in actual data for the
					// filters
					codes = (List<HealthReport>) query.executeWithArray(healthIncidents[i], _cal1.getTime(),
							_cal2.getTime(), "ACTIVE");
				} else {
					codes = (List<HealthReport>) query.executeWithArray(healthIncidents[i], pinCode, _cal1.getTime(),
							_cal2.getTime(), "ACTIVE");
				}
				// Iterate through the results and increment the count
				for (Iterator iterator = codes.iterator(); iterator.hasNext();) {
					HealthReport _report = (HealthReport) iterator.next();
					healthIncidentCount++;
				}
				// Put the record in the Map data structure
				_healthReport.put(healthIncidents[i], new Integer(healthIncidentCount));
			}
			return _healthReport;
		} catch (Exception ex) {
			return null;
		} finally {
			pm.close();
		}
	}
}