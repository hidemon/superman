package semantics.chunker;

import com.aliasi.chunk.RegExChunker;
/**
 * The chunker for filenames.
 * 
 * @author Yu
 */
public class FilenameRegExChunker extends RegExChunker {

	private static final long serialVersionUID = 8877775874859603758L;

	public FilenameRegExChunker() {
        super(FILENAME_REGEX,CHUNK_TYPE,CHUNK_SCORE);
    }

    private final static String FILENAME_REGEX = "[^\\s]+\\.[A-Za-z]{1,5}";

    private final static String CHUNK_TYPE = "filename";

    private final static double CHUNK_SCORE = 0.0;
}
