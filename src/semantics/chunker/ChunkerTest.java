package semantics.chunker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.ExactDictionaryChunker;
/**
 * The unit tests for chunkers
 * 
 * @author Yu
 */
public class ChunkerTest {
	@Test
	public void UrlRegExChunkerTest() {
		Chunker chunker = new UrlRegExChunker();
		String s = "git clone git://git.kernel.org/pub/scm/.../linux.git my-linux";

        Chunking chunking = chunker.chunk(s);
        Set<Chunk> chunkSet = chunking.chunkSet();
        assertTrue(chunking.chunkSet().size() > 0);
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
	public void SHARegExChunkerTest() {
		Chunker chunker = new SHARegExChunker();
		String s = "git diff 00236a2ae558 02236a2458";
		String[] expectedResult = {"00236a2ae558", "02236a2458"};
		int i = 0;
        Chunking chunking = chunker.chunk(s);
        Set<Chunk> chunkSet = chunking.chunkSet();
        assertTrue(chunking.chunkSet().size() > 0);
        Iterator<Chunk> it = chunkSet.iterator();
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            String result = s.substring(start,end);
            assertEquals(result, expectedResult[i++]);
        }
	}
	
	@Test
	public void FilenameRegExChunkerTest() {
		Chunker chunker = new FilenameRegExChunker();
		String s = "superman java hello.java world.java";
		String[] expectedResult = {"hello.java", "world.java"};
		int i = 0;
        Chunking chunking = chunker.chunk(s);
        Set<Chunk> chunkSet = chunking.chunkSet();
        assertTrue(chunking.chunkSet().size() > 0);
        Iterator<Chunk> it = chunkSet.iterator();
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            String result = s.substring(start,end);
            assertEquals(result, expectedResult[i++]);
        }
	}
	
	@Test
	public void SemanticsMapTest() {
		SemanticsMap map = new SemanticsMap("git");
		ExactDictionaryChunker dictionaryChunker = map.getChunker();
		String text = "supergit find difference";
		Chunking chunking = dictionaryChunker.chunk(text);
		assertTrue(chunking.chunkSet().size() > 0);
	    for (Chunk chunk : chunking.chunkSet()) {
	        String type = chunk.type();
            String expectedType = "diff";
            assertEquals(type, expectedType);	        
	    }
	}
	
	@Test
	public void BranchRegExTest() {
		Chunker chunker = new BranchRegExChunker();
		String s = "superman merge #abc #xyz";
		String[] expectedResult = {"#abc", "#xyz"};
		int i = 0;
        Chunking chunking = chunker.chunk(s);
        Set<Chunk> chunkSet = chunking.chunkSet();
        assertTrue(chunking.chunkSet().size() > 0);
        Iterator<Chunk> it = chunkSet.iterator();
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            String result = s.substring(start,end);
            assertEquals(result, expectedResult[i++]);
        }
	}
	
	@Test
	public void EmailRegExTest() {
		Chunker chunker = new EmailRegExChunker();
		String s = "nlp git set email \"superman@gmail.com\"";
		String[] expectedResult = {"superman@gmail.com"};
		int i = 0;
        Chunking chunking = chunker.chunk(s);
        Set<Chunk> chunkSet = chunking.chunkSet();
        assertTrue(chunking.chunkSet().size() > 0);
        Iterator<Chunk> it = chunkSet.iterator();
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            String result = s.substring(start,end);
            assertEquals(result, expectedResult[i++]);
        }
	}
}
