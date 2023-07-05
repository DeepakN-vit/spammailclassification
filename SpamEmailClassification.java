import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpamEmailClassification {

    private Map<String, Integer> spamWordCounts;
    private Map<String, Integer> hamWordCounts;
    private int spamEmailCount;
    private int hamEmailCount;

    public SpamEmailClassification() {
        spamWordCounts = new HashMap<>();
        hamWordCounts = new HashMap<>();
        spamEmailCount = 0;
        hamEmailCount = 0;
    }

    public void train(String filePath, String label) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split(" ");
            for (String word : words) {
                if (label.equals("spam")) {
                    spamWordCounts.put(word, spamWordCounts.getOrDefault(word, 0) + 1);
                } else {
                    hamWordCounts.put(word, hamWordCounts.getOrDefault(word, 0) + 1);
                }
            }
        }
        br.close();

        if (label.equals("spam")) {
            spamEmailCount++;
        } else {
            hamEmailCount++;
        }
    }

    public double calculateSpamProbability(String[] words) {
        double spamProbability = (double) spamEmailCount / (spamEmailCount + hamEmailCount);
        for (String word : words) {
            int wordCount = spamWordCounts.getOrDefault(word, 0);
            spamProbability *= (double) (wordCount + 1) / (spamWordCounts.size() + 1);
        }
        return spamProbability;
    }

    public String classifyEmail(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line).append(" ");
        }
        br.close();

        String[] words = sb.toString().split(" ");
        double spamProbability = calculateSpamProbability(words);

        if (spamProbability >= 0.5) {
            return "spam";
        } else {
            return "ham";
        }
    }

    public static void main(String[] args) throws IOException {
        SpamEmailClassification classifier = new SpamEmailClassification();

        // Training
        classifier.train("path/to/spam_emails.txt", "spam");
        classifier.train("path/to/ham_emails.txt", "ham");

        // Classification
        String emailFilePath = "path/to/email.txt";
        String emailClass = classifier.classifyEmail(emailFilePath);
        System.out.println("Email Classification: " + emailClass);
    }
}
