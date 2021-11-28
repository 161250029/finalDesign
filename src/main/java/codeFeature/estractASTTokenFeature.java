package codeFeature;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class estractASTTokenFeature {

    public static List<String> extract(MethodDeclaration methodDeclaration) {
        MethodVisitor methodVisitor = new MethodVisitor();
        List<String> tokens = new ArrayList<>();
        methodVisitor.visit(methodDeclaration , tokens);
        return tokens;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File("D:\\SliceNewData\\commons-io-b5990be69139f0b04919bc097144a105051aafcc#FileUtils#RV_RETURN_VALUE_IGNORED_BAD_PRACTICE#BAD_PRACTICE#2#1156#1156#false.java"));
        List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
        System.out.println(extract(methodList.get(0)));
    }

}
