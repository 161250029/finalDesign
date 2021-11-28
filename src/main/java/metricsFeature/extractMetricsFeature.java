package metricsFeature;

import jnr.ffi.annotations.In;
import metricsFeature.model.MetricsFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import prepareData.Config;
import tool.DomTool;
import tool.ExcelUtil;
import tool.FileTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class extractMetricsFeature {

    // 合法文件名（非合法：java文件中只包含构造函数或切片得到的结果无法编译，具体表现为无法生成有效方法体的java文件）
    private static List<String> validFileNames = new ArrayList<>();

    private static Map<String , MetricsFeature> map = new HashMap<>();

    public static Map<String , MetricsFeature> extract(String metricsFilePath) {
        initValidFileNames();
        Document document = DomTool.getDocument(metricsFilePath);
        NodeList files = document.getElementsByTagName("file");
        for (int i = 0 ; i < files.getLength() ; i ++) {
            Element cur = (Element) files.item(i);
            String fileName = cur.getAttribute("file_name");
            if (!validFileNames.contains(fileName)) {
                continue;
            }
            NodeList methods = cur.getElementsByTagName("method");
            MetricsFeature metricsFeature = new MetricsFeature();
            for (int j = 0 ; j < methods.getLength() ; j ++) {
                Element method = (Element) methods.item(j);
                String name = method.getAttribute("name");
                String clazz = name.split("\\.")[0];
                String methodName = name.split("\\.")[1];
                if (methodName.startsWith(clazz)) {
                    continue;
                }
                metricsFeature.setCycleComplexity(Integer.parseInt(method.getElementsByTagName("complexity").item(0).getTextContent()));
                metricsFeature.setCallNum(Integer.parseInt(method.getElementsByTagName("calls").item(0).getTextContent()));
                metricsFeature.setDepth(Integer.parseInt(method.getElementsByTagName("maximum_depth").item(0).getTextContent()));
                metricsFeature.setStatementNum(Integer.parseInt(method.getElementsByTagName("statements").item(0).getTextContent()));
            }
            map.put(fileName , metricsFeature);
        }
        System.out.println(map.keySet().size());
        List<MetricsFeature> list = new ArrayList<MetricsFeature>(map.values());
        ExcelUtil.writeExcelWithTitle(list , "test.xls");
        return map;
    }

    private static void initValidFileNames() {
        validFileNames = FileTool.findFileNames(Config.sliceFuncDirPath , ".java");
    }

    public static void main(String[] args) {
        extract(Config.metricsSourceFilePath);
    }
}
