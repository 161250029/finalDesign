package codeFeature;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import prepareData.Config;
import tool.FileTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// token序列主要是为了做三星对比实验用的
public class estractASTTokenFeature {

    // 合法文件名（非合法：java文件中只包含构造函数或切片得到的结果无法编译，具体表现为无法生成有效方法体的java文件）
    private static List<String> validFileNames = new ArrayList<>();

    public static List<String> extract(MethodDeclaration methodDeclaration) {
        MethodVisitor methodVisitor = new MethodVisitor();
        List<String> tokens = new ArrayList<>();
        methodVisitor.visit(methodDeclaration , tokens);
        return tokens;
    }

    public static void extract() {
        List<String> totalFileNames = FileTool.findFileNames(Config.sliceDirPath , ".java");
        totalFileNames.forEach(fileName -> {
            if (validFileNames.contains(fileName)) {
                CompilationUnit cu = null;
                try {
                    cu = StaticJavaParser.parse(new File(Config.sliceDirPath + fileName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
                List<String> tokens = new ArrayList<>();

                for (MethodDeclaration m : methodList) {
                    if (m.getBody().isPresent()) {
                        tokens = extract(m);
                        break;
                    }
                }
                StringBuilder tokenList = new StringBuilder();
                for (String token : tokens) {
                    tokenList.append(token + ",");
                }
                try {
                    FileTool.write_content(Config.astFeatureDirPath + fileName ,
                            new String(tokenList));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private static void initValidFileNames() {
        validFileNames = FileTool.findFileNames(Config.sliceFuncDirPath , ".java");
    }

    public static void run() {
        initValidFileNames();
        extract();
    }

    public static void main(String[] args) throws FileNotFoundException {
//        run();

        CompilationUnit cu = StaticJavaParser.parse(new File(Config.sliceDirPath + "commons-io-b5990be69139f0b04919bc097144a105051aafcc#FileUtils#RV_RETURN_VALUE_IGNORED_BAD_PRACTICE#BAD_PRACTICE#2#1156#1156#false.java"));
        List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
        extract(methodList.get(0));
        System.out.println(cu);
//        System.out.println(methodList.get(0));
//        System.out.println(extract(methodList.get(0)));
    }

}
