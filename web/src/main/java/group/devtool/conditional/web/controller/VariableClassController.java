package group.devtool.conditional.web.controller;

import group.devtool.conditional.web.request.VariableClassRequest;
import group.devtool.conditional.web.response.VariableClassResponse;
import group.devtool.conditional.web.service.VariableClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditional/ruleClass")
public class VariableClassController {

	@Autowired
	private VariableClassService variableClassService;

	@PostMapping("/{ruleId}/{versionId}/variableClass")
	public void addVariableClass(@PathVariable("ruleId") Integer ruleId,
															 @PathVariable("versionId") Integer versionId,
															 @RequestBody VariableClassRequest request) {
		variableClassService.addVariableClass(request, versionId, ruleId);
	}

	// 删除规则变量定义
	@DeleteMapping("/{ruleId}/{versionId}/variableClass/{variableId}")
	public void deleteVariableClass(@PathVariable("ruleId") Integer ruleId,
																	@PathVariable("versionId") Integer versionId,
																	@PathVariable("variableId") Integer variableId) {
		variableClassService.deleteVariableClass(variableId, versionId, ruleId);
	}

	// 修改规则变量定义
	@PutMapping("/{ruleId}/{versionId}/variableClass/{variableId}")
	public void updateVariableClass(@PathVariable("ruleId") Integer ruleId,
																	@PathVariable("versionId") Integer versionId,
																	@PathVariable("variableId") Integer variableId,
																	@RequestBody VariableClassRequest request) {
		variableClassService.updateVariableClass(request, variableId, versionId, ruleId);
	}

	// 查询规则变量定义列表
	@GetMapping("/{ruleId}/{versionId}/variableClass")
	public Page<VariableClassResponse> getVariableClassList(@PathVariable("ruleId") Integer ruleId,
																													@PathVariable("versionId") Integer versionId,
																													@RequestParam(name = "pageNumber", value = "0") Integer pageNumber,
																													@RequestParam(name = "pageSize", value = "10") Integer pageSize,
																													@RequestParam(name = "code") String code
																													) {
		return variableClassService.getVariableClasses(code, pageNumber, pageSize, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/variableClass/{variableId}")
	public VariableClassResponse getVariableClass(@PathVariable("ruleId") Integer ruleId,
																								@PathVariable("versionId") Integer versionId,
																								@PathVariable("variableId") Integer variableId) {
		// 检查规则变量定义，如果不存在，提示错误：规则变量定义不存在
		return variableClassService.getVariableClass(variableId, versionId, ruleId);
	}

}
