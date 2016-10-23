package semantics.translate;

import java.util.ArrayList;
import java.util.List;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.ExactDictionaryChunker;

import semantics.chunker.SemanticsMap;
import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;

/**
 * The core class which is responsible for translating
 * natural language to structural instructions.
 * 
 * @author Yu
 */
public class NLTranslator {
	private SemanticsMap semanticsMap;
	
	public NLTranslator(String... args) {
		semanticsMap = new SemanticsMap(args);
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
	    	throw new NoTokenFoundException();
	    }
	    switch(tokens.get(0)) {
	    case "diff": return GitTranslator.translateDiff(input);
	    case "clone": return GitTranslator.translateClone(input);
	    case "pull": return GitTranslator.translatePull(input);
	    case "compile": return CompilationTranslator.translateCompile(input);
	    case "run": return CompilationTranslator.translateRun(input);
	    default: return "Cannot recognize the command!";
	    }
	    
	}
	
	public static void main(String[] args) {
		
		NLTranslator translator = new NLTranslator("git", "compile");
		String command = "superman find difference between 52345f and d89a32";
		try {
			System.out.println(translator.translate(command));
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}
		
		command = "superman clone the repo git://www.github.haha.git";
		try {
			System.out.println(translator.translate(command));
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}
		
		command = "superman run helloworld.java";
		try {
			System.out.println(translator.translate(command));
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}
		
		command = "superman run helloworld.c";
		try {
			System.out.println(translator.translate(command));
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}
		
	}
	
}
