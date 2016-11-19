import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CommandLine {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please input the command line: ");
            String command = scanner.nextLine();
            System.out.println(String.format("Please tell me whether you want to execute: %s", command));

            System.out.println("If yes, please enter 1. If no, please enter 0: ");
            boolean flag = Integer.parseInt(scanner.nextLine()) == 1;
            if (flag) {
                System.out.println("Executing the command line now...");
                if (!execute(command)) {
                    System.out.println();
                    continue;
                }
            } else {
                System.out.println("Do you want to exit? If so, please enter 'Exit', else enter any instructions");
            }
            if (scanner.nextLine().equals("Exit")) break;
        }
    }

    private static boolean execute(String line) {
        Runtime rt = Runtime.getRuntime();
        Process pr = null;
        try {
            pr = rt.exec(line);
        } catch (IOException e) {
            System.out.print(e.getMessage());
            return false;
        }
        BufferedReader stdInput = null, stdError = null;
        if (pr.getInputStream() != null) {
            stdInput = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
        }
        if (pr.getInputStream() != null) {
            stdError = new BufferedReader(new
                    InputStreamReader(pr.getErrorStream()));
        }
        if (stdInput != null) {
            System.out.print("Here is the standard output of the command:\n");
            print(stdInput);
        }

        if (stdError != null) {
            print(stdError);
            return false;
        }
        return true;
    }

    private static void print(BufferedReader std) {
        try {
            String s = std.readLine();
            while (s != null) {
                System.out.println(s);
                s = std.readLine();
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
