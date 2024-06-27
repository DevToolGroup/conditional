package group.devtool.conditional.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import group.devtool.conditional.engine.AbstractRuleClassLoader.TypeLoader;
import group.devtool.conditional.engine.RuleClassException.RuleClassSyntaxException;

public class TypeLoaderTest {

  public List<Character> simple = new ArrayList<>();
  public List<Character> collection = new ArrayList<>();
  public List<Character> error = new ArrayList<>();

  @Before
  public void read() {
    String simpleDeclaredString = "Integer ";
    for (int i = 0; i < simpleDeclaredString.length(); i ++) {
      simple.add(simpleDeclaredString.charAt(i));
    }

    String collectionDeclaredString = "List<User> ";
    for (int i = 0; i < collectionDeclaredString.length(); i ++) {
      collection.add(collectionDeclaredString.charAt(i));
    }

    String errorCollectionDeclaredString = " List<User> ";
    for (int i = 0; i < errorCollectionDeclaredString.length(); i ++) {
      error.add(errorCollectionDeclaredString.charAt(i));
    }


  }

  @Test
  public void testSimpleTypeLoader() {
    TypeLoader loader = new TypeLoader();
    try {
      int end = loader.load(0, simple, null);
      assertEquals(7, end);
      assertEquals("Integer", loader.pop().getValue());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testCollectionTypeLoader() {
    TypeLoader loader = new TypeLoader();
    try {
      int end = loader.load(0, collection, null);
      assertEquals(10, end);
      assertEquals("List<User>", loader.pop().getValue());
    } catch (RuleClassException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testErrorCollectionTypeLoader() {
    TypeLoader loader = new TypeLoader();
    try {
      int end = loader.load(0, error, null);
    } catch (RuleClassException e) {
      assertTrue(e instanceof RuleClassSyntaxException);
    }

  }
}
