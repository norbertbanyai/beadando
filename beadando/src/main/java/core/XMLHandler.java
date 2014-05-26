package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.joda.time.LocalDate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import static core.Main.logger;

import dao.Employee;

/**
 * The core of the class {@code DataLoader}. Provides the XML handling.
 * User class: {@link DataLoader}.
 */
public class XMLHandler {
	
	/**
	 * The path of the XML file.
	 */
	private static String XML_NAME;

	/**
	 * Class representing a handler for {@code Employee}'s objects in an XML file.
	 * 
	 * @see DefaultHandler
	 * @see XMLHandler#loadEmployeesFromXML(String)
	 */
	private static final class EmployeeXMLInputHandler extends DefaultHandler {
		/**
		 * List of {@code Employee}s.
		 */
		private List<Employee> employees;
		
		/**
		 * The actual position of the parsing.
		 */
		private String actualPosition;
		
		/**
		 * A map that contains the values for the employees.
		 * <p>For example this pair: id, 100 or this pair: salary, 5000</p>
		 */
		private Map<String, String> m = new HashMap<String, String>();

		/**
		 * Returns the list of the employees read from the XML file.
		 * @return the employees' list
		 */
		public List<Employee> getEmployeeList() {
			return employees;
		}
		
		@Override
		public void startDocument() throws SAXException {
			employees = new ArrayList<Employee>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			actualPosition = qName;
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			actualPosition = null;
			if ("Employee".equals(qName)) {
				employees.add(new Employee(Integer.valueOf(m.get("id")), m
						.get("name"), new Date(new LocalDate(m.get("hireDate"))
						.toDate().getTime()), new BigDecimal(m.get("salary")),
						m.get("department")));
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			m.put(actualPosition, new String(ch, start, length));
		}
	}

	/**
	 * Reads an XML file, and puts every read object into {@link EmployeeXMLInputHandler#employees}.
	 * <p>After parsing the document, returns the list of the employees.</p>
	 * @see EmployeeXMLInputHandler
	 * 
	 * @param xmlPath the absolute path of the XML file we want to read from
	 * @return the list of the employees which are in the XML file
	 */
	public static List<Employee> loadEmployeesFromXML(String xmlPath) {
		XMLHandler.XML_NAME = xmlPath;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		List<Employee> l = null;
		try {
			SAXParser parser = spf.newSAXParser();
			DefaultHandler h = new EmployeeXMLInputHandler();
			parser.parse(new FileInputStream(XML_NAME), h);
			l = ((EmployeeXMLInputHandler) h).getEmployeeList();
		} catch (ParserConfigurationException e) {
			logger.error("Error wile parsing XML:" + e.getMessage(), e);
		} catch (SAXException e) {
			logger.error("Error wile parsing XML:" + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error("Error wile parsing XML:" + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error wile parsing XML:" + e.getMessage(), e);
		}
		return l;
	}

	/**
	 * Writes the given {@code Employee} list into an XML file.
	 * 
	 * @param xmlPath the absolute path of the XML file we want to write to
	 * @param l the list of the employees we want to write to {@code xmlPath}
	 */
	public static void loadEmployeesToXML(String xmlPath, List<Employee> l) {
		XMLHandler.XML_NAME = xmlPath;
		try {
			List<Employee> list = l;
			if (null != list && !list.isEmpty()) {
				PrintWriter wXml = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(XML_NAME), "utf-8"));
				XMLOutputFactory xof = XMLOutputFactory.newInstance();
				XMLStreamWriter xmlsw = xof.createXMLStreamWriter(wXml);
				xmlsw.writeStartDocument("UTF-8", "1.0");
				xmlsw.writeCharacters("\n");
				xmlsw.writeStartElement("Employees");
				xmlsw.writeCharacters("\n");
				for (Employee employee : list) {
					xmlsw.writeCharacters("\t");
					xmlsw.writeStartElement("Employee");
					xmlsw.writeCharacters("\n\t\t");

					xmlsw.writeStartElement("id");
					xmlsw.writeCharacters(String.valueOf(employee.getId()));
					xmlsw.writeEndElement();
					xmlsw.writeCharacters("\n\t\t");

					xmlsw.writeStartElement("name");
					xmlsw.writeCharacters(employee.getName());
					xmlsw.writeEndElement();
					xmlsw.writeCharacters("\n\t\t");

					xmlsw.writeStartElement("hireDate");
					xmlsw.writeCharacters(employee.getHireDate().toString());
					xmlsw.writeEndElement();
					xmlsw.writeCharacters("\n\t\t");

					xmlsw.writeStartElement("salary");
					xmlsw.writeCharacters(employee.getSalary().toString());
					xmlsw.writeEndElement();
					xmlsw.writeCharacters("\n\t\t");

					xmlsw.writeStartElement("department");
					xmlsw.writeCharacters(employee.getDepartment());
					xmlsw.writeEndElement();
					xmlsw.writeCharacters("\n\t");

					xmlsw.writeEndElement();
					xmlsw.writeCharacters("\n\n");
				}
				xmlsw.writeEndElement();
				xmlsw.writeCharacters("\n");
				xmlsw.writeEndDocument();

				xmlsw.flush();
				xmlsw.close();
			}
		} catch (XMLStreamException e) {
			logger.error("Error while writing into an XML file:" + e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error("Error while writing into an XML file:" + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error("Error while writing into an XML file:" + e.getMessage(), e);
		}
	}
}
