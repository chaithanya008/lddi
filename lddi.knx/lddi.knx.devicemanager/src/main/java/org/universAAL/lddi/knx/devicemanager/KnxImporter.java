/*
     Copyright 2010-2014 AIT Austrian Institute of Technology GmbH
	 http://www.ait.ac.at

     See the NOTICE file distributed with this work for additional
     information regarding copyright ownership

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/

package org.universAAL.lddi.knx.devicemanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.osgi.service.log.LogService;
import org.universAAL.lddi.knx.utils.KnxGroupAddress;

/**
 * The class takes a ets4 export file as inputstream and creates a list of
 * KnxGroupAddress objects including building part information if available Name
 * and description of building parts are editable in ETS; type is fixed: Floor,
 * Room, Corridor, Cabinet, Stairway or Building Part
 *
 * This class is only tested with ETS4 XML data schema 1.0 and 1.1 ! Higher
 * schema versions may require refinement of this code !!
 *
 * @author marcus@openremote.org
 * @author Thomas Fuxreiter (foex@gmx.at)
 */
public class KnxImporter {
	private LogService logger;

	/**
	 * @param logger
	 */
	public KnxImporter(LogService logger) {
		this.logger = logger;
	}

	/**
	 * This method can import ETS4 XML data schemas version 1.0 and 1.1 Higher
	 * schema versions may require refinement of this code !!
	 *
	 * @param inputStream
	 *            of ETS4 export file (zip file)
	 * @return list of KnxGroupAddress objects
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<KnxGroupAddress> importETS4Configuration(InputStream inputStream) throws Exception {

		List<KnxGroupAddress> result = new ArrayList<KnxGroupAddress>();
		String xmlData = null;
		DecimalFormat df = new DecimalFormat("000");
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		ZipInputStream zin = new ZipInputStream(inputStream);
		ZipEntry zipEntry = zin.getNextEntry();
		while (zipEntry != null) {
			if (zipEntry.getName().endsWith("/0.xml")) {
				xmlData = convertStreamToString(zin);
				break;
			}
			zipEntry = zin.getNextEntry();
		}

		if (xmlData != null) {
			// Remove UTF-8 Byte-order mark from the beginning of the data
			xmlData = xmlData.trim().replaceFirst("^([\\W]+)<", "<");

			// parse the XML as a W3C Document
			StringReader in = new StringReader(xmlData);
			document = builder.build(in);

			// Query all <GroupAddress> elements
			XPath xpathGA = XPath.newInstance("//knx:GroupAddress");
			// xpathGA.addNamespace("knx", "http://knx.org/xml/project/10");
			xpathGA.addNamespace("knx", document.getRootElement().getNamespace().getURI());

			// Store all <GroupAddress> elements in xresultGA
			List<Element> xresultGA = xpathGA.selectNodes(document);

			// // If list is empty try any namespace
			// if (xresultGA.isEmpty()) {
			// xpathGA = XPath.newInstance("//*[local-name()='GroupAddress']");
			// xresultGA = xpathGA.selectNodes(document);
			// }

			for (Element element : xresultGA) {
				String id = element.getAttributeValue("Id");
				String name = element.getAttributeValue("Name");
				String address = element.getAttributeValue("Address");
				// Convert address in readable KNX format M/S/G
				String levelAddress = getAddressFromInt(Integer.parseInt(address));
				String description = element.getAttributeValue("Description");
				String comment = element.getAttributeValue("Comment");
				String dpt = null;

				String deviceId = null;
				String bpType = null; // BuildingPart Type
				String bpName = null; // BuildingPart Name
				String bpDescription = null;

				// Query referenced <Send> element within ComObjectInstanceRef
				// element which holds DPT
				XPath xpathCO = XPath.newInstance("//knx:Send[@GroupAddressRefId='" + id + "']/../..");
				// xpathCO.addNamespace("knx", "http://knx.org/xml/project/10");
				xpathCO.addNamespace("knx", document.getRootElement().getNamespace().getURI());

				List<Element> resultSGA = xpathCO.selectNodes(document);

				if (resultSGA.size() == 0) {
					// The reference could also be in the <Receive> element
					xpathCO = XPath.newInstance("//knx:Receive[@GroupAddressRefId='" + id + "']/../..");
					// xpathCO.addNamespace("knx",
					// "http://knx.org/xml/project/10");
					xpathCO.addNamespace("knx", document.getRootElement().getNamespace().getURI());

					resultSGA = xpathCO.selectNodes(document);
				}

				if (resultSGA.size() > 0) {
					dpt = resultSGA.get(0).getAttributeValue("DatapointType");
					if (dpt != null && dpt != "") {
						StringTokenizer st = new StringTokenizer(dpt, "-");
						st.nextElement();
						try {
							dpt = st.nextToken() + "." + df.format(Integer.parseInt(st.nextToken()));
						} catch (Exception e) {
							dpt = null;
						}
					} else {
						this.logger.log(LogService.LOG_WARNING,
								"No KNX data type specified for devices in groupAddress " + levelAddress
										+ " - Skipping this groupAddress!");
						continue;
						// dpt = null;
					}
				} else {
					this.logger.log(LogService.LOG_WARNING, "No corresponding device found in KNX config"
							+ " for groupAddress " + levelAddress + " - Skipping this empty groupAddress!");
					continue;
				}

				// BuildingPart are linked to devices
				// from GA go to DeviceInstance Id, then search for BuildingPart

				// Query referenced DeviceInstance element which holds DPT
				XPath xpathDI = XPath.newInstance("//knx:Send[@GroupAddressRefId='" + id + "']/../../../..");
				// xpathDI.addNamespace("knx", "http://knx.org/xml/project/10");
				xpathDI.addNamespace("knx", document.getRootElement().getNamespace().getURI());

				List<Element> resultDI = xpathDI.selectNodes(document);
				if (resultDI.size() > 0) {
					// TODO: Only the first occurence of GroupAddressRefId is
					// used!! 1 group address can include several devices which
					// have different locations!! How solve this??
					deviceId = resultDI.get(0).getAttributeValue("Id");
					if (deviceId != null && deviceId != "") {
						// now, search for BuildingPart
						// Query referenced BuildingPart element which holds
						// name and type
						XPath xpathBP = XPath.newInstance("//knx:DeviceInstanceRef[@RefId='" + deviceId + "']/..");
						// xpathBP.addNamespace("knx",
						// "http://knx.org/xml/project/10");
						xpathBP.addNamespace("knx", document.getRootElement().getNamespace().getURI());

						List<Element> xresultBP = xpathBP.selectNodes(document);

						if (xresultBP.size() > 0) {
							bpName = xresultBP.get(0).getAttributeValue("Name");
							bpType = xresultBP.get(0).getAttributeValue("Type");
							bpDescription = xresultBP.get(0).getAttributeValue("Description");
						}
					}
				}

				// result.add(new KnxGroupAddress(dpt, levelAddress, name));
				result.add(new KnxGroupAddress(dpt, levelAddress, name, description, comment, bpType, bpName,
						bpDescription));
				// LOGGER.debug("Created GroupAddress: " + levelAddress + " - "
				// + name + " - " + dpt);
			}
		}

		return result;
	}

	public List<KnxGroupAddress> importETS3GroupAddressCsvExport(InputStream inputStream) throws Exception {
		List<KnxGroupAddress> result = new ArrayList<KnxGroupAddress>();

		String str;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
		while ((str = in.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(str, ";");
			String name = st.nextToken();
			String ga = st.nextToken();
			if (ga.indexOf("-") == -1) {
				result.add(new KnxGroupAddress(null, ga, name));
			}
		}

		return result;
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	/**
	 * Transform a plain integer KNX group address to 1/2/3 format.
	 *
	 * @param knxaddress
	 * @return readable group address
	 */
	public static String getAddressFromInt(int knxaddress) {
		int maingroup, subgroup, group;
		// extract values
		maingroup = (knxaddress >> 11) & 0x0f;
		subgroup = (knxaddress >> 8) & 0x07;
		group = knxaddress & 0xff;
		String erg = "" + maingroup + "/" + subgroup + "/" + group;
		return erg;
	}

}
