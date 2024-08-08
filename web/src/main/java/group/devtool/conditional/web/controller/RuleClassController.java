package group.devtool.conditional.web.controller;

import group.devtool.conditional.web.response.RuleClassVersionResponse;
import group.devtool.conditional.web.service.RuleClassService;
import group.devtool.conditional.web.response.RuleClassResponse;
import group.devtool.conditional.web.request.RuleClassRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// 把以下接口按照类型拆分为不同的文件
@RestController
@RequestMapping(value = "/conditional/ruleClass")
public class RuleClassController {

	@Autowired
	private RuleClassService service;

	// 添加规则定义
	@PostMapping("/")
	public Integer addRuleClass(@RequestBody @Validated RuleClassRequest request) {
		return service.addRuleClass(request);
	}

	// 查询规则定义
	@GetMapping("/")
	public Page<RuleClassResponse> queryRuleClass(@RequestParam(value = "code", required = false) String code,
																								@RequestParam(value = "description", required = false) String description,
																								@RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
																								@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
																								) {
		return service.queryRuleClass(code, description, pageNumber, pageSize);
	}

	// 查询指定规则ID的规则定义
	@GetMapping("/{ruleId}")
	public RuleClassResponse getRuleClass(@PathVariable("ruleId") Integer ruleId) {
		return service.getRuleClass(ruleId);
	}

	@PutMapping("/{ruleId}")
	public void updateRuleClass(@PathVariable("ruleId") Integer ruleId, @RequestBody RuleClassRequest request) {
		service.updateRuleClass(request);
	}

	@DeleteMapping("/{ruleId}")
	public void deleteRuleClass(@PathVariable("ruleId") Integer ruleId) {
		service.deleteRuleClass(ruleId);
	}

	// 添加版本
	@PostMapping("/{ruleId}/version")
	public Integer addVersion(@PathVariable("ruleId") Integer ruleId) {
		// 新增规则发布记录
		return service.addVersion(ruleId);
	}

	// 查看版本
	@GetMapping("/{ruleId}/version/{versionId}")
	public RuleClassVersionResponse getVersion(@PathVariable("ruleId") Integer ruleId, @PathVariable("versionId") Integer versionId) {
		return service.getVersion(versionId, ruleId);
	}

	// 根据分页参数查看版本列表，添加分页参数
	@GetMapping("/{ruleId}/version/page")
	public Page<RuleClassVersionResponse> getVersions(@PathVariable("ruleId") Integer ruleId,
																										@RequestParam(name = "pageNumber", value = "0") Integer pageNumber,
																										@RequestParam(name = "pageSize", value = "10") Integer pageSize) {
		return service.getVersions(ruleId, pageNumber, pageSize);
	}

	// 删除版本
	@DeleteMapping("/{ruleId}/version/{versionId}")
	public void deleteVersion(@PathVariable("ruleId") Integer ruleId, @PathVariable("versionId") Integer versionId) {
		service.deleteVersion(versionId, ruleId);
	}

	// 发布版本
	@PutMapping("/{ruleId}/version/{versionId}")
	public void updateVersion(@PathVariable("ruleId") Integer ruleId,
														@PathVariable("versionId") Integer versionId,
														@RequestParam(name = "status", defaultValue = "ONLINE") String status) {
		service.updateVersion(versionId, ruleId, status);
	}


}
