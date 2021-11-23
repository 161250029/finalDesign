package tool;

import entity.*;
import joanaCore.datastructure.Func;
import joanaCore.datastructure.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class DomTool {
 
	public DomTool() {
	}

	public static Document getDocument(String FilePath) {
		Document document = null;
		try {
			File file = new File(FilePath);
			InputStream inputStream = new FileInputStream(file);
			//创建一个document解析工厂
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//实例化一个DocumentBuilder对象
			DocumentBuilder builder = factory.newDocumentBuilder();
			//使用DocumentBuilder对象获取一个Document的对象
			document = builder.parse(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}


	public static NodeList getBugInstanceNodes(String FilePath) {
		Document document = getDocument(FilePath);
		Element element = document.getDocumentElement();  //获得元素节点
		NodeList nodeList = element.getElementsByTagName("BugInstance"); //获取元素节点
		return nodeList;
	}

	public static List<Element> getOWASP_BugInstanceNodes(String FilePath) {
		List<Element> left_bugs = new ArrayList<>();
		NodeList bug_list = getBugInstanceNodes(FilePath);
		for(int i=0; i< bug_list.getLength(); i++){
			Element bugElement = (Element) bug_list.item(i);
		     	if (bugElement.getElementsByTagName("Method").getLength() == 0)
		     		continue;
			NodeList childNodes = bugElement.getChildNodes();
			for(int j = 0; j < childNodes.getLength(); j++){
				if(childNodes.item(j).getNodeType() == Node.ELEMENT_NODE){
					if (childNodes.item(j).getNodeName().equals("Class")) {
						String class_name = ((Element)childNodes.item(j)).getAttribute("classname");
//						if (class_name.length() > 28 &&!class_name.endsWith("Test") && class_name.substring(0 , 28).equals("org.owasp.benchmark.testcode")) {
							left_bugs.add(bugElement);
							continue;
//						}
					  }
					}
				}
		}
		return left_bugs;
	}

	public static List<AlarmDO> getAlarmDOs(String FilePath) {
		List<AlarmDO> alarmDOList = new ArrayList<>();
		List<Element> bugElement = getOWASP_BugInstanceNodes(FilePath);
		for (Element e : bugElement) {
			NodeList childNodes = e.getChildNodes();
			AlarmDO alarmDO = new AlarmDO();
			alarmDO.setPriority(e.getAttribute("priority"));
			alarmDO.setType(e.getAttribute("type"));
			alarmDO.setDesc(e.getAttribute("category"));
			boolean isFirstMethod = true;
			boolean isFirstSourceLine = true;
			for(int j = 0; j < childNodes.getLength(); j++){
				if (childNodes.item(j).getNodeName().equals("Class")) {
					String class_name = ((Element)childNodes.item(j)).getAttribute("classname");
					alarmDO.setClazz(class_name);
				}else if ("Method".equals(childNodes.item(j).getNodeName()) && isFirstMethod){
					isFirstMethod = false;
					alarmDO.setMethod(((Element)childNodes.item(j)).getAttribute("name"));
					alarmDO.setSig(((Element)childNodes.item(j)).getAttribute("signature"));
				}else if("SourceLine".equals(childNodes.item(j).getNodeName()) && isFirstSourceLine){
					isFirstSourceLine = false;
					alarmDO.setStart(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("start")));
					alarmDO.setEnd(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("end")));
					alarmDO.setSourceFile(((Element)childNodes.item(j)).getAttribute("sourcepath"));
					alarmDO.setFileName(((Element)childNodes.item(j)).getAttribute("sourcefile"));
				}
			}

			if (alarmDO.getFileName() != null) {
				alarmDOList.add(alarmDO);
			}
		}
		return alarmDOList;
	}


	public List<ReportEntry> getReportEntries(String FilePath) {
		List<ReportEntry> reportEntries = new ArrayList<>();
		List<Element> bugElement = getOWASP_BugInstanceNodes(FilePath);
		for (Element e : bugElement) {
			NodeList childNodes = e.getChildNodes();
			ReportEntry reportEntry = new ReportEntry();
			reportEntry.setPriority(e.getAttribute("priority"));
			reportEntry.setRank(Integer.parseInt(e.getAttribute("rank")));
			reportEntry.setType(e.getAttribute("type"));
			reportEntry.setPattern(e.getAttribute("category"));
			boolean isFirstMethod = true;
			boolean isFirstSourceLine = true;
			for(int j = 0; j < childNodes.getLength(); j++){
				if (childNodes.item(j).getNodeName().equals("Class")) {
					String class_name = ((Element)childNodes.item(j)).getAttribute("classname");
					reportEntry.setPackageName(class_name.split("\\.")[0]);
				}else if ("Method".equals(childNodes.item(j).getNodeName()) && isFirstMethod){
					isFirstMethod = false;
					reportEntry.setMethodName(((Element)childNodes.item(j)).getAttribute("name"));
					reportEntry.setSignature(((Element)childNodes.item(j)).getAttribute("signature"));
				}else if("SourceLine".equals(childNodes.item(j).getNodeName()) && isFirstSourceLine){
					isFirstSourceLine = false;
					reportEntry.setLineNumber(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("start")));
					reportEntry.setFileName(((Element)childNodes.item(j)).getAttribute("sourcepath"));
				}
			}
			reportEntries.add(reportEntry);
		}
		return reportEntries;
	}

	public static Map<String , List<Integer>> MappingTable(String CobraPath) {
		Document document = getDocument(CobraPath);
		Element element = document.getDocumentElement();  //获得元素节点
		NodeList nodeList = element.getElementsByTagName("vul"); //获取元素节点
		Map<String , List<Integer>> mapping = new HashMap<>();
		for(int i=0; i< nodeList.getLength(); i++) {
			Element bugElement = (Element) nodeList.item(i);
			List<Integer> current = mapping.get(bugElement.getElementsByTagName("file_path").item(0).getTextContent().trim().substring(1));
			if (current == null)
				current = new ArrayList<>();
			current.add(Integer.parseInt(bugElement.getElementsByTagName("line_number").item(0).getTextContent().trim()));
			mapping.put(bugElement.getElementsByTagName("file_path").item(0).getTextContent().trim().substring(1) ,
					current);
		}
		return mapping;
	}

	public static List<OWASPINFO> handleinfo(String report_path) {
		List<OWASPINFO> owaspinfos = new ArrayList<>();
		List<Element> bugElement = getOWASP_BugInstanceNodes(report_path);
		for (Element e : bugElement) {
			NodeList childNodes = e.getChildNodes();
			OWASPINFO owaspinfo = new OWASPINFO();
			owaspinfo.setLevel(Integer.parseInt(e.getAttribute("priority")));
			owaspinfo.setDesc(e.getAttribute("type"));
			boolean isFirstMethod = true;
			boolean isFirstSourceLine = true;
			boolean isValid_Bug = true;
			for (int j = 0; j < childNodes.getLength(); j++) {
				if (childNodes.item(j).getNodeName().equals("Class")) {
					String[] class_split = ((Element) childNodes.item(j)).getAttribute("classname").split("\\.");
					if (class_split[class_split.length - 1].startsWith("BenchmarkTest"))
						owaspinfo.setClass_name(class_split[class_split.length - 1]);
					else {
						isValid_Bug = false;
						break;
					}
				} else if ("Method".equals(childNodes.item(j).getNodeName()) && isFirstMethod) {
					isFirstMethod = false;
					owaspinfo.setMethod(((Element) childNodes.item(j)).getAttribute("name"));
					owaspinfo.setMethod_signature(((Element) childNodes.item(j)).getAttribute("signature"));
				} else if ("SourceLine".equals(childNodes.item(j).getNodeName()) && isFirstSourceLine) {
					isFirstSourceLine = false;
					owaspinfo.setStart(Integer.parseInt(((Element) childNodes.item(j)).getAttribute("start")));
					owaspinfo.setEnd(Integer.parseInt(((Element) childNodes.item(j)).getAttribute("end")));
				}
			}
			if (isValid_Bug)
			     owaspinfos.add(owaspinfo);
		}
		return owaspinfos;
	}



	public static OWASP_RECORD handle_source_label(String report_path) {
		Document document = getDocument(report_path);
		Element element = document.getDocumentElement();
		NodeList vulnerability_nodeList = element.getElementsByTagName("vulnerability");
		NodeList file_nodeList = element.getElementsByTagName("test-number");
		OWASP_RECORD owasp_record = new OWASP_RECORD();
		owasp_record.setTag(Boolean.parseBoolean(vulnerability_nodeList.item(0).getTextContent()));
		owasp_record.setFilename("BenchmarkTest" + file_nodeList.item(0).getTextContent());
		return owasp_record;
	}


	public static Map<String , Boolean> MapFile(String dir_path) {
		List<String> paths = FileTool.findPath(dir_path , "xml");
		Map<String , Boolean> map = new HashMap<>();
		for (String path : paths) {
			OWASP_RECORD owasp_record = handle_source_label(path);
			map.put(owasp_record.getFilename() , owasp_record.isTag());
		}
		return map;
	}

	public static void genenerate_labels(String dir_path , String report_dir_path) throws IOException {
		List<String> labels = new ArrayList<>();
		Map<String , Boolean> mapping = MapFile(report_dir_path);
		List<String> paths = FileTool.findPath(dir_path , "txt");
		System.out.println(paths.size());
		System.out.println(dir_path);
		for (String path : paths) {
			String filename = path.split("\\.")[0];
			String fileprefix = filename.split("#")[0];
			String key = fileprefix.split("\\\\")[fileprefix.split("\\\\").length - 1];
			System.out.println(key);
			if (mapping.get(key))
				labels.add("1");
			else
				labels.add("0");
		}
		FileTool.write_labels(labels , "D:\\ExperimentData\\findsecCode\\labels.txt");
	}

	public static void generate_excel(String dir_path , String reportpath) {
		Map<String ,Boolean> map = MapFile(dir_path);
		List<OWASPINFO> owasp_infos = handleinfo(reportpath);
		for (OWASPINFO owasp_info : owasp_infos) {
			System.out.println(owasp_info.getClass_name());
				owasp_info.setTag(map.get(owasp_info.getClass_name()));
		}
		ExcelUtil.writeExcelWithTitle(owasp_infos
				, "D:\\findsecbugs-v1.4.5-129_report.xlsx");
	}

	public static List<OWASP> getOWASP(String FilePath) {
		List<OWASP> owaspList = new ArrayList<>();
		List<Element> bugElement = getOWASP_BugInstanceNodes(FilePath);
		for (Element e : bugElement) {
			NodeList childNodes = e.getChildNodes();
			OWASP owasp = new OWASP();
			Func function = new Func();
			Location location = new Location();
			owasp.setLevel(Integer.parseInt(e.getAttribute("priority")));
			owasp.setCategory(e.getAttribute("category"));
			owasp.setType(e.getAttribute("type"));
			boolean isFirstMethod = true;
			boolean isFirstSourceLine = true;
			for(int j = 0; j < childNodes.getLength(); j++){
				if (childNodes.item(j).getNodeName().equals("Class")) {
					String class_name = ((Element)childNodes.item(j)).getAttribute("classname");
					function.setClazz(class_name);
				}else if ("Method".equals(childNodes.item(j).getNodeName()) && isFirstMethod){
					isFirstMethod = false;
					function.setMethod(((Element)childNodes.item(j)).getAttribute("name"));
					function.setSig(((Element)childNodes.item(j)).getAttribute("signature"));
				}else if("SourceLine".equals(childNodes.item(j).getNodeName()) && isFirstSourceLine){
					isFirstSourceLine = false;
					location.setStartLine(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("start")));
					location.setEndLine(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("end")));
					location.setSourceFile(((Element)childNodes.item(j)).getAttribute("sourcepath"));
					owasp.setSourcepath(((Element)childNodes.item(j)).getAttribute("sourcepath"));
					owasp.setSourcefile(((Element)childNodes.item(j)).getAttribute("sourcefile"));
				}
			}
			owasp.setFunction(function);
			owasp.setLocation(location);
			owaspList.add(owasp);
		}
		return owaspList;
	}

	public static List<Element> getDefects4j_BugInstanceNodes(String FilePath) {
		List<Element> left_bugs = new ArrayList<>();
		NodeList bug_list = getBugInstanceNodes(FilePath);
		for(int i=0; i< bug_list.getLength(); i++){
			Element bugElement = (Element) bug_list.item(i);
			if (bugElement.getElementsByTagName("Method").getLength() == 0)
				continue;
			NodeList childNodes = bugElement.getChildNodes();
			for(int j = 0; j < childNodes.getLength(); j++){
				if(childNodes.item(j).getNodeType() == Node.ELEMENT_NODE){
					if (childNodes.item(j).getNodeName().equals("Class")) {
						left_bugs.add(bugElement);
					}
				}
			}
		}
		return left_bugs;
	}

	public static List<ResultInfo> getBugResult(String FilePath) {
		List<ResultInfo>  resultInfos = new ArrayList<>();
		List<Element> bugElement = getDefects4j_BugInstanceNodes(FilePath);
		for (Element e : bugElement) {
			NodeList childNodes = e.getChildNodes();
			ResultInfo resultInfo = new ResultInfo();
			boolean isFirstMethod = true;
			boolean isFirstSourceLine = true;
			resultInfo.setType(e.getAttribute("type"));
			for(int j = 0; j < childNodes.getLength(); j++){
				if (childNodes.item(j).getNodeName().equals("Class")) {
					String class_name = ((Element)childNodes.item(j)).getAttribute("classname");
					resultInfo.setClazz(class_name);
				}else if ("Method".equals(childNodes.item(j).getNodeName()) && isFirstMethod){
					isFirstMethod = false;
					resultInfo.setMethod(((Element)childNodes.item(j)).getAttribute("name"));
				}else if("SourceLine".equals(childNodes.item(j).getNodeName()) && isFirstSourceLine){
					isFirstSourceLine = false;
					if (((Element)childNodes.item(j)).getAttribute("start").equals("")) {
						resultInfo.setLocation(0);
						break;
					}
					resultInfo.setLocation(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("start")));
				}
			}
			resultInfos.add(resultInfo);
		}
		ExcelUtil.writeExcelWithTitle(new ArrayList<>(resultInfos) , "bugreport.xls");
		return resultInfos;
	}

	public static List<Bug_info> getDefects4j(String FilePath) {
		List<Bug_info> owaspList = new ArrayList<>();
		List<Element> bugElement = getDefects4j_BugInstanceNodes(FilePath);
		for (Element e : bugElement) {
			NodeList childNodes = e.getChildNodes();
			Bug_info bug_info = new Bug_info();
			boolean isFirstMethod = true;
			boolean isFirstSourceLine = true;
			for(int j = 0; j < childNodes.getLength(); j++){
				if (childNodes.item(j).getNodeName().equals("Class")) {
					String class_name = ((Element)childNodes.item(j)).getAttribute("classname");
					bug_info.setClazz(class_name);
				}else if ("Method".equals(childNodes.item(j).getNodeName()) && isFirstMethod){
					isFirstMethod = false;
					bug_info.setMethod(((Element)childNodes.item(j)).getAttribute("name"));
					bug_info.setSig(((Element)childNodes.item(j)).getAttribute("signature"));
				}else if("SourceLine".equals(childNodes.item(j).getNodeName()) && isFirstSourceLine){
					isFirstSourceLine = false;
					if (((Element)childNodes.item(j)).getAttribute("start").equals("")) {
						bug_info.setLocation(0);
						break;
					}
					bug_info.setLocation(Integer.parseInt(((Element)childNodes.item(j)).getAttribute("start")));
				}
			}
			owaspList.add(bug_info);
		}
		return owaspList;
	}

	public static void label_compare(List<Bug_info> test , List<Bug_info> standard , int index) {
		Set<Bug_info> total = new HashSet<>();
		for(Bug_info bug_info : test) {
			for (Bug_info test_bug : standard) {
				if (bug_info.getClazz().equals(test_bug.getClazz())) {
					bug_info.setIspositive(false);
					total.add(bug_info);
					continue;
				}
			}
			total.add(bug_info);
		}
	ExcelUtil.writeExcelWithTitle(new ArrayList<>(total) ,"compare" + index + ".xls");
	}

		/**
		 * @param args
		 */
		public static void main (String[]args){
			try {
//			System.out.println(handleinfo("D:\\Benchmark-master\\Benchmark-master\\results\\Benchmark_1.2-findbugs-v3.0.1-92.xml")
//					.size());
//			handle_source_label("D:\\Benchmark-master\\Benchmark-master\\src\\main\\java\\org\\owasp\\benchmark\\testcode\\" +
//					"BenchmarkTest00001.xml");

//				generate_excel("D:\\Benchmark-master\\Benchmark-master\\src\\main\\java\\org\\owasp\\benchmark\\testcode","D:\\Benchmark-master\\Benchmark-master\\results\\Benchmark_1.2-findsecbugs-v1.4.5-129.xml");
//			System.out.println(getOWASP("D:\\Benchmark-master\\Benchmark-master\\results\\Benchmark_1.2-findsecbugs-v1.4.6-122.xml").size() );
				// handleinfo("C:\\Users\\92039\\Desktop\\report.xml").get(0).toString();
//			MappingTable("C:\\Users\\admin\\Desktop\\bugreport.xml");
//				genenerate_labels("D:\\ExperimentData\\findsecCode" , "D:\\\\Benchmark-master\\\\Benchmark-master\\\\src\\\\main\\\\java\\\\org\\\\owasp\\\\benchmark\\\\testcode");
//				List<Bug_info> web1 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.6.0\\webmagic-WebMagic-0.6.0\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web2 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.7.0\\webmagic-WebMagic-0.7.0\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web3 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.7.2\\webmagic-WebMagic-0.7.2\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web4 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.7.3\\webmagic-WebMagic-0.7.3\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web5 = getDefects4j("D:\\project1\\webmagic-webmagic-parent-0.6.1\\webmagic-webmagic-parent-0.6.1\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web6 = getDefects4j("D:\\project1\\webmagic\\webmagic-core\\target\\result.xml");
//				label_compare(web1, web2 , 1);
//				label_compare(web2 , web3 , 2);
//				label_compare(web3 , web4 , 3);
//				label_compare(web4 , web5 , 4);
//				label_compare(web5 , web6 , 5);

//				List<Bug_info> web1 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.6.0\\webmagic-WebMagic-0.6.0\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web2 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.7.0\\webmagic-WebMagic-0.7.0\\webmagic-core\\target\\result.xml");
//				List<Bug_info> web3 = getDefects4j("D:\\project1\\webmagic-WebMagic-0.7.2\\webmagic-WebMagic-0.7.2\\webmagic-core\\target\\result.xml");
//				label_compare(web3 , web2 , 10);


//				List<Bug_info> webbit1 = getDefects4j("D:\\Downloads\\webbit-0.4.15\\webbit-0.4.15\\result1.xml");
//				List<Bug_info> webbit2 = getDefects4j("D:\\Downloads\\webbit-0.4.16\\webbit-0.4.16\\result1.xml");
//				List<Bug_info> webbit3 = getDefects4j("D:\\Downloads\\webbit-0.4.17\\webbit-0.4.17\\result1.xml");
//				List<Bug_info> webbit4 = getDefects4j("D:\\Downloads\\webbit-0.4.18\\webbit-0.4.18\\result1.xml");
//				List<Bug_info> webbit5 = getDefects4j("D:\\Downloads\\webbit-0.4.19\\webbit-0.4.19\\result1.xml");
//				label_compare(webbit2 , webbit1 , 1);
//				label_compare(webbit3 , webbit2 , 2);
//				label_compare(webbit4 , webbit3 , 3);
//				label_compare(webbit5 , webbit4 , 4);

//				getBugResult("D:\\blkkk\\result5.xml");

				getBugResult("D:\\\\Downloads\\\\nv-websocket-client-nv-websocket-client-2.3\\\\nv-websocket-client-nv-websocket-client-2.3\\\\target\\\\result.xml");
//				List<Bug_info> version1 = getDefects4j("D:\\blkkk\\result4.xml");
//				List<Bug_info> version2 = getDefects4j("D:\\blkkk\\result5.xml");
//
//				label_compare(version1 , version2 , 1);


//				List<Bug_info> pre = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.3\\nv-websocket-client-nv-websocket-client-2.3\\target\\result.xml");
//				List<Bug_info> current = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.4\\nv-websocket-client-nv-websocket-client-2.4\\target\\result.xml");
//
//				List<Bug_info> version1 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.3\\nv-websocket-client-nv-websocket-client-2.3\\target\\result.xml");
//				List<Bug_info> version2 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.4\\nv-websocket-client-nv-websocket-client-2.4\\target\\result.xml");
//				List<Bug_info> version3 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.5\\nv-websocket-client-nv-websocket-client-2.5\\target\\result.xml");
//				List<Bug_info> version4 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.6\\nv-websocket-client-nv-websocket-client-2.6\\target\\result.xml");
//				List<Bug_info> version5 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.7\\nv-websocket-client-nv-websocket-client-2.7\\target\\result.xml");
//				List<Bug_info> version6 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.8\\nv-websocket-client-nv-websocket-client-2.8\\target\\result.xml");
//				List<Bug_info> version7 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.10\\nv-websocket-client-nv-websocket-client-2.10\\target\\result.xml");
//				List<Bug_info> version8 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.11\\nv-websocket-client-nv-websocket-client-2.11\\target\\result.xml");
//				List<Bug_info> version9 = getDefects4j("D:\\Downloads\\nv-websocket-client-nv-websocket-client-2.12\\nv-websocket-client-nv-websocket-client-2.12\\target\\result.xml");
//				label_compare(version8 , version9, 1);


//				getBugResult("D:\\project1\\webmagic-WebMagic-0.7.2\\webmagic-WebMagic-0.7.2\\webmagic-core\\target\\result.xml");

//				List<Bug_info> gec1 = getDefects4j("D:\\Downloads\\spark-2.6.0\\spark-2.6.0\\target\\result.xml");
//				List<Bug_info> gec2 = getDefects4j("D:\\Downloads\\spark-2.7.0\\spark-2.7.0\\target\\result.xml");
//				List<Bug_info> gec3 = getDefects4j("D:\\Downloads\\spark-2.7.1\\spark-2.7.1\\target\\result.xml");
//				List<Bug_info> gec4 = getDefects4j("D:\\Downloads\\spark-2.7.2\\spark-2.7.2\\target\\result.xml");
//				List<Bug_info> gec5 = getDefects4j("D:\\Downloads\\spark-2.9.0\\spark-2.9.0\\target\\result.xml");
//				List<Bug_info> gec6 = getDefects4j("D:\\Downloads\\spark-2.9.2\\spark-2.9.2\\target\\result.xml");
//				List<Bug_info> gec7 = getDefects4j("D:\\Downloads\\spark-2.9.3\\spark-2.9.3\\target\\result.xml");
//				label_compare(gec1 , gec2 , 1);
//				label_compare(gec2 , gec3 , 2);
//				label_compare(gec3 , gec4 , 3);
//				label_compare(gec4 , gec5 , 4);
//				label_compare(gec5 , gec6 , 5);
//				label_compare(gec6 , gec7 , 6);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
