package group.devtool.conditional.engine;

import java.util.Iterator;

/**
 * 变量引用
 */
public interface VariableReference {

  public static class SimpleVariableReference implements VariableReference {

    private String var;

    public SimpleVariableReference(String value) {
      this.var = value;
    }

    public String getName() {
      return var;
    }

  }

  public static class NestVariableReference implements VariableReference {

    private VariableExpressionInstance first;

    private VariableExpressionInstance child;

    public NestVariableReference(VariableExpressionInstance first, VariableExpressionInstance child) {
      this.first = first;
      this.child = child;

    }

    public String getName() {
      return first.getExpressionString();
    }

    public Iterator<String> iterator() {
      VariableExpressionInstance next = child;
      return new Iterator<String>() {

        @Override
        public boolean hasNext() {
          if (next instanceof NestVariableExpressionInstanceImpl) {
            return true;
          } else {
            return false;
          }
        }

        @Override
        public String next() {
          NestVariableExpressionInstanceImpl nextNest = (NestVariableExpressionInstanceImpl) next;
          return nextNest.getName();
        }
        
      };
    }


  }


}
