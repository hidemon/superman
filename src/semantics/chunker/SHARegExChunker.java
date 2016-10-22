package semantics.chunker;

import com.aliasi.chunk.RegExChunker;
/**
 *  The chunker for SHA
 * @author Yu
 */
public class SHARegExChunker extends RegExChunker {

	private static final long serialVersionUID = -8070711970953772731L;

	public SHARegExChunker() {
        super(SHA_REGEX,CHUNK_TYPE,CHUNK_SCORE);
    }

    private final static String SHA_REGEX = "[a-f0-9]{5,64}";

    private final static String CHUNK_TYPE = "sha";

    private final static double CHUNK_SCORE = 0.0;
}
