package group.devtool.conditional.engine;

import java.math.BigDecimal;
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
    return result;
  }

}
