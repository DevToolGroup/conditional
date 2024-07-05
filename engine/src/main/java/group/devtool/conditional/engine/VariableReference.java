/*
 * The Conditional rule engine, similar to Drools, 
 * introduces the definition of input and output parameters, 
 * thereby demarcating the boundaries between programmers and business personnel. 
 * 
 * It reduces the complexity of rules, making it easier for business staff to maintain and use them.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
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
