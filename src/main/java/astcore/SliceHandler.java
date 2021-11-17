package astcore;

import astcore.datastructure.MethodRange;
import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import jnr.ffi.annotations.In;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SliceHandler {
    /**
     * 根据给出的行号对目录下的Java文件进行切片操作
     * 目前暂未考虑子目录的情况
     * 输出格式：文件名#切片行.csv
     * @param inputDir 输入文件目录
     * @param outputDir 输出文件目录
     * @param lineFile 保存切片行号的csv文件地址
     */
    public static void sliceByLines(String inputDir, String outputDir, String lineFile) {
        // 读取保存切片行号的csv文件
        try (BufferedReader reader = new BufferedReader(new FileReader(lineFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split(",");
                // 每行第一个数据为文件名
                Path javaFilePath = Paths.get(inputDir, lineData[0] + ".java");
                // 后续数据为切片行号
                List<Integer> sliceLines = new ArrayList<>();
                for (int i = 1; i < lineData.length; i++) {
                    sliceLines.add(Integer.parseInt(lineData[i]));
                }
                Path sliceFilePath = Paths.get(outputDir, lineData[0] + "#" + lineData[lineData.length - 1] + ".java");

                if (!Files.exists(sliceFilePath)) {
                    Files.createFile(sliceFilePath);
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(sliceFilePath.toFile()))) {
                    writer.write(sliceFile(javaFilePath.toFile(), sliceLines));
                    System.out.println(sliceFilePath.toString());
                } catch (IOException e) {
                    System.out.println(e.toString() + " " + lineFile);
                }

            }
        } catch (IOException e) {
            System.out.println(e.toString() + " " + lineFile);
        }


    }

    /**
     * 对指定的方法体进行切片操作，保留与切片行号有关的代码
     * @param file Java文件对象
     * @param methodName 需要切片的方法名
     * @param lines 切片行号列表
     * @return 处理完成的Java文件内容
     */
    public static String sliceFile(File file, String methodName, List<Integer> lines) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);

            for (MethodDeclaration m : methodList) {
                if (methodName.equals(m.getNameAsString())) {
                    // 方法体存在
                    if (m.getBody().isPresent()) {
                        sliceMethod(m, lines);
                    }
                } else {
                    m.remove();
                }
            }
            cu.removePackageDeclaration();
            removeComments(cu);
            return cu.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString() + " " + file.getAbsolutePath());
        }
        System.out.println(String.format("%s method not in %s", methodName, file.getAbsolutePath()));
        return "";
    }

    public static String sliceFile(File file, List<Integer> lines) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
            for (MethodDeclaration m : methodList) {
                m.getRange().ifPresent(r -> {
                    if (r.contains(new Position(lines.get(0), 100))) {
                        sliceMethod(m, lines);
                    } else {
                        m.remove();
                    }
                });
            }
            return cu.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString() + " " + file.getAbsolutePath());
        }
        return "";
    }

    public static MethodRange getMethodRange(File file , String methodName) {
        // node中不提供remove方法的节点，一般都无法删除。直接设为null会报错
        MethodRange methodRange = new MethodRange();
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);

            int start = Integer.MAX_VALUE;
            int end  = Integer.MIN_VALUE;
            for (MethodDeclaration m : methodList) {
                if (methodName.equals(m.getNameAsString()) && m.getBody().isPresent()) {
//                    NodeList<Statement> statements = m.getBody().get().getStatements();
//                    for (Statement statement : statements) {
//                        start = Math.min(start , statement.getBegin().get().line);
//                        end = Math.max(end , statement.getEnd().get().line);
//                    }
                    System.out.println(m.toString());
                    start = m.getBegin().get().line;
                    end = m.getEnd().get().line;
                }
            }
            methodRange.setStart(start);
            methodRange.setEnd(end);
            return methodRange;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString() + " " + file.getAbsolutePath());
        }
        System.out.println(String.format("%s method not in %s", methodName, file.getAbsolutePath()));
        return methodRange;
    }

    public static MethodRange getMethodRange(File file) {
        // node中不提供remove方法的节点，一般都无法删除。直接设为null会报错
        MethodRange methodRange = new MethodRange();
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);

            int start = Integer.MAX_VALUE;
            int end  = Integer.MIN_VALUE;
            for (MethodDeclaration m : methodList) {
                if (m.getBody().isPresent()) {
//                    NodeList<Statement> statements = m.getBody().get().getStatements();
//                    for (Statement statement : statements) {
//                        start = Math.min(start , statement.getBegin().get().line);
//                        end = Math.max(end , statement.getEnd().get().line);
//                    }
                    System.out.println(m.toString());
                    start = m.getBegin().get().line;
                    end = m.getEnd().get().line;
                }
            }
            methodRange.setStart(start);
            methodRange.setEnd(end);
            return methodRange;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString() + " " + file.getAbsolutePath());
        }
        return methodRange;
    }

    public static String getFuncBody(File file) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
            for (MethodDeclaration m : methodList) {
                if (m.getBody().isPresent()) {
                    return m.toString();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString() + " " + file.getAbsolutePath());
        }
        return null;
    }

    private static void removeComments(CompilationUnit cu) {
        // 获得语法树中所有的注释节点
        List<Comment> comments = cu.getAllComments();
        comments.forEach(Node::remove);
    }


    private static void sliceMethod(MethodDeclaration method, List<Integer> lines) {
//        walk函数对AST上的节点做层次遍历。
        method.getBody().get().walk(node -> {
            if (node.getRange().isPresent()) {
//                System.out.println(node.getClass() + "_" + node.toString() + "_" + node.getBegin().get().line + "_" + node.getEnd().get().line);
                if (node.getParentNode().isPresent() && node instanceof CatchClause) {
                    return;
                }
                int start = node.getRange().get().begin.line;
                int end = node.getRange().get().end.line;
                boolean flag = false;
                for (int line : lines) {
                    if (line >= start && line <= end) {
                        flag = true;
                        break;
                    }
                }
                /**
                 * 如果当前节点的父节点是SwitchEntry类型,并且是父节点的第一个子节点，则不删除。
                 * 父节点:case Const.MULTIANEWARRAY:
                 * 当前节点:Const.MULTIANEWARRAY
                 *
                 * 假设lines的范围只包含case语句下面的一条语句，由于当前节点的范围不在lines的范围中，就会误删该节点
                 * 最终父节点下的第一个子节点为空，结果就会显示为null
                 */
                if (node.getParentNode().isPresent() && node.getParentNode().get() instanceof SwitchEntry
                && node == node.getParentNode().get().getChildNodes().get(0)) {
                    flag = true;
                }

                /**
                 * 如果当前节点是catch语句中的非blockstmt节点，例如参数类型节点等，需要和catchclause节点一起保留
                 * 递归查找父节点，以防误删。
                 */
                Node temp = node;
                while(temp.getParentNode().isPresent()) {
                    temp = temp.getParentNode().get();
                    if (temp instanceof Parameter && temp.getParentNode().isPresent()
                    && temp.getParentNode().get() instanceof CatchClause) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    node.remove();
                }
            }
        });
    }

    public static void main(String[] args) {
        getMethodRange(new File("D:\\SliceData\\commons-bcel-af0e5ef6784e2bc028b17a1f7dbc2cc5dd5c348f#BCELFactory#SF_SWITCH_FALLTHROUGH#STYLE#2#188#192#false.java") ,
                "visitAllocationInstruction");
    }
}