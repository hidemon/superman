package semantics.translate;

import java.util.ArrayList;
import java.util.List;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.ExactDictionaryChunker;

import semantics.autocomplete.AutoComplete;
import semantics.chunker.SemanticsMap;
import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;

/**
 * The core class which is responsible for translating natural language to
 * structural instructions.
 * 
 * @author Yu
 */
public class NLTranslator {
	private SemanticsMap semanticsMap;
	private AutoComplete autoCompleter;

	public NLTranslator(String... args) {
		semanticsMap = new SemanticsMap(args);
		autoCompleter = new AutoComplete("command.csv");
	}

	public String translate(String input) throws NoTokenFoundException, InvalidParameterException {
		ExactDictionaryChunker dictionaryChunker = semanticsMap.getChunker();
		Chunking chunking = dictionaryChunker.chunk(input);
		// Get the sequential command tokens.
		List<String> tokens = new ArrayList<>();
		for (Chunk chunk : chunking.chunkSet()) {
			tokens.add(chunk.type());
		}
		if (tokens.size() == 0) {
			return autoCompleter.autoComplete(input);
		}
		switch (tokens.get(0)) {
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

		default:
			return "Cannot recognize the command!";
		}

	}

	public static void main(String[] args) {

		NLTranslator translator = new NLTranslator("git", "compile");
		String command = "superman find difference between 52345f and d89a32";
		try {
			System.out.println(translator.translate(command));

			command = "superman clone the repo git://www.github.haha.git";
			System.out.println(translator.translate(command));

			command = "superman run helloworld.java";
			System.out.println(translator.translate(command));

			command = "superman run helloworld.c";
			System.out.println(translator.translate(command));
			
			command = "superman runnn helloworld.c";
			System.out.println(translator.translate(command));
			
			command = "superman please delete helloworld.c";
			System.out.println(translator.translate(command));
			
			command = "superman please move helloworld.c D:/good.c";
			System.out.println(translator.translate(command));
			
			command = "make commission";
			System.out.println(translator.translate(command));
			
			command = "superman please merge #abc and #xyz using stratege ours";
			System.out.println(translator.translate(command));
			
			command = "superman please reset 532eac";
			System.out.println(translator.translate(command));
			
			command = "superman fetch #origin";
			System.out.println(translator.translate(command));
			
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}
	}
}
