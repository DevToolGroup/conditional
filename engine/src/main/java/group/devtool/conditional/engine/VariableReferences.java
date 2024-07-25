package group.devtool.conditional.engine;

import java.io.Serializable;
import java.util.List;

/**
 * 变量引用集合
 */
public interface VariableReferences extends Serializable {

	public List<VariableReference> getVariableReferences();

}
