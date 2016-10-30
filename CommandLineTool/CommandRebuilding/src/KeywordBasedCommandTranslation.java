/**
 * Created by lijingjiang on 10/30/16 2:57PM.
 */

public class KeywordBasedCommandTranslation {
  public static String nlpToCommand(String input) {
    VerbalExpression gitCreate = VerbalExpression.regex()
                                     .startOfLine()
                                     .then("superman")
                                     .anything()
                                     .then("create")
                                     .anything()
                                     .build();

    if (gitCreate.testExact(input)) {
      String result = "git init;";
      return result;
    }

    VerbalExpression gitRemoteAdding = VerbalExpression.regex()
                                           .startOfLine()
                                           .then("superman")
                                           .anything()
                                           .then("add")
                                           .anything()
                                           .then("remote")
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

    if (gitRemoteAdding.testExact(input)) {
      String userName = gitRemoteAdding.getText(input, 1);
      String repoName = gitRemoteAdding.getText(input, 2);
      String prefix = "git remote add origin https://github.com/";
      String result = prefix + userName + "/" + repoName;
      return result;
    }

    if (gitCreate.testExact(input)) {
      String result = "git init;";
      return result;
    }

    VerbalExpression gitCloneRegex = VerbalExpression.regex()
                                         .startOfLine()
                                         .then("superman")
                                         .anything()
                                         .then("clone")
                                         .anything()
                                         .build();

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

    VerbalExpression gitAddRegex = VerbalExpression.regex()
                                       .startOfLine()
                                       .then("superman")
                                       .anything()
                                       .then("add")
                                       .anything()
                                       .build();

    VerbalExpression gitAddAllRegex = VerbalExpression.regex()
                                          .startOfLine()
                                          .then("superman")
                                          .anything()
                                          .then("add")
                                          .anything()
                                          .then("all")
                                          .anything()
                                          .build();

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

    VerbalExpression gitPushRegex = VerbalExpression.regex()
                                        .startOfLine()
                                        .then("superman")
                                        .anything()
                                        .then("push")
                                        .anything()
                                        .build();

    VerbalExpression gitPushSpecificRemoteRegex = VerbalExpression.regex()
                                                      .startOfLine()
                                                      .then("superman")
                                                      .anything()
                                                      .then("push")
                                                      .anything()
                                                      .then("to")
                                                      .anything()
                                                      .then("\"")
                                                      .capture()
                                                      .anything()
                                                      .anythingBut("\"")
                                                      .endCapture()
                                                      .anything()
                                                      .build();

    VerbalExpression gitPushSpecificRemoteFromSpecificBranchRegex =
        VerbalExpression.regex()
            .startOfLine()
            .then("superman")
            .anything()
            .then("push")
            .anything()
            .then("to")
            .anything()
            .then("\"")
            .capture()
            .anything()
            .anythingBut("\"")
            .endCapture()
            .anything()
            .then("from")
            .anything()
            .then("\"")
            .capture()
            .anything()
            .anythingBut("\"")
            .endCapture()
            .anything()
            .build();

    VerbalExpression
        gitPushSpecificRemoteFromSpecificBranchAtSpecificRemoteRegex =
            VerbalExpression.regex()
                .startOfLine()
                .then("superman")
                .anything()
                .then("push")
                .anything()
                .then("to")
                .anything()
                .then("\"")
                .capture()
                .anything()
                .anythingBut("\"")
                .endCapture()
                .anything()
                .then("at")
                .anything()
                .then("\"")
                .capture()
                .anything()
                .anythingBut("\"")
                .endCapture()
                .anything()
                .then("from")
                .anything()
                .then("\"")
                .capture()
                .anything()
                .anythingBut("\"")
                .endCapture()
                .anything()
                .build();

    if (gitPushRegex.testExact(input)) {
      if (gitPushSpecificRemoteFromSpecificBranchAtSpecificRemoteRegex
              .testExact(input)) {
        String remoteMachine =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 1);
        String remoteBranch =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 2);
        String localBranch =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 3);
        String prefix = "git push ";
        String result =
            prefix + remoteMachine + localBranch + ":" + remoteBranch + ";";
        return result;
      } else if (gitPushSpecificRemoteFromSpecificBranchRegex.testExact(
                     input)) {
        String remoteMachine =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 1);
        String localBranch =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 2);
        String prefix = "git push ";
        String result = prefix + remoteMachine + localBranch + ";";
        return result;
      } else if (gitPushSpecificRemoteRegex.testExact(input)) {
        String remoteMachine =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 1);
        String prefix = "git push ";
        String result = prefix + remoteMachine + ";";
        return result;
      }
      String result = "git push ";
      return result;
    }

    VerbalExpression gitPullRegex = VerbalExpression.regex()
                                        .startOfLine()
                                        .then("superman")
                                        .anything()
                                        .then("pull")
                                        .anything()
                                        .build();

    VerbalExpression gitPullSpecificRemoteRegex = VerbalExpression.regex()
                                                      .startOfLine()
                                                      .then("superman")
                                                      .anything()
                                                      .then("pull")
                                                      .anything()
                                                      .then("from")
                                                      .anything()
                                                      .then("\"")
                                                      .capture()
                                                      .anything()
                                                      .anythingBut("\"")
                                                      .endCapture()
                                                      .anything()
                                                      .build();

    VerbalExpression gitPullSpecificRemoteFromSpecificBranchRegex =
        VerbalExpression.regex()
            .startOfLine()
            .then("superman")
            .anything()
            .then("pull")
            .anything()
            .then("from")
            .anything()
            .then("\"")
            .capture()
            .anything()
            .anythingBut("\"")
            .endCapture()
            .anything()
            .then("at")
            .anything()
            .then("\"")
            .capture()
            .anything()
            .anythingBut("\"")
            .endCapture()
            .anything()
            .build();

    VerbalExpression
        gitPullSpecificRemoteFromSpecificBranchToSpecificLocalBranchRemoteRegex =
            VerbalExpression.regex()
                .startOfLine()
                .then("superman")
                .anything()
                .then("pull")
                .anything()
                .then("from")
                .anything()
                .then("\"")
                .capture()
                .anything()
                .anythingBut("\"")
                .endCapture()
                .anything()
                .then("at")
                .anything()
                .then("\"")
                .capture()
                .anything()
                .anythingBut("\"")
                .endCapture()
                .anything()
                .then("to")
                .anything()
                .then("\"")
                .capture()
                .anything()
                .anythingBut("\"")
                .endCapture()
                .anything()
                .build();

    if (gitPullRegex.testExact(input)) {
      if (gitPullSpecificRemoteFromSpecificBranchToSpecificLocalBranchRemoteRegex
              .testExact(input)) {
        String remoteMachine =
            gitPullSpecificRemoteFromSpecificBranchToSpecificLocalBranchRemoteRegex
                .getText(input, 1);
        String remoteBranch =
            gitPullSpecificRemoteFromSpecificBranchToSpecificLocalBranchRemoteRegex
                .getText(input, 2);
        String localBranch =
            gitPullSpecificRemoteFromSpecificBranchToSpecificLocalBranchRemoteRegex
                .getText(input, 3);
        String prefix = "git pull ";
        String result =
            prefix + remoteMachine + remoteBranch + ":" + localBranch + ";";
        return result;
      } else if (gitPullSpecificRemoteFromSpecificBranchRegex.testExact(
                     input)) {
        String remoteMachine =
            gitPullSpecificRemoteFromSpecificBranchRegex.getText(input, 1);
        String remoteBranch =
            gitPullSpecificRemoteFromSpecificBranchRegex.getText(input, 2);
        String prefix = "git pull ";
        String result = prefix + remoteMachine + remoteBranch + ";";
        return result;
      } else if (gitPullSpecificRemoteRegex.testExact(input)) {
        String remoteMachine =
            gitPushSpecificRemoteFromSpecificBranchRegex.getText(input, 1);
        String prefix = "git pull ";
        String result = prefix + remoteMachine + ";";
        return result;
      }
      String result = "git pull";
      return result;
    }

    VerbalExpression gitBranchRegex = VerbalExpression.regex()
                                          .startOfLine()
                                          .then("superman")
                                          .anything()
                                          .then("branch")
                                          .anything()
                                          .build();

    VerbalExpression gitRemoteBranchRegex = VerbalExpression.regex()
                                                .startOfLine()
                                                .then("superman")
                                                .anything()
                                                .then("remote")
                                                .anything()
                                                .then("branch")
                                                .anything()
                                                .build();

    VerbalExpression gitRemoteBranchReversedRegex = VerbalExpression.regex()
                                                        .startOfLine()
                                                        .then("superman")
                                                        .anything()
                                                        .then("branch")
                                                        .anything()
                                                        .then("remote")
                                                        .anything()
                                                        .build();

    VerbalExpression gitBranchMergedRegex = VerbalExpression.regex()
                                                .startOfLine()
                                                .then("superman")
                                                .anything()
                                                .then("branch")
                                                .anything()
                                                .then("merged")
                                                .anything()
                                                .build();

    VerbalExpression gitBranchMergedReversedRegex = VerbalExpression.regex()
                                                        .startOfLine()
                                                        .then("superman")
                                                        .anything()
                                                        .then("merged")
                                                        .anything()
                                                        .then("branch")
                                                        .anything()
                                                        .build();

    VerbalExpression gitBranchNotMergedRegex = VerbalExpression.regex()
                                                   .startOfLine()
                                                   .then("superman")
                                                   .anything()
                                                   .then("branch")
                                                   .anything()
                                                   .then("not merged")
                                                   .anything()
                                                   .build();

    VerbalExpression gitBranchNotMergedReversedRegex = VerbalExpression.regex()
                                                           .startOfLine()
                                                           .then("superman")
                                                           .anything()
                                                           .then("not merged")
                                                           .anything()
                                                           .then("branch")
                                                           .anything()
                                                           .build();

    if (gitBranchRegex.testExact(input)) {
      if (gitRemoteBranchRegex.testExact(input)) {
        String result = "git branch -r;";
        return result;
      } else if (gitRemoteBranchReversedRegex.testExact(input)) {
        String result = "git branch -r;";
        return result;
      } else if (gitBranchMergedRegex.testExact(input)) {
        String result = "git branch --merged;";
        return result;
      } else if (gitBranchMergedReversedRegex.testExact(input)) {
        String result = "git branch --merged;";
        return result;
      } else if (gitBranchNotMergedRegex.testExact(input)) {
        String result = "git branch --no-merged;";
        return result;
      } else if (gitBranchNotMergedReversedRegex.testExact(input)) {
        String result = "git branch --no-merged;";
        return result;
      }
      String result = "git commit;";
      return result;
    }

    VerbalExpression gitRemoteRegex = VerbalExpression.regex()
                                          .startOfLine()
                                          .then("superman")
                                          .anything()
                                          .then("remote")
                                          .maybe("track")
                                          .anything()
                                          .build();

    if (gitRemoteRegex.testExact(input)) {
      String result = "git remote -v";
      return result;
    }

    VerbalExpression gitLogRegex = VerbalExpression.regex()
                                       .startOfLine()
                                       .then("superman")
                                       .anything()
                                       .then("log")
                                       .anything()
                                       .build();

    VerbalExpression gitLogGraphRegex = VerbalExpression.regex()
                                            .startOfLine()
                                            .then("superman")
                                            .anything()
                                            .then("log")
                                            .anything()
                                            .then("graph")
                                            .anything()
                                            .build();

    VerbalExpression gitLogPrettyRegex = VerbalExpression.regex()
                                             .startOfLine()
                                             .then("superman")
                                             .anything()
                                             .then("log")
                                             .anything()
                                             .then("pretty")
                                             .anything()
                                             .build();

    VerbalExpression gitLogPrettyReversedRegex = VerbalExpression.regex()
                                                     .startOfLine()
                                                     .then("superman")
                                                     .anything()
                                                     .then("pretty")
                                                     .anything()
                                                     .then("log")
                                                     .anything()
                                                     .build();

    if (gitLogRegex.testExact(input)) {
      if (gitLogGraphRegex.testExact(input)) {
        String result = "git log --graph";
        return result;
      } else if (gitLogPrettyRegex.testExact(input)) {
        String result =
            "git log --pretty=format:'%C(yellow)%h%Creset%C(green)%d%Creset %ad %s %Cred(%an)%Creset' --date=short --decorate --graph";
        return result;
      } else if (gitLogPrettyReversedRegex.testExact(input)) {

        String result =
            "git log --pretty=format:'%C(yellow)%h%Creset%C(green)%d%Creset %ad %s %Cred(%an)%Creset' --date=short --decorate --graph";
        return result;
      }
    }

    String result = "unknown command;";
    return result;
  }

  public static void main(String[] args) { nlpToCommand(null); }
}
