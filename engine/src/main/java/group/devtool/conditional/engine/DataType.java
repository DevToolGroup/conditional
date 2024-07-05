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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public enum DataType {
  
  Integer(Integer.class),
  Long(Long.class),
  Float(Float.class),
  Double(Double.class),
  Decimal(BigDecimal.class),
  Boolean(Boolean.class),
  String(String.class),
  Time(Date.class),
  List(List.class),
  Map(Map.class);

  private Class<?> type;

  DataType(Class<?> type) {
    this.type = type;
  }

  public Class<?> getType() {
    return type;
  }

  public static Set<String> getSystemTypes() {
    Set<String> result = new HashSet<>();

    DataType[] types = values();
    for (DataType dataType : types) {
      result.add(dataType.name());
    }
    return result;
  }

  public static Set<String> getBaseTypes() {
    Set<String> result = new HashSet<>();
    result.add(Integer.name());
    result.add(Long.name());
    result.add(Float.name());
    result.add(Double.name());
    result.add(Decimal.name());
    result.add(Boolean.name());
    result.add(String.name());
    result.add(Time.name());
    return result;
  }

}
