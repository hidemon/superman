package semantics.train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.TokenizedLM;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
/**
 * The classier used for training data and classifying the input
 * @author yusun
 *
 */
public class TokenizedLMClassifier {
	DynamicLMClassifier<TokenizedLM> classifier;
	
	/**
	 * train the new data
	 * @param dataPath The training data file
	 * @throws IOException
	 */
	public void train(String dataPath) throws IOException {
		List<String[]> annotatedData = Util.readAnnotatedCsvRemoveHeader(new File(dataPath));
		String[] categories = Util.getCategories(annotatedData);
		int maxTokenNGram = 2;
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
		classifier 
			= DynamicLMClassifier.createTokenized(categories,tokenizerFactory,maxTokenNGram);

		for (String[] row: annotatedData) {
			String truth = row[Util.ANNOTATION_OFFSET];
			String text = row[Util.TEXT_OFFSET];
			Classification classification = new Classification(truth);
			Classified<CharSequence> classified = new Classified<CharSequence>(text,classification);
			classifier.handle(classified);
		}
	}
	
	public String classify(String data) {
			if (data.equals("")) return "";
			Classification classification = classifier.classify(data);
			// If the classification result score is too low, return an empty string.
			if (Double.parseDouble(classification.toString().split("\n")[1].split(" ")[2]) < -10) {
				return "";
			};
			return classification.bestCategory();
	}
	
	public static void main(String[] args) throws IOException {
		String dataPath = args.length > 0 ? args[0] : "data/command_train.csv";
		TokenizedLMClassifier classifier = new TokenizedLMClassifier();
		classifier.train(dataPath);
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String line = scanner.nextLine();
			System.out.println(classifier.classify(line));
		}
	}
}



