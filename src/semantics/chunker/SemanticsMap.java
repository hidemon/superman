package semantics.chunker;

import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;

/**
 * The chunker for synonyms
 * 
 * @author Yu
 */
public class SemanticsMap {
	MapDictionary<String> dictionary;
	static final double CHUNK_SCORE = 1.0;

	public SemanticsMap() {
		dictionary = new MapDictionary<String>();
		setUpGit();
	}

	public void setUpGit() {
		dictionary.addEntry(new DictionaryEntry<String>("difference", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("distinction", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("difference", "diff", CHUNK_SCORE));
		dictionary.addEntry(new DictionaryEntry<String>("disparity", "diff", CHUNK_SCORE));
	}

	public ExactDictionaryChunker getChunker() {
		boolean returnAllMatches = true;
		boolean caseSensitive = false;
		ExactDictionaryChunker dictionaryChunker = new ExactDictionaryChunker(dictionary,
				IndoEuropeanTokenizerFactory.INSTANCE, returnAllMatches, caseSensitive);
		return dictionaryChunker;
	}
}
