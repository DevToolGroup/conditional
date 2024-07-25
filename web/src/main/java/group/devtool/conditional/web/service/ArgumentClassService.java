package group.devtool.conditional.web.service;

import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.ArgumentClassEntity;
import group.devtool.conditional.web.repository.ArgumentClassRepository;
import group.devtool.conditional.web.repository.RuleClassVersionRepository;
import group.devtool.conditional.web.response.ArgumentClassResponse;
import group.devtool.conditional.web.request.ArgumentClassRequest;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArgumentClassService {

	@Autowired
	private ArgumentClassRepository repository;

	@Autowired
	private RuleClassVersionRepository versionRepository;

	/**
	 * 添加参数定义
	 *
	 * @param request   参数定义
	 * @param versionId 版本ID
	 * @param ruleId    规则ID
	 * @return 参数定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addArgumentClass(ArgumentClassRequest request, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		ArgumentClassEntity entity = repository.findByRuleIdAndVersionIdAndCode(ruleId, versionId, request.getCode());
		if (entity != null) {
			throw new ConditionalException("规则参数编码已存在。编码：" + request.getCode());
		}
		entity = ArgumentClassMapper.MAPPER.toArgumentClassEntity(request);
		entity.setRuleId(ruleId);
		entity.setVersionId(versionId);
		Long timestamp = Instant.now().toEpochMilli();
		entity.setCreateTime(timestamp);
		entity.setUpdateTime(timestamp);
		entity.setDeleted(ConditionalConstants.NO);
		repository.save(entity);
		return entity.getId();
	}

	/**
	 * 查看参数定义
	 * @param ruleId 规则ID
	 * @param versionId 版本ID
	 * @param argumentId 参数定义ID
	 * @return 参数定义
	 */
	public ArgumentClassResponse getArgumentClass(Integer ruleId, Integer versionId, Integer argumentId) {
		ArgumentClassEntity entity = doGetArgumentClassEntity(ruleId, versionId, argumentId);
		return ArgumentClassMapper.MAPPER.toArgumentClassResponse(entity);
	}

	private ArgumentClassEntity doGetArgumentClassEntity(Integer ruleId, Integer versionId, Integer argumentId) {
		ArgumentClassEntity entity = repository.findByIdAndRuleIdAndVersionId(argumentId, ruleId, versionId);
		if (argumentId == null) {
			throw new ConditionalException("规则参数定义不存在");
		}
		return entity;
	}

	/**
	 * 删除参数定义
	 * @param ruleId 规则ID
	 * @param argumentId 参数定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteArgumentClass(Integer ruleId, Integer versionId, Integer argumentId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.OFFLINE, ConditionalStatus.DESIGN);

		ArgumentClassEntity entity = repository.findByIdAndRuleIdAndVersionId(argumentId, ruleId, versionId);
		entity.setDeleted(ConditionalConstants.YES);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		repository.save(entity);
	}

	// 修改删除定义
	@Transactional(rollbackFor = Exception.class)
	public void updateArgumentClass(ArgumentClassRequest request, Integer argumentId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		ArgumentClassEntity entity = repository.findByRuleIdAndVersionIdAndCode(ruleId, versionId, request.getCode());
		if (entity != null && !entity.getId().equals(argumentId)) {
			throw new ConditionalException("规则参数编码已存在。编码：" + request.getCode());
		}

		entity = doGetArgumentClassEntity(ruleId, versionId, argumentId);

		entity.setCode(request.getCode());
		entity.setType(request.getType());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		repository.save(entity);
	}

	/**
	 * 查询参数定义列表
	 *
	 * @param code 参数编码
	 * @param pageNumber 页码
	 * @param pageSize 每页数量
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 参数定义列表
	 */
	public Page<ArgumentClassResponse> getArgumentClasses(String code,
																												Integer pageNumber, Integer pageSize,
																												Integer versionId, Integer ruleId) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<ArgumentClassEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotEmpty(code)) {
				predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
			}
			predicates.add(criteriaBuilder.equal(root.get("version_id"), versionId));
			predicates.add(criteriaBuilder.equal(root.get("rule_id"), ruleId));
			predicates.add(criteriaBuilder.equal(root.get("deleted"), ConditionalConstants.NO));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<ArgumentClassEntity> result = repository.findAll(specification, pageable);
		return result.map(ArgumentClassMapper.MAPPER::toArgumentClassResponse);
	}

	@Mapper
	private interface ArgumentClassMapper {

		ArgumentClassMapper MAPPER = Mappers.getMapper(ArgumentClassMapper.class);

		ArgumentClassResponse toArgumentClassResponse(ArgumentClassEntity entity);

		ArgumentClassEntity toArgumentClassEntity(ArgumentClassRequest request);

	}
}
