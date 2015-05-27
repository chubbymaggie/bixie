/*
 * 
 * Copyright (C) 2011 Martin Schaef and Stephan Arlt
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package bixie.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bixie.checker.report.Report.FaultExplanation;
import bixie.checker.util.SourceLocation;
import bixie.ws.util.BixieParserException;
import bixie.ws.util.Examples;
import bixie.ws.util.Runner;
import bixie.ws.util.WebserviceReportPrinter;

/**
 * @author arlt, schaef
 */
@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

	String code = null;
	String example_idx = "0";

	protected Set<Integer> supportLines = new HashSet<Integer>();
	protected Map<Integer, String> infeasibleLines = new HashMap<Integer, String>();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			// load all examples
			req.setAttribute("examples",
					Examples.v().getExamples(req.getServletContext()));
			req.setAttribute("exampleIdx", example_idx);
			forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {		
		try {
			// run Bixie
			req.setAttribute("examples",
					Examples.v().getExamples(req.getServletContext()));
			code = req.getParameter("code");
			this.example_idx = req.getParameter("examplecounter");
			req.setAttribute("exampleIdx", example_idx);

			this.supportLines.clear();
			this.infeasibleLines.clear();
			WebserviceReportPrinter reportPrinter = Runner.run(
					req.getServletContext(), code);
			
			for (Entry<Integer, List<FaultExplanation>> entry : reportPrinter
					.getFaultExplanations().entrySet()) {
				for (FaultExplanation im : entry.getValue()) {
					
					for (SourceLocation loc : im.otherLines) {
						this.supportLines.add(loc.StartLine);
					}
					for (SourceLocation loc : im.infeasibleLines) {
						this.supportLines.remove(loc.StartLine);
						String comment = null;
						if (loc.comment != null) {
							if (loc.comment.equals("elseBlock")
									|| loc.comment.equals("elseblock")) {
								comment = "The case where this conditional evaluates to true";
							} else if (loc.comment.equals("thenBlock")
									|| loc.comment.equals("thenblock")) {
								comment = "The case where this conditional evaluates to false";
							} else {								
								comment = "This line";
							}
							if (this.supportLines.size() > 0) {
								comment += " conflicts with the other marked lines";
							} else {
								comment += " can never be executed";
							}
							//add prefix about the severity of the error:
							if (entry.getKey()==0) {
								comment = "ERROR: " + comment;
							} else if (entry.getKey()==1) {
								comment = "Warning: " + comment;
							} else if (entry.getKey()==2) {
								comment = "Unreachable: " + comment;
							} else {
								//don't know.
								System.out.println(entry.getKey());
							}
								
						}
						this.infeasibleLines.put(loc.StartLine, comment);
					}
				}
			}
			
			req.setAttribute("inflines", this.infeasibleLines);
			req.setAttribute("suplines", this.supportLines);

		} catch (BixieParserException e) {
//			for (Entry<Integer, String> entry : e.getErrorMessages().entrySet()) {
//				System.out.println(entry.getKey() + " " + entry.getValue());
//			}
//			e.printStackTrace();
			req.setAttribute("parsererror", e.getErrorMessages());
		} catch (RuntimeException e) {
			e.printStackTrace();
			req.setAttribute("error", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			forward(req, resp);
		}
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp) {
		try {
			// forward request
			req.getRequestDispatcher("index.jsp").forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
