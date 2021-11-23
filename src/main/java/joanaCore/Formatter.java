package joanaCore;

import edu.kit.joana.ifc.sdg.graph.SDGNode;
import joanaCore.datastructure.Func;

import java.util.*;

public class Formatter {
    public static String prepareSliceForEncoding(Collection<SDGNode> slice) {
        ArrayList<SDGNode> nodes=new ArrayList<>(slice);
        nodes.sort(Comparator.comparingInt(sdgNode -> sdgNode.getId()));
        StringBuilder result = new StringBuilder();
        for (SDGNode node : slice) {
            result.append(node).append(" :: ").append(node.getKind()).append(" :: ").append(node.getOperation()).append(" :: ").append(node.getType()).append(" :: ").append(node.getLabel()).append("\n");
        }
        return result.toString();
    }

    public void dfs(Collection<SDGNode> slice, Func func){

    }

}
