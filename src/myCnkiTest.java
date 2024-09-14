import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class myCnkiTest {

    @Test
    void readFile() throws IOException {
        String fp = "D:\\temp\\test.txt";
        String content = "这是一个测试文件";
        Files.write(Paths.get(fp), content.getBytes());
        String result = myCnki.readFile(fp);
        assertEquals(content, result);
    }

    @Test
    void writeFile() throws IOException {
        String fp = "D:\\temp\\test_output.txt";
        String content = "测试以下输出内容";
        myCnki.writeFile(fp, content);
        String result = new String(Files.readAllBytes(Paths.get(fp)));
        assertEquals(content, result);
    }

    @Test
    void getWordFrequency() {
        String text = "不断测试不断测试";
        Map<String, Integer> frequencyMap = myCnki.getWordFrequency(text);
        assertEquals(2, frequencyMap.size());
        assertEquals(2, frequencyMap.get("测试").intValue());
    }

    @Test
    void cosineSimilarity() {
        Map<String, Integer> frequencyMap1 = Map.of("测试", 3, "一个", 1);
        Map<String, Integer> frequencyMap2 = Map.of("测试", 2, "另一个", 1);
        double similarity = myCnki.cosineSimilarity(frequencyMap1, frequencyMap2);
        assertTrue(similarity > 0);
    }

    @Test
    void calculateCosineSimilarity() {
        String text1 = "这是一个测试。";
        String text2 = "这是另一个测试。";
        double similarity = myCnki.calculateCosineSimilarity(text1, text2);
        assertTrue(similarity > 0);
    }

    @Test
    void main() throws IOException {
        String[] arg = {"D:\\temp\\orig.txt", "D:\\temp\\orig_0.8_add.txt", "D:\\temp\\result.txt"};
        myCnki.main(arg);

        // 检查输出文件是否生成并包含预期内容
        String result = new String(Files.readAllBytes(Paths.get("D:\\temp\\result.txt")));
        assertTrue(result.contains("%"));
    }
}