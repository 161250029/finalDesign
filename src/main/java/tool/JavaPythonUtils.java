package tool;

import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
public class JavaPythonUtils {
    public static void testgunapi() throws InterruptedException, IOException {
 
        Process pr = Runtime.getRuntime().exec("python D:\\PycharmProjects\\NET\\Net.py");
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        pr.waitFor();
    }

    public static void test() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("D:\\PycharmProjects\\NET\\Net.py");
        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction pyFunction = interpreter.get("do_telnet", PyFunction.class);
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__(
                new PyString("hadoop\\r\\n") , new PyString("gy18851835309\\r\\n"),
                new PyString("$"));
        System.out.println("the anwser is: " + pyobj);
    }

    public static void testadd() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("D:\\PycharmProjects\\NET\\Net.py");
        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction pyFunction = interpreter.get("add", PyFunction.class);
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__(
                new PyInteger(1) , new PyInteger(5));
        System.out.println("the anwser is: " + pyobj);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        test();
    }
 
}