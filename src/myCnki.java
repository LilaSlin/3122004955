
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // 写入结果到文件
    public static void writeFile(String fp, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fp))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("找不到对应的文件："+ e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 文本预处理和分词，针对中文采用简化分词
    public static Map<String, Integer> getWordFrequency(String text) {
        // 创建一个 Map 对象保存词频
        Map<String, Integer> frequencyMap = new HashMap<>();

        // 创建 JiebaSegmenter 对象进行分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> tokens = segmenter.process(text, JiebaSegmenter.SegMode.INDEX);

        // 遍历分词结果，统计每个词的频率
        for (SegToken token : tokens) {
            String word = token.word;
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }
        return frequencyMap;
    }
    // 计算两个词频向量的余弦相似度
    public static double cosineSimilarity(Map<String, Integer> originalMap, Map<String, Integer> comparedMap) {

        // 计算点积
        double dotProduct = 0.0;
        for (String word : originalMap.keySet()) {
            if (comparedMap.containsKey(word)) {
                dotProduct += originalMap.get(word) * comparedMap.get(word);
            }
        }

        // 计算两个向量的模
        double norm1 = 0.0;
        for (int freq : originalMap.values()) {
            norm1 += freq * freq;
        }
        norm1 = (float) Math.sqrt(norm1);

        double norm2 = 0.0;
        for (int freq : comparedMap.values()) {
            norm2 += freq * freq;
        }
        norm2 = (float) Math.sqrt(norm2);

        // 如果模为0，则余弦相似度为0
        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        // 计算余弦相似度
        return dotProduct / (norm1 * norm2);
    }

    // 计算余弦相似度
    public static double calculateCosineSimilarity(String text1, String text2) {
        // 对文本进行分词处理，获取词频
        Map<String, Integer> originalFrequencyMap = getWordFrequency(text1);
        Map<String, Integer> comparedFrequencyMap = getWordFrequency(text2);

        // 计算词频向量的余弦相似度
        return cosineSimilarity(originalFrequencyMap, comparedFrequencyMap);
    }

    public static void main(String[] args) {
        String originPath = "D:\\temp\\orig.txt";
        String comparedPath = "D:\\temp\\orig_0.8_del.txt";

        try {
            // 读取原文文本文件和对比版论文文本文件
            String originalText = readFile(originPath);
            String comparedText = readFile(comparedPath);
            String outputPath = "D:\\temp\\result.txt";

            // 计算相似度
            double similarity = calculateCosineSimilarity(originalText, comparedText);

            // 如果相似度为0，则说明文本可能存在问题，输出相似度为0
            if (similarity == 0.0) {
                System.out.println("文本可能存在问题，相似度为0");
            }

            // 将相似度转换为百分比并写入答案文件
            writeFile(outputPath, String.format("%.2f%%", similarity * 100));
            System.out.println("相似度为: " + String.format("%.2f%%", similarity * 100));

        } catch (IOException e) {
            System.err.println("文件未找到: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
