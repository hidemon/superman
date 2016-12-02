package semantics.chunker;

import com.aliasi.chunk.RegExChunker;

public class EmailRegExChunker extends RegExChunker {

	private static final long serialVersionUID = -4124200608827268609L;

	public EmailRegExChunker() {
		super(EMAIL_REGEX, CHUNK_TYPE, CHUNK_SCORE);
	}

	private final static String EMAIL_REGEX = 
			"[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";

	private final static String CHUNK_TYPE = "email";

	private final static double CHUNK_SCORE = 0.0;
}
