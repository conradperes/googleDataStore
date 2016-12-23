package com.gaejexperiments.db;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class PostHealthIncidentServlet extends HttpServlet {
	public static final Logger _logger = Logger.getLogger(PostHealthIncidentServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		String strResponse = "";
		String strHealthIncident = "";
		String strPinCode = "";
		try {
			// DO ALL YOUR REQUIRED VALIDATIONS HERE AND THROW EXCEPTION IF
			// NEEDED
			strHealthIncident = (String) req.getParameter("healthincident");
			strPinCode = (String) req.getParameter("pincode");
			String strRecordStatus = "ACTIVE";
			Date dt = new Date();
			HealthReport HR = new HealthReport(strPinCode, strHealthIncident, strRecordStatus, dt);
			DBUtils.saveHealthReport(HR);
			strResponse = "Your Health Incident has been reported successfully.";
		} catch (Exception ex) {
			_logger.severe("Error in saving Health Record : " + strHealthIncident + "," + strPinCode + " : "
					+ ex.getMessage());
			strResponse = "Error in saving Health Record via web. Reason : " + ex.getMessage();
		}
		resp.getWriter().println(strResponse);
	}
}