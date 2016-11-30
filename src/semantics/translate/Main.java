package semantics.translate;

import java.util.Scanner;

import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;

public class Main {

	public static void main(String[] args) {
		NLTranslator translator = new NLTranslator("git", "compile");
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String command = scanner.nextLine();
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

}
