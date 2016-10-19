package semantics.chunker;

import com.aliasi.chunk.RegExChunker;

/**
 *  The chunker for URL
 * @author Yu
 */
public class UrlRegExChunker extends RegExChunker {

	private static final long serialVersionUID = -4527738404231966583L;

	public UrlRegExChunker() {
        super(URL_REGEX,CHUNK_TYPE,CHUNK_SCORE);
    }

    private final static String URL_REGEX
        = "(https?|ftps?|git|ssh)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private final static String CHUNK_TYPE = "url";

    private final static double CHUNK_SCORE = 0.0;

}
