package group.devtool.conditional.web.controller;

import group.devtool.conditional.web.request.ReturnClassRequest;
import group.devtool.conditional.web.response.ReturnClassResponse;
import group.devtool.conditional.web.service.ReturnClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/conditional/ruleClass")
public class ReturnClassController {

	// 注入ReturnClassService
	@Autowired
	private ReturnClassService returnClassService;

	@PostMapping("/{ruleId}/{versionId}/returnClass")
	public Integer addReturnClass(@PathVariable("ruleId") Integer ruleId,
																@PathVariable("versionId") Integer versionId,
																@RequestBody ReturnClassRequest request) {
		return returnClassService.addReturnClass(request, versionId, ruleId);
	}

	// 删除返回定义
	@DeleteMapping("/{ruleId}/returnClass/{returnId}")
	public void deleteReturnClass(@PathVariable("ruleId") Integer ruleId,
																@PathVariable("versionId") Integer versionId,
																@PathVariable("returnId") Integer returnId) {
		returnClassService.deleteReturnClass(returnId, versionId, ruleId);
	}

	@PutMapping("/{ruleId}/returnClass/{returnId}")
	public void updateReturnClass(@PathVariable("ruleId") Integer ruleId,
																@PathVariable("versionId") Integer versionId,
																@PathVariable("returnId") Integer returnId,
																@RequestBody ReturnClassRequest request) {
		returnClassService.updateReturnClass(request, returnId, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/returnClass/{returnId}")
	public ReturnClassResponse getReturnClass(@PathVariable("ruleId") Integer ruleId,
																						@PathVariable("versionId") Integer versionId,
																						@PathVariable("returnId") Integer returnId) {
		// 检查返回定义是否存在，如果不存在，提示错误：规则返回定义不存在
		return returnClassService.getReturnClass(returnId, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/returnClass")
	public ReturnClassResponse getReturnClasses(@PathVariable("ruleId") Integer ruleId,
																							@PathVariable("versionId") Integer versionId) {
		return returnClassService.getReturnClasses(versionId, ruleId);
	}

}
