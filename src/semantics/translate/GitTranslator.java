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

	final static String[] strategies = { "resolve", "recursive", "ours", "octopus", "subtree" };

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

	public static String translatePull(String input) throws InvalidParameterException {
		Chunker chunker = new BranchRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		StringBuilder result = new StringBuilder("git pull");
		if (chunkSet.size() == 0) {
			return result.toString();
		}
		if (chunkSet.size() != 2) {
			throw new InvalidParameterException();
		}
		Iterator<Chunk> it = chunkSet.iterator();
		while (it.hasNext()) {
			Chunk chunk = it.next();
			int start = chunk.start();
			int end = chunk.end();
			result.append(' ').append(input.substring(start + 1, end));
		}
		return result.toString();
	}

	public static String translateFetch(String input) throws InvalidParameterException {
		Chunker chunker = new BranchRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		StringBuilder result = new StringBuilder("git fetch");
		if (chunkSet.size() == 0) {
			return result.toString();
		}
		if (chunkSet.size() != 1) {
			throw new InvalidParameterException();
		}
		Chunk urlChunk = chunkSet.iterator().next();
		result.append(' ').append(input.substring(urlChunk.start() + 1, urlChunk.end()));
		return result.toString();
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
		// Use simple scan to find the stratege
		for (String stratege : strategies) {
			if (input.indexOf(stratege) != -1) {
				result.append(" -s " + stratege);
			}
		}
		Iterator<Chunk> it = chunkSet.iterator();
		while (it.hasNext()) {
			Chunk chunk = it.next();
			int start = chunk.start();
			int end = chunk.end();
			result.append(' ').append(input.substring(start + 1, end));
		}
		return result.toString();
	}

	public static String translateReset(String input) throws NoTokenFoundException, InvalidParameterException {
		Chunker chunker = new SHARegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		if (chunkSet.size() != 1) {
			throw new InvalidParameterException();
		}
		Chunk urlChunk = chunkSet.iterator().next();
		StringBuilder result = new StringBuilder("git reset --hard ");
		result.append(input.substring(urlChunk.start(), urlChunk.end()));
		return result.toString();
	}

	public static String translatePush(String input) throws NoTokenFoundException {
		Chunker chunker = new BranchRegExChunker();
		Chunking chunking = chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			return "git push original master";
		}
		StringBuilder result = new StringBuilder("git push");

		Iterator<Chunk> it = chunkSet.iterator();
		while (it.hasNext()) {
			Chunk chunk = it.next();
			int start = chunk.start();
			int end = chunk.end();
			String branchName = input.substring(start, end);
			result.append(' ').append(branchName.charAt(0) == '#' ? branchName.substring(1) : branchName);
		}
		return result.toString();
	}
}
