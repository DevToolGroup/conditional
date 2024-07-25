package group.devtool.conditional.web.controller;

import group.devtool.conditional.web.request.FactClassRequest;
import group.devtool.conditional.web.request.FactPropertyClassRequest;
import group.devtool.conditional.web.response.FactPropertyClassResponse;
import group.devtool.conditional.web.response.FactClassResponse;
import group.devtool.conditional.web.service.FactClassService;
import group.devtool.conditional.web.service.RuleClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditional/ruleClass")
public class FactClassController {

	@Autowired
	private RuleClassService service;

	@Autowired
	private FactClassService factService;

	@PostMapping("/{ruleId}/{versionId}/factClass")
	public void addFactClass(@PathVariable("ruleId") Integer ruleId,
													 @PathVariable("versionId") Integer versionId,
													 @RequestBody FactClassRequest request) {
		factService.addFactClass(request, versionId, ruleId);
	}

	@DeleteMapping("/{ruleId}/{versionId}/factClass/{factId}")
	public void deleteFactClass(@PathVariable("ruleId") Integer ruleId,
															@PathVariable("versionId") Integer versionId,
															@PathVariable("factId") Integer factId) {
		factService.deleteFactClass(factId, versionId, ruleId);
	}

	@PutMapping("/{ruleId}/{versionId}/factClass/{factId}")
	public void updateFactClass(@PathVariable("ruleId") Integer ruleId,
															@PathVariable("versionId") Integer versionId,
															@PathVariable("factId") Integer factId,
															@RequestBody FactClassRequest request) {
		factService.updateFactClass(request, factId, versionId, ruleId);
	}

	@GetMapping("/{ruleId}/{versionId}/factClass")
	public Page<FactClassResponse> getFactClassList(@PathVariable("ruleId") Integer ruleId,
																									@PathVariable("versionId") Integer versionId,
																									@RequestParam("code") String code,
																									@RequestParam("name") String name,
																									@RequestParam("pageNumber") Integer pageNumber,
																									@RequestParam("pageSize") Integer pageSize
	) {
		return factService.getFactClasses(code, name, pageNumber, pageSize, versionId, ruleId);
	}

	// 查询规则事实详情
	@GetMapping("/{ruleId}/{versionId}/factClass/{factId}")
	public FactClassResponse getFactClass(@PathVariable("ruleId") Integer ruleId,
																				@PathVariable("versionId") Integer versionId,
																				@PathVariable("factId") Integer factId) {
		return factService.getFactClass(factId, versionId, ruleId);
	}

	// 添加规则事实属性
	@PostMapping("/{ruleId}/{versionId}/factClass/{factId}/factPropertyClass")
	public void addFactPropertyClass(@PathVariable("ruleId") Integer ruleId,
																	 @PathVariable("versionId") Integer versionId,
																	 @PathVariable("factId") Integer factId,
																	 @RequestBody FactPropertyClassRequest request) {
		factService.addFactPropertyClass(request, factId, versionId, ruleId);
	}

	// 删除规则事实属性
	@DeleteMapping("/{ruleId}/{versionId}/factClass/{factId}/factPropertyClass/{factPropertyId}")
	public void deleteFactPropertyClass(@PathVariable("ruleId") Integer ruleId,
																			@PathVariable("versionId") Integer versionId,
																			@PathVariable("factPropertyId") Integer factPropertyId,
																			@PathVariable("factId") Integer factId) {
		factService.deleteFactPropertyClass(factPropertyId, factId, versionId, ruleId);
	}

	// 更新规则事实属性
	@PutMapping("/{ruleId}/{versionId}/factClass/{factId}/factPropertyClass/{factPropertyId}")
	public void updateFactPropertyClass(@PathVariable("ruleId") Integer ruleId,
																			@PathVariable("versionId") Integer versionId,
																			@PathVariable("factPropertyId") Integer factPropertyId,
																			@PathVariable("factId") Integer factId,
																			@RequestBody FactPropertyClassRequest request) {
		factService.updateFactPropertyClass(request, factPropertyId, factId, versionId, ruleId);
	}

	// 查询规则事实属性
	@GetMapping("/{ruleId}/{versionId}/factClass/{factId}/factPropertyClass")
	public Page<FactPropertyClassResponse> getFactPropertyClasses(@PathVariable("ruleId") Integer ruleId,
																																@PathVariable("versionId") Integer versionId,
																																@PathVariable("factId") Integer factId,
																																@RequestParam("code") String code,
																																@RequestParam("name") String name,
																																@RequestParam("pageNumber") Integer pageNumber,
																																@RequestParam("pageSize") Integer pageSize) {
		return factService.getFactPropertyClasses(code, name, pageNumber, pageSize, factId, versionId, ruleId);
	}

	// 查询规则事实属性详情
	@GetMapping("/{ruleId}/{versionId}/factClass/{factId}/factPropertyClass/{factPropertyId}")
	public FactPropertyClassResponse getFactPropertyClass(@PathVariable("ruleId") Integer ruleId,
																												@PathVariable("versionId") Integer versionId,
																												@PathVariable("factPropertyId") Integer factPropertyId,
																												@PathVariable("factId") Integer factId) {
		return factService.getFactPropertyClass(factPropertyId, factId, versionId, ruleId);
	}
}
