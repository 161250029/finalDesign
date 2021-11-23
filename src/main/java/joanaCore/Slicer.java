package joanaCore;


import com.ibm.wala.ipa.cha.ClassHierarchyException;
import joanaCore.datastructure.Func;
import joanaCore.datastructure.Location;
import joanaCore.exception.SlicerException;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface Slicer {
    void config(List<File> appJars, List<URL> libJars, List<String> exclusions) throws ClassHierarchyException, IOException;
    List<Integer> computeSlice(Func func, Location line) throws SlicerException;
}
