package semantics.translate;

import java.util.Iterator;
import java.util.Set;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;

import semantics.chunker.BranchRegExChunker;
import semantics.chunker.FilenameRegExChunker;
import semantics.chunker.SHARegExChunker;
import semantics.chunker.UrlRegExChunker;
import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;

/**
 * The class which is responsible for translating commands regarding git to
 * structural instructions.
 * 
 * @author Yu
 */
public class GitTranslator {

	public static String translateDiff(String input) throws InvalidParameterException, NoTokenFoundException {
		Chunker chunker = new SHARegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			return "git diff";
		}
		if (chunkSet.size() != 2) {
			throw new InvalidParameterException();
		}
		Iterator<Chunk> it = chunkSet.iterator();
		StringBuilder result = new StringBuilder("git diff");
		while (it.hasNext()) {
			Chunk chunk = it.next();
			int start = chunk.start();
			int end = chunk.end();
			result.append(' ').append(input.substring(start, end));
		}
		return result.toString();
	}

	public static String translateClone(String input) throws InvalidParameterException, NoTokenFoundException {
		Chunker chunker = new UrlRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		if (chunkSet.size() != 1) {
			throw new InvalidParameterException();
		}
		Chunk urlChunk = chunkSet.iterator().next();
		StringBuilder result = new StringBuilder("git clone ");
		result.append(input.substring(urlChunk.start(), urlChunk.end()));
		return result.toString();
	}
	
	public static String translateMv(String input) throws InvalidParameterException, NoTokenFoundException {
		Chunker chunker = new FilenameRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		if (chunkSet.size() != 2) {
			throw new InvalidParameterException();
		}
		Iterator<Chunk> it = chunkSet.iterator();
		StringBuilder result = new StringBuilder("git mv");
		while (it.hasNext()) {
			Chunk chunk = it.next();
			int start = chunk.start();
			int end = chunk.end();
			result.append(' ').append(input.substring(start, end));
		}
		return result.toString();
	}

	public static String translatePull(String input) {
		return "git pull";
	}

	public static String translateRm(String input) throws NoTokenFoundException, InvalidParameterException {
		Chunker chunker = new FilenameRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		if (chunkSet.size() != 1) {
			throw new InvalidParameterException();
		}
		Chunk urlChunk = chunkSet.iterator().next();
		StringBuilder result = new StringBuilder("git rm ");
		result.append(input.substring(urlChunk.start(), urlChunk.end()));
		return result.toString();
	}
	
	public static String translateCommit(String input) throws NoTokenFoundException {
		// Default comment
		String comment = "new commit";
		int commentS = input.indexOf('"');
		if (commentS != -1) {
			 input = input.substring(commentS);
			 int commentE = input.indexOf('"');
			 if (commentE != -1) {
				 comment = input.substring(0, commentE + 1);
			 }
		}
		return "git commit -m " + "\"" + comment + "\"";
	}

	// Have to use a '#' as a prefix of branch name
	public static String translateMerge(String input) throws NoTokenFoundException {
		Chunker chunker = new BranchRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		StringBuilder result = new StringBuilder("git merge");
		
		Iterator<Chunk> it = chunkSet.iterator();
		while (it.hasNext()) {
			Chunk chunk = it.next();
			int start = chunk.start();
			int end = chunk.end();
			result.append(' ').append(input.substring(start, end));
		}
		return result.toString();
	}
}
