package group.devtool.conditional.web.controller;

import group.devtool.conditional.web.request.ConditionClassRequest;
import group.devtool.conditional.web.response.ConditionClassResponse;
import group.devtool.conditional.web.service.ConditionClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditional/ruleClass")
public class ConditionClassController {

	@Autowired
	private ConditionClassService conditionService;

	@PostMapping("/{ruleId}/{versionId}/conditionClass")
	public void addConditionClass(@PathVariable("ruleId") Integer ruleId,
																@PathVariable("versionId") Integer versionId,
																@RequestBody ConditionClassRequest request) {
		conditionService.addConditionClass(request, versionId, ruleId);
	}

	@DeleteMapping("/{ruleId}/{versionId}/conditionClass/{conditionId}")
	public void deleteConditionClass(@PathVariable("ruleId") Integer ruleId,
																	 @PathVariable("versionId") Integer versionId,
																	 @PathVariable("conditionId") Integer conditionId) {
		conditionService.deleteConditionClass(conditionId, versionId, ruleId);
	}

	@PutMapping("/{ruleId}/{versionId}/conditionClass/{conditionId}")
	public void updateConditionClass(@PathVariable("ruleId") Integer ruleId,
																	 @PathVariable("versionId") Integer versionId,
																	 @PathVariable("conditionId") Integer conditionId,
																	 @RequestBody ConditionClassRequest request) {
		conditionService.updateConditionClass(request, conditionId, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/conditionClass/{conditionId}")
	public ConditionClassResponse getConditionClass(@PathVariable("ruleId") Integer ruleId,
																									@PathVariable("versionId") Integer versionId,
																									@PathVariable("conditionId") Integer conditionId) {
		return conditionService.getConditionClass(conditionId, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/conditionClass")
	public Page<ConditionClassResponse> getConditionClassList(@PathVariable("ruleId") Integer ruleId,
																														@PathVariable("versionId") Integer versionId,
																														@RequestParam(name = "pageNumber", value = "0") Integer pageNumber,
																														@RequestParam(name = "pageSize", value = "10") Integer pageSize
																														) {
		return conditionService.getConditionClasses(pageNumber, pageSize, versionId, ruleId);
	}

}
