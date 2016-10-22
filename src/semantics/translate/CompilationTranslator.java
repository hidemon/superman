package semantics.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;

import semantics.chunker.FilenameRegExChunker;
import semantics.exception.NoTokenFoundException;

public class CompilationTranslator {

	public static String translateCompile(String input) throws NoTokenFoundException {
		Chunker chunker = new FilenameRegExChunker();
		Chunking chunking= chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		List<String> filenames = getFilenames(chunkSet, input);
		// Get the 1st chunk to get the file type of the compiling file.
		String fileType = getFileType(filenames.get(0));
		switch (fileType) {
		case "java": return generateCommand(filenames, "javac");
		case "c":
		case "cc":
		case "cpp": return generateCommand(filenames, "cl");
		}
		return "";
	}
	
	public static String translateRun(String input) throws NoTokenFoundException {
		Chunker chunker = new FilenameRegExChunker();
		Chunking chunking= chunker.chunk(input);
		Set<Chunk> chunkSet = chunking.chunkSet();
		if (chunkSet.size() == 0) {
			throw new NoTokenFoundException();
		}
		List<String> filenames = getFilenames(chunkSet, input);
		// Get the 1st chunk to get the file type of the compiling file.
		String fileType = getFileType(filenames.get(0));
		switch (fileType) {
		case "java": 
			String command1 = generateCommand(filenames, "javac");
			String command2 = generateCommand(transformFileType(filenames, "class"), "java");
			return command1 + '\n' + command2;
		case "class":
			 return generateCommand(filenames, "java");
		case "c":
		case "cc":
			// To do..
		case "cpp": return generateCommand(filenames, "cl");
		}
		return "";
	}
	
	private static List<String> getFilenames(Set<Chunk> chunkSet, String input) {
		List<String> files = new ArrayList<>();
		for (Chunk chunk : chunkSet) {
			files.add(input.substring(chunk.start(), chunk.end()));
		}
		return files;
	}
	
	// Transform one type of file to another type of file.
	// For example, input : ["hello.java"], "class" --> output : ["hello.class"]
	private static List<String> transformFileType(List<String> oldFilenames, String newType) {
		List<String> newFilenames = new ArrayList<>();
		for (String filename : oldFilenames) {
			newFilenames.add(filename.split("\\.")[0] + "." + newType);
		}
		return newFilenames;
	}
	
	private static String getFileType(String filename) {
		return filename.split("\\.")[1];
	}
	
	private static String generateCommand(List<String> filenames, String command) {
		StringBuilder result = new StringBuilder(command);
		for (String filename : filenames) {
			result.append(' ').append(filename);
		}
		return result.toString();
	}

}
