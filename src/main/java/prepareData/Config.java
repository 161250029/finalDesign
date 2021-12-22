package prepareData;

public class Config {
    // D:\DataSet\
    public static final String dirPath = "D:\\AddProject\\";

    // 切片存放目录
    public static final String sliceDirPath = "D:\\SliceData\\";

    // 函数体存放目录
    public static final String sliceFuncDirPath = "D:\\SliceFuncData\\";

    // ASTFeature 存放目录
    public static final String astFeatureDirPath = "D:\\ASTFeature\\";

    // 细节度量报告原始文件路径
    public static final String metricsDetailXmlFilePath = Config.sliceDirPath + "metrics.xml";

    // 总体度量报告原始文件路径
    public static final String metricsGeneralXmlFilePath = Config.sliceFuncDirPath + "metrics.xml";

    // 合并后的度量特征保存路径
    public static final String metricsXslFilePath = Config.sliceFuncDirPath + "metrics.xls";
    // 扫描报告文件名
    public static final String reportName = "report.xml";

    // 标记文件名
    public static final String labelName = "label.xls";

}
