package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.spi.XmlWriter;
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

import dao.Employee;

public class XMLHandler {
	private static String XML_NAME = "employees.xml";

	private static final class EmployeeXMLInputHandler extends DefaultHandler {
		private List<Employee> employees;
		private String actualPosition;
		private Map<String, String> m = new HashMap<String, String>();

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
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

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
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
