package codeFeature;

import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CycleComplexityVisitor extends VoidVisitorAdapter<Integer> {

    public void visit(CatchClause stmt, int res) {
        res ++;
        super.visit(stmt, res);
    }


    public void visit(DoStmt stmt, int res) {
        res ++;
        super.visit(stmt, res);
    }

    public void visit(ForEachStmt stmt, int res) {
        res ++;
        super.visit(stmt, res);
    }

    public void visit(ForStmt stmt, int res) {
        res ++;
        super.visit(stmt, res);
    }

    public void visit(IfStmt stmt, int res) {
        res ++;
        super.visit(stmt, res);
    }
}
