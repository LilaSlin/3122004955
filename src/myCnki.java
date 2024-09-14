
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class myCnki {
    // 读取文件内容
    public static String readFile(String fp) throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(fp)));
        } catch (IOException e) {
            System.err.println("找不到对应的文件："+ e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        String originPath = "D:\\temp\\orig.txt";
        String comparedPath = "D:\\temp\\orig_0.8_del.txt";

        try {
            // 读取原文文本文件和对比版论文文本文件
            String originalText = readFile(originPath);
            String comparedText = readFile(comparedPath);


        } catch (IOException e) {
            System.err.println("文件未找到: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
