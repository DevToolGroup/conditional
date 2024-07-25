package group.devtool.conditional.web.controller;

import group.devtool.conditional.web.request.ArgumentClassRequest;
import group.devtool.conditional.web.response.ArgumentClassResponse;
import group.devtool.conditional.web.service.ArgumentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditional/ruleClass")
public class ArgumentClassController {

	@Autowired
	private ArgumentClassService argumentService;

	@PostMapping("/{ruleId}/{versionId}/argumentClass")
	public Integer addArgumentClass(@PathVariable("ruleId") Integer ruleId,
																	@PathVariable("versionId") Integer versionId,
																	@RequestBody ArgumentClassRequest request) {
		return argumentService.addArgumentClass(request, versionId, ruleId);
	}

	@DeleteMapping("/{ruleId}/{versionId}/argumentClass/{argumentId}")
	public void deleteArgumentClass(@PathVariable("ruleId") Integer ruleId,
																	@PathVariable("versionId") Integer versionId,
																	@PathVariable("argumentId") Integer argumentId) {
		argumentService.deleteArgumentClass(ruleId, versionId, argumentId);
	}

	@PutMapping("/{ruleId}/{versionId}/argumentClass/{argumentId}")
	public void updateArgumentClass(@PathVariable("ruleId") Integer ruleId,
																	@PathVariable("versionId") Integer versionId,
																	@PathVariable("argumentId") Integer argumentId,
																	@RequestBody ArgumentClassRequest request) {
		argumentService.updateArgumentClass(request, argumentId, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/argumentClass")
	public Page<ArgumentClassResponse> getArgumentClassList(@PathVariable("ruleId") Integer ruleId,
																													@PathVariable("versionId") Integer versionId,
																													@RequestParam(name = "code", required = false) String code,
																													@RequestParam(name = "pageNumber", value = "0") Integer pageNumber,
																													@RequestParam(name = "pageSize", value = "10") Integer pageSize) {
		return argumentService.getArgumentClasses(code, pageNumber, pageSize, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/argumentClass/{argumentId}")
	public ArgumentClassResponse getArgumentClass(@PathVariable("ruleId") Integer ruleId,
																								@PathVariable("versionId") Integer versionId,
																								@PathVariable("argumentId") Integer argumentId) {
		return argumentService.getArgumentClass(ruleId, versionId, argumentId);
	}

}
