package semantics.autocomplete;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.SortedSet;

import com.aliasi.io.FileLineReader;
import com.aliasi.spell.AutoCompleter;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.util.ObjectToCounterMap;
import com.aliasi.util.ScoredObject;

import semantics.exception.NoTokenFoundException;
/**
 *  The Autocomplete module for handling the imcomplete commands.
 * @author Yu
 */
public class AutoComplete {
	ObjectToCounterMap<String> commandToFreq;
	FixedWeightEditDistance editDistance;
	AutoCompleter completer;

	public AutoComplete(String filename) {
		commandToFreq = new ObjectToCounterMap<String>();
		double matchWeight = 0.0;
		double insertWeight = -10.0;
		double substituteWeight = -10.0;
		double deleteWeight = -10.0;
		double transposeWeight = Double.NEGATIVE_INFINITY;
		editDistance = new FixedWeightEditDistance(matchWeight, deleteWeight, insertWeight, substituteWeight,
				transposeWeight);
		setUpCompleter(filename);
	}

	private void setUpCompleter(String filename) {
		try {
			setUpMap(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int maxResults = 5;
		int maxQueueSize = 10000;
		double minScore = -25.0;
		completer = new AutoCompleter(commandToFreq, editDistance, maxResults, maxQueueSize, minScore);
	}

	private void setUpMap(String filename) throws UnsupportedEncodingException, IOException {
		File wordsFile = new File(filename);
		String[] lines = FileLineReader.readLineArray(wordsFile, "ISO-8859-1");

		int lineCount = 0;
		for (String line : lines) {
			if (lineCount++ < 1)
				continue;
			int i = line.lastIndexOf(',');
			if (i < 0)
				continue;
			String phrase = line.substring(0, i);
			String countString = line.substring(i + 1);
			Integer count = Integer.valueOf(countString);
			commandToFreq.set(phrase, count);
		}
	}

	/**
	 * @param incompleteCommand The incomplete command.
	 * @return The hints for the potential complete command.
	 * @throws NoTokenFoundException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public String autoComplete(String incompleteCommand) throws NoTokenFoundException {

		StringBuilder hint = new StringBuilder();
		String[] tokens = incompleteCommand.split(" ");
		for (String token : tokens) {
			SortedSet<ScoredObject<String>> completions = completer.complete(token);
			if (!completions.isEmpty()) {
				// Get the first object.
				ScoredObject<String> firstCompletion = completions.iterator().next();
				hint.append(
						"By '" + token + "', " + " Do you mean '" + firstCompletion.getObject() + "'?");
				hint.append('\n');
			}
		}
		if (hint.length() == 0) return "#" + incompleteCommand;
		return hint.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		AutoComplete ac = new AutoComplete("command.csv");
		try {
			System.out.println(ac.autoComplete("jav clon"));
		} catch (NoTokenFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
