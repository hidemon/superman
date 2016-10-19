package semantics.chunker;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.ExactDictionaryChunker;

public class ChunkerTest {
	@Test
	public void UrlRegExChunkerTest() {
		Chunker chunker = new UrlRegExChunker();
		String s = "git clone git://git.kernel.org/pub/scm/.../linux.git my-linux";

        Chunking chunking = chunker.chunk(s);
        Set<Chunk> chunkSet = chunking.chunkSet();
        Iterator<Chunk> it = chunkSet.iterator();
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            String result = s.substring(start,end);
            String expectedResult = "git://git.kernel.org/pub/scm/.../linux.git";
            assertEquals(result, expectedResult);
        }
	}
	
	@Test
	public void SemanticsMapTest() {
		SemanticsMap map = new SemanticsMap();
		ExactDictionaryChunker dictionaryChunker = map.getChunker();
		String text = "supergit find difference";
		Chunking chunking = dictionaryChunker.chunk(text);
	    for (Chunk chunk : chunking.chunkSet()) {
	        String type = chunk.type();
            String expectedType = "diff";
            assertEquals(type, expectedType);	        
	    }
	}
}
