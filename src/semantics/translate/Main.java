package semantics.translate;

import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;

public class Main {

	public static void main(String[] args) {
		NLTranslator translator = new NLTranslator("git", "compile");
		if (args.length == 0) throw new IllegalArgumentException();
		String command = args[0];
		try {
			String result = translator.translate(command);
			if (result.equals("Cannot recognize the command!")) {
				System.err.println("Cannot recognize the command!");
			} else {
				System.out.println(result);
			}
		} catch (NoTokenFoundException | InvalidParameterException e) {
			e.printStackTrace();
		}

	}

}
