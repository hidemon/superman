package semantics.translate;

import java.util.Iterator;
import java.util.Set;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;

import semantics.chunker.SHARegExChunker;
import semantics.chunker.UrlRegExChunker;
import semantics.exception.InvalidParameterException;
/**
 * The class which is responsible for translating
 * commands regarding git to structural instructions.
 * 
 * @author Yu
 */
public class GitTranslator {
	
	public static String translateDiff(String input) throws InvalidParameterException {
		Chunker chunker = new SHARegExChunker();
        Chunking chunking = chunker.chunk(input);
        Set<Chunk> chunkSet = chunking.chunkSet();
        if (chunkSet.size() != 2) {
        	throw new InvalidParameterException();
        }
        Iterator<Chunk> it = chunkSet.iterator();
        StringBuilder result = new StringBuilder("git diff");
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            result.append(' ').append(input.substring(start,end));
        }
        return result.toString();
	}
	
	public static String translateClone(String input) throws InvalidParameterException {
		Chunker chunker = new UrlRegExChunker();
        Chunking chunking = chunker.chunk(input);
        Set<Chunk> chunkSet = chunking.chunkSet();
        if (chunkSet.size() != 1) {
        	throw new InvalidParameterException();
        }
        Chunk urlChunk = chunkSet.iterator().next();
        StringBuilder result = new StringBuilder("git clone ");
        result.append(input.substring(urlChunk.start(), urlChunk.end()));
        return result.toString();
	}

	public static String translatePull(String input) {
		return "git pull";
	}
}
