package semantics.chunker;

import com.aliasi.chunk.RegExChunker;

public class BranchRegExChunker extends RegExChunker {

	private static final long serialVersionUID = -53949338187722619L;

	public BranchRegExChunker() {
        super(BRANCH_REGEX,CHUNK_TYPE,CHUNK_SCORE);
    }

    private final static String BRANCH_REGEX = "#[a-z0-9]+";

    private final static String CHUNK_TYPE = "branch";

    private final static double CHUNK_SCORE = 0.0;
}
