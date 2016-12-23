package com.gaejexperiments.db;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ReportsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/xml");
		String strResult = "";
		String strData = "";
		try {
			String type = (String) req.getParameter("type");
			if (type == null) {
				strResult = "No Report Type specified.";
				throw new Exception(strResult);
			} else if (type.equals("HEALTHINCIDENTCOUNT_CURRENT_MONTH")) {
				String strHealthIncident = (String) req.getParameter("healthincident");
				String strPinCode = (String) req.getParameter("pincode");
				Map<String, Integer> _healthReports = DBUtils.getHealthIncidentCountForCurrentMonth(strHealthIncident,
						strPinCode);
				if (_healthReports == null) {
				} else {
					Iterator<String> it = _healthReports.keySet().iterator();
					while (it.hasNext()) {
						String healthIncident = (String) it.next();
						int healthIncidentCount = 0;
						Integer healthIncidentCountObject = _healthReports.get(healthIncident);
						if (healthIncidentCountObject == null) {
							healthIncidentCount = 0;
						} else {
							healthIncidentCount = healthIncidentCountObject.intValue();
						}
						if (healthIncidentCount > 0)
							strData += "<HealthIncident><name>" + healthIncident + "</name>" + "<count>"
									+ healthIncidentCount + "</count></HealthIncident>";
					}
				}
				strResult = "<Response><Status>success</Status><StatusDescription></StatusDescription><Result>"
						+ strData + "</Result></Response>";
			}
		} catch (Exception ex) {
			strResult = "<Response><Status>fail</Status><StatusDescription>" + "Error in executing operation : "
					+ ex.getMessage() + "</StatusDescription></Response>";
		}
		resp.getWriter().println(strResult);
	}
}