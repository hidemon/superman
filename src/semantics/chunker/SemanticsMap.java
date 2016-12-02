package semantics.chunker;

import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;

/**
 * The chunker for synonyms.
 * 
 * @author Yu
 */
public class SemanticsMap {
	MapDictionary<String> dictionary;
	static final double CHUNK_SCORE = 1.0;

	public SemanticsMap(String... args) {
		dictionary = new MapDictionary<String>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("git")) {
				setUpGit();
			} else if (args[i].equals("compile")) {
				setUpComplition();
			}
		}
	}

	public void setUpGit() {
		dictionary.addEntry(new DictionaryEntry<String>("status", "status", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("config", "config", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("configuration", "config", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("set", "config", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("difference", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("diff", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("distinction", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("difference", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("disparity", "diff", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("clone", "clone", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("merge", "merge", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("pull", "pull", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("push", "push", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("fetch", "fetch", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("sync", "pull", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("synchronize", "pull", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("rm", "rm", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("remove", "rm", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("delete", "rm", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("commit", "commit", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("commission", "commit", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("submit", "commit", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("merge", "merge", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("reset", "reset", CHUNK_SCORE));
		
		dictionary.addEntry(new DictionaryEntry<String>("mv", "mv", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("move", "mv", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("transfer", "mv", CHUNK_SCORE));
		
	}
	
	public void setUpComplition() {
		dictionary.addEntry(new DictionaryEntry<String>("compile", "compile", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("javac", "compile", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("run", "run", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("java", "run", CHUNK_SCORE));
	}

	public ExactDictionaryChunker getChunker() {
		boolean returnAllMatches = true;
		boolean caseSensitive = false;
		ExactDictionaryChunker dictionaryChunker = new ExactDictionaryChunker(dictionary,
				IndoEuropeanTokenizerFactory.INSTANCE, returnAllMatches, caseSensitive);
		return dictionaryChunker;
	}
}
