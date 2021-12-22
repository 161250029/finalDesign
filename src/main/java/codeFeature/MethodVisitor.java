package codeFeature;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter<List<String>> {

    // 变量名替换
    private List<String> vals = new ArrayList<>();

    // 字符串字面量替换
    private List<String> strs = new ArrayList<>();

    private static final String positiveIdentifier = "P";

    private static final String negtiveIdentifier = "N";



    public void visit(AssertStmt stmt, List<String> result) {
        result.add("assert");
        // VoidVisitorAdapter遍历子节点，调用子节点的accpet函数回调MethodVisitor的visit方法
        super.visit(stmt, result);
    }

    public void visit(BreakStmt stmt, List<String> result) {
        result.add("break");
        super.visit(stmt, result);
    }

    public void visit(CatchClause stmt, List<String> result) {
        result.add("catch");
        super.visit(stmt, result);
    }

    public void visit(ContinueStmt stmt, List<String> result) {
        result.add("continue");
        super.visit(stmt, result);
    }

    public void visit(DoStmt stmt, List<String> result) {
        result.add("do");
        super.visit(stmt, result);
    }

    public void visit(ForEachStmt stmt, List<String> result) {
        result.add("forEach");
        super.visit(stmt, result);
    }

    public void visit(ForStmt stmt, List<String> result) {
        result.add("for");
        super.visit(stmt, result);
    }

    public void visit(IfStmt stmt, List<String> result) {
        result.add("if");
        super.visit(stmt, result);
    }

    public void visit(LabeledStmt stmt, List<String> result) {
        result.add("label");
        super.visit(stmt, result);
    }

    public void visit(ReturnStmt stmt, List<String> result) {
        result.add("return");
        super.visit(stmt, result);
    }

    public void visit(SwitchEntry stmt, List<String> result) {
        result.add("case");
        super.visit(stmt, result);
    }

    public void visit(SwitchStmt stmt, List<String> result) {
        result.add("switch");
        super.visit(stmt, result);
    }

    public void visit(TryStmt stmt, List<String> result) {
        result.add("try");
        super.visit(stmt, result);
    }

    public void visit(WhileStmt stmt, List<String> result) {
        result.add("while");
        super.visit(stmt, result);
    }

    public void visit(ArrayCreationExpr expr, List<String> result) {
        result.add("array");
        result.add(expr.getElementType().asString());
        super.visit(expr, result);
    }

    public void visit(AssignExpr expr, List<String> result) {
        result.add(expr.getOperator().asString());
        super.visit(expr, result);
    }

    public void visit(BinaryExpr expr, List<String> result) {
        result.add(expr.getOperator().asString());
        super.visit(expr, result);
    }

    public void visit(CastExpr expr, List<String> result) {
        result.add(expr.toString());
        super.visit(expr, result);
    }

    // char字面值
    public void visit(CharLiteralExpr expr, List<String> result) {
        result.add(Character.toString(expr.asChar()));
        super.visit(expr, result);
    }

    public void visit(ClassExpr expr, List<String> result) {
        result.add(expr.toString());
        super.visit(expr, result);
    }

    public void visit(FieldAccessExpr expr, List<String> result) {
        result.add(expr.getName().asString());
        super.visit(expr, result);
    }

    // 整型字面值
    public void visit(IntegerLiteralExpr expr, List<String> result) {
        int intValue = expr.asInt();
        boolean flag = intValue > 0 ? true: false;
        int absValue = Math.abs(intValue);

        if (absValue >= 0 && absValue <= 9) {
            result.add(Integer.toString(intValue));
        }else if (absValue > 9 && absValue <= 99) {
            if (flag) {
                result.add(positiveIdentifier + 2);
                expr.setInt(10);
            }
            else {
                result.add(negtiveIdentifier + 2);
                expr.setInt(-10);
            }
        }else if (absValue > 99 && absValue <= 999) {
            if (flag) {
                result.add(positiveIdentifier + 3);
                expr.setInt(100);
            }
            else {
                result.add(negtiveIdentifier + 3);
                expr.setInt(-100);
            }
        }else if (absValue > 999) {
            if (flag) {
                result.add(positiveIdentifier + "4+");
                expr.setInt(1000);
            }
            else {
                result.add(negtiveIdentifier + "4+");
                expr.setInt(-1000);
            }
        }
        super.visit(expr, result);
    }

    // 调用函数节点
    public void visit(MethodCallExpr expr, List<String> result) {
        result.add(expr.getName().asString());
        super.visit(expr, result);
    }

    // 使用变量名
    public void visit(NameExpr expr, List<String> result) {
        String valName = expr.getName().asString();
        if (vals.contains(valName)) {
            result.add("val" + (vals.indexOf(valName) + 1));
            expr.setName("val" + (vals.indexOf(valName) + 1));
        }
        else {
            vals.add(valName);
            result.add("val" + vals.size());
            expr.setName("val" + (vals.indexOf(valName) + 1));
        }
        super.visit(expr, result);
    }

    public void visit(ObjectCreationExpr expr, List<String> result) {
        result.add(expr.getType().asString());
        super.visit(expr, result);
    }

    // 字符串字面值
    public void visit(StringLiteralExpr expr, List<String> result) {
        String strValue = expr.getValue();
        if (strs.contains(strValue)) {
            result.add("str" + (strs.indexOf(strValue) + 1));
            expr.setEscapedValue("str" + (strs.indexOf(strValue) + 1));
        }
        else {
            strs.add(strValue);
            result.add("str" + strs.size());
            expr.setEscapedValue("str" + strs.size());
        }
        super.visit(expr, result);
    }

    public void visit(SuperExpr expr, List<String> result) {
        result.add("super");
        super.visit(expr, result);
    }

    public void visit(ThisExpr expr, List<String> result) {
        result.add("this");
        super.visit(expr, result);
    }

    public void visit(UnaryExpr expr, List<String> result) {
        result.add(expr.getOperator().asString());
        super.visit(expr, result);
    }

    public void visit(VariableDeclarator declarator, List<String> result) {
        result.add(declarator.getType().asString());
        String valName = declarator.getName().asString();
        if (vals.contains(valName)) {
            result.add("val" + (vals.indexOf(valName) + 1));
            declarator.setName("val" + (vals.indexOf(valName) + 1));
        }
        else {
            vals.add(valName);
            result.add("val" + vals.size());
            declarator.setName("val" + vals.size());
        }
        super.visit(declarator, result);
    }

    // 修饰符
    public void visit(Modifier modifier, List<String> result) {
        result.add(modifier.getKeyword().asString());
    }


    public void visit(Parameter parameter , List<String> result) {
        String valName = parameter.getName().asString();
        if (vals.contains(valName)) {
            result.add("val" + (vals.indexOf(valName) + 1));
            parameter.setName("val" + (vals.indexOf(valName) + 1));
        }
        else {
            vals.add(valName);
            result.add("val" + vals.size());
            parameter.setName("val" + vals.size());
        }
        super.visit(parameter, result);
    }


}