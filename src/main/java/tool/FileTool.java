package tool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTool {

    /**
     *
     * @param dirPath 目录
     * @param ext 文件后缀
     * @return 符合后缀的文件路径列表
     */
    public static List<String> findPath(String dirPath , String ext) {
        List<String> paths = new ArrayList<>();
        File file = new File(dirPath);
        File[] file_path = file.listFiles();
        for (File f : file_path) {
            if (f.getName().endsWith(ext)) {
                paths.add(f.getAbsolutePath());
            }
            if (f.isDirectory()) {
                paths.addAll(findPath(f.getAbsolutePath() , ext));
            }
        }
        return paths;
    }

    /**
     *
     * @param dirPath 目录路径
     * @param fileName 文件名（包含后缀）
     * @return 文件的绝对路径
     */
    public static String findFile(String dirPath , String fileName) {
        File file = new File(dirPath);
        File[] childs = file.listFiles();
        String absoluteFilePath = null;
        for (File child : childs) {
            if (!child.isDirectory()) {
                if (child.getName().equals(fileName)) {
                    absoluteFilePath = child.getAbsolutePath();
                    break;
                }
            }
            else {
                absoluteFilePath = findFile(child.getAbsolutePath() , fileName);
                if (absoluteFilePath != null) {
                    break;
                }
            }
        }
        return absoluteFilePath;
    }

    /**
     *
     * @param dirPath
     * @return 直接子目录列表
     */
    public static List<String> getDirName(String dirPath) {
        File file = new File(dirPath);
        File[] childs = file.listFiles();
        List<String> dirName = new ArrayList<>();
        for (File child : childs) {
            if (child.isDirectory()){
                dirName.add(child.getName());
            }
        }
        return dirName;
    }

    /**
     *
     * @param file_path
     * @param start
     * @param end
     * @return 文件中指定的n行文件
     */
    public static List<String> readRangeLine(String file_path , int start , int end) {
        List<String> contents = read(file_path);
        if (start <= 0 || end > contents.size() || start > end) {
            return null;
        }
        return new ArrayList<>(contents.subList(start - 1 , end));
    }

    public static List<String> read(String file_path) {
        List<String> contents = new ArrayList<>();
        File file = new File(file_path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                contents.add(line.trim());
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static void write_content(String file_path , String content) throws IOException {
        File file = new File(file_path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.close();
    }

    public static List<String> get_content(List<Integer> lines , String file_path) throws IOException {
        File file = new File(file_path);
        BufferedReader bw = new BufferedReader(new FileReader(file));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();
        String str = "";
        while((str = bw.readLine()) != null) {
            contents.add(str);
        }
        for (Integer i : lines) {
            if (i > 0 && i <= contents.size())
            result.add(contents.get(i - 1));
        }
        return result;
    }

    public static void write_some_contents(List<String> content , String file_path) throws IOException {
        File file = new File(file_path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String str : content) {
            bw.write(str);
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    public static void write_some_lines(List<Integer> lines , String source_path ,String file_path) throws IOException {
        List<String> content = get_content(lines , source_path);
        write_some_contents(content , file_path);
    }


    public static void write_labels(List<String> content , String file_path) throws IOException {
        File file = new File(file_path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String str : content) {
            bw.write(str + " ");
            bw.flush();
        }
        bw.close();
    }

    public static void main(String args[]) {
        System.out.println(findPath("D:\\Benchmark-master\\Benchmark-master\\src\\main\\java\\org\\owasp\\benchmark\\testcode" , "xml").size());
        System.out.println(getDirName("D:\\DataSet\\commons-bcel"));
        System.out.println(findFile("D:\\DataSet\\commons-bcel\\commons-bcel-31dcc10240df3b9d0c2abce5610aaaeb04d0b864" ,
                "Const.java"));
    }
}
