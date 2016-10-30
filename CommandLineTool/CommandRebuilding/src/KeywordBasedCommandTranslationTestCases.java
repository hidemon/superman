/**
 * Created by lijingjiang on 10/30/16 3:02PM.
 */

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class KeywordBasedCommandTranslationTestCases {

  @Test()
  public void testCloneBasic() {
    String input = "superman clone https://github.com/lijingjiang/reponame";
    String translationResult =
            KeywordBasedCommandTranslation.nlpToCommand(input);
    assertEquals(translationResult,
                 "git clone https://github.com/lijingjiang/reponame;");
  }


  @Test()
  public void testAddAllBasic() {
    String input = "superman add all";
    String translationResult =
        KeywordBasedCommandTranslation.nlpToCommand(input);
    assertEquals(translationResult, "git add --all;");
  }

  @Test()
  public void testAddInteractiveBasic()  {
    String input = "superman add interactively";
    String translationResult =
            KeywordBasedCommandTranslation.nlpToCommand(input);
    assertEquals(translationResult, "git add -i;");
  }

  @Test()
  public void testCommit()  {
    String input = "superman add interactively";
    String translationResult =
            KeywordBasedCommandTranslation.nlpToCommand(input);
    assertEquals(translationResult, "git add -i;");
  }

  @Test()
  public void testCommit2()  {
    String input = "superman please amend my commit";
    String translationResult =
            KeywordBasedCommandTranslation.nlpToCommand(input);
    assertEquals(translationResult, "git commit --amend;");
  }
}
