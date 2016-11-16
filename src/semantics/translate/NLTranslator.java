package semantics.translate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.ExactDictionaryChunker;

import semantics.autocomplete.AutoComplete;
import semantics.chunker.SemanticsMap;
import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;
import semantics.train.TokenizedLMClassifier;

/**
 * The core class which is responsible for translating natural language to
 * structural instructions.
 * 
 * @author Yu
 */
public class NLTranslator {
	private SemanticsMap semanticsMap;
	private AutoComplete autoCompleter;
	private TokenizedLMClassifier classifier;

	public NLTranslator(String... args) {
		semanticsMap = new SemanticsMap(args);
		try {
			autoCompleter = new AutoComplete("command.csv");
			classifier = new TokenizedLMClassifier();
			classifier.train("data/command_train.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String translate(String input) throws NoTokenFoundException, InvalidParameterException {
		ExactDictionaryChunker dictionaryChunker = semanticsMap.getChunker();
		Chunking chunking = dictionaryChunker.chunk(input);
		// Get the sequential command tokens.
		List<String> tokens = new ArrayList<>();
		for (Chunk chunk : chunking.chunkSet()) {
			tokens.add(chunk.type());
		}
		String commandCore = "";
		if (tokens.size() == 0) {
			commandCore = classifier.classify(input);
			if (commandCore.isEmpty()) {
				return autoCompleter.autoComplete(input);
			}
		} else {
			commandCore = tokens.get(0);	
		}
		switch (commandCore) {
		case "diff":
			return GitTranslator.translateDiff(input);
		case "clone":
			return GitTranslator.translateClone(input);
		case "pull":
			return GitTranslator.translatePull(input);
		case "fetch":
			return GitTranslator.translateFetch(input);
		case "rm":
			return GitTranslator.translateRm(input);
		case "mv":
			return GitTranslator.translateMv(input);
		case "commit":
			return GitTranslator.translateCommit(input);
		case "merge":
			return GitTranslator.translateMerge(input);
		case "reset":
			return GitTranslator.translateReset(input);
		case "compile":
			return CompilationTranslator.translateCompile(input);
		case "run":
			return CompilationTranslator.translateRun(input);
		// test classifier
		case "test":
			return "Classify Test!";
		default:
			return "Cannot recognize the command!";
		}

	}

	public static void main(String[] args) {

		NLTranslator translator = new NLTranslator("git", "compile");
		String command = "superman find difference between 52345f and d89a32";
		try {
			System.out.println(translator.translate(command));

			command = "nlp clone the repo git://www.github.haha.git";
			System.out.println(translator.translate(command));

			command = "nlp run helloworld.java";
			System.out.println(translator.translate(command));

			command = "nlp run helloworld.c";
			System.out.println(translator.translate(command));
			
			command = "nlp runnn helloworld.c";
			System.out.println(translator.translate(command));
			
			command = "nlp please delete helloworld.c";
			System.out.println(translator.translate(command));
			
			command = "nlp please move helloworld.c D:/good.c";
			System.out.println(translator.translate(command));
			
			command = "nlp make commission";
			System.out.println(translator.translate(command));
			
			command = "nlp please merge #abc and #xyz using stratege ours";
			System.out.println(translator.translate(command));
			
			command = "nlp please reset 532eac";
			System.out.println(translator.translate(command));
			
			command = "nlp fetch #origin branch";
			System.out.println(translator.translate(command));
			
			command = "nlp test is going on?";
			System.out.println(translator.translate(command));
			
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}
	}
}
