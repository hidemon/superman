/**
 * Created by lijingjiang on 10/30/16.
 */

public class KeywordBasedCommandTranslation {
  public static String nlpToCommand(String input) {
    /**
     * The following code will create git command from the processed nature
     * language
     *
     * I have created a elastic regular expression Library, so the following
     * code is easy to understand and change
     */

    /**
     * Command input: git clone
     * input requirement: superman *** clone ***
     * eg: superman clone
     */
    VerbalExpression gitCloneRegex = VerbalExpression.regex()
                                         .startOfLine()
                                         .then("superman")
                                         .anything()
                                         .then("clone")
                                         .anything()
                                         .build();

    /**
     * Command input: git clone
     * https://www.github.com/username/reponame/anythingelse;
     * input requirement: superman *** clone *** https://www.github.com/repo
     */
    VerbalExpression gitCloneRepoRegex = VerbalExpression.regex()
                                             .startOfLine()
                                             .then("superman")
                                             .anything()
                                             .then("clone")
                                             .anything()
                                             .then("http")
                                             .maybe("s")
                                             .then("://")
                                             .maybe("www.")
                                             .then("github.com")
                                             .then("/")
                                             .capture()
                                             .anythingBut("/")
                                             .endCapture()
                                             .then("/")
                                             .capture()
                                             .anythingBut("/")
                                             .endCapture()
                                             .anything()
                                             .build();

    if (gitCloneRegex.testExact(input)) {
      if (gitCloneRepoRegex.testExact(input)) {
        String userName = gitCloneRepoRegex.getText(input, 1);
        String repoName = gitCloneRepoRegex.getText(input, 2);
        String prefix = "git clone https://github.com/";
        String result = prefix + userName + "/" + repoName;
        return result;
      }
      String result = "Please provide valid repo url";
      return result;
    }

    /**
     * git Add command
     * git add command group
     */
    VerbalExpression gitAddRegex = VerbalExpression.regex()
                                       .startOfLine()
                                       .then("superman")
                                       .anything()
                                       .then("add")
                                       .anything()
                                       .build();

    /**
     * Command input: git add --all;
     * input requirement: superman *** add *** all ***
     * eg: superman add all change
     */
    VerbalExpression gitAddAllRegex = VerbalExpression.regex()
                                          .startOfLine()
                                          .then("superman")
                                          .anything()
                                          .then("add")
                                          .anything()
                                          .then("all")
                                          .anything()
                                          .build();

    /**
     * Command input: git add -i
     * input requirement: superman *** add *** inteact[ively]***
     * eg: superman add interactive/ interactively
     */
    VerbalExpression gitAddInteactivelyRegex = VerbalExpression.regex()
                                                   .startOfLine()
                                                   .then("superman")
                                                   .anything()
                                                   .then("add")
                                                   .anything()
                                                   .then("interact")
                                                   .maybe("ively")
                                                   .anything()
                                                   .build();

    if (gitAddRegex.testExact(input)) {
      if (gitAddAllRegex.testExact(input)) {
        String result = "git add --all;";
        return result;
      } else if (gitAddInteactivelyRegex.testExact(input)) {
        String result = "git add -i;";
        return result;
      }
      String result = "git add;";
      return result;
    }

    /**
     * git commit command
     * git commit command group
     */
    VerbalExpression gitCommitRegex = VerbalExpression.regex()
                                          .startOfLine()
                                          .then("superman")
                                          .anything()
                                          .then("commit")
                                          .anything()
                                          .build();

    VerbalExpression gitCommitWithMessageRegex = VerbalExpression.regex()
                                                     .startOfLine()
                                                     .then("superman")
                                                     .anything()
                                                     .then("commit")
                                                     .anything()
                                                     .maybe("commit")
                                                     .anything()
                                                     .then("message")
                                                     .anything()
                                                     .then("\"")
                                                     .capture()
                                                     .anythingBut("\"")
                                                     .endCapture()
                                                     .anything()
                                                     .build();

    VerbalExpression gitCommitAmendRegex = VerbalExpression.regex()
                                               .startOfLine()
                                               .then("superman")
                                               .anything()
                                               .then("amend")
                                               .anything()
                                               .then("commit")
                                               .anything()
                                               .build();

    VerbalExpression gitCommitAmendSequenceReversedRegex =
        VerbalExpression.regex()
            .startOfLine()
            .then("superman")
            .anything()
            .then("commit")
            .anything()
            .then("amend")
            .anything()
            .build();

    if (gitCommitRegex.testExact(input)) {
      if (gitCommitWithMessageRegex.testExact(input)) {
        String commitMessage = gitCommitWithMessageRegex.getText(input, 1);
        String prefix = "git commit -m";
        String result = prefix + "\"" + commitMessage + "\""
                        + ";";
        return result;
      } else if (gitCommitAmendRegex.testExact(input)) {
        String result = "git commit --amend;";
        return result;
      } else if (gitCommitAmendSequenceReversedRegex.testExact(input)) {
        String result = "git commit --amend;";
        return result;
      }
      String result = "git commit;";
      return result;
    }

    String result = "unknown command;";
    return result;
  }

  public static void main(String[] args) { nlpToCommand(null); }
}
