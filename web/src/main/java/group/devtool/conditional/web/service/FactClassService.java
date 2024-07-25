package group.devtool.conditional.web.service;

import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.FactClassEntity;
import group.devtool.conditional.web.entity.FactPropertyClassEntity;
import group.devtool.condition.web.repository.*;
import group.devtool.conditional.web.repository.*;
import group.devtool.conditional.web.response.FactPropertyClassResponse;
import group.devtool.conditional.web.entity.ArgumentClassEntity;
import group.devtool.conditional.web.entity.ReturnClassEntity;
import group.devtool.conditional.web.request.FactClassRequest;
import group.devtool.conditional.web.request.FactPropertyClassRequest;
import group.devtool.conditional.web.response.FactClassResponse;
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
public class FactClassService {

	@Autowired
	private FactClassRepository repository;

	@Autowired
	private FactPropertyClassRepository propertyRepository;

	@Autowired
	private RuleClassVersionRepository versionRepository;

	@Autowired
	private ArgumentClassRepository argumentsRepository;

	@Autowired
	private ReturnClassRepository returnRepository;

	/**
	 * 添加规则事实
	 *
	 * @param request   规则事实请求
	 * @param versionId 规则版本ID
	 * @param ruleId    规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addFactClass(FactClassRequest request, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		// 如果规则事实编码已存在，提示错误：规则事实编码已存在
		if (repository.findByCodeAndVersionIdAndRuleId(request.getCode(), versionId, ruleId) != null) {
			throw new ConditionalException("规则事实编码已存在");
		}

		long timestamp = Instant.now().toEpochMilli();
		FactClassEntity entity = FactClassMapper.MAPPER.toFactClassEntity(request);
		entity.setVersionId(versionId);
		entity.setRuleId(ruleId);
		entity.setCreateTime(timestamp);
		entity.setUpdateTime(timestamp);
		entity.setDeleted(ConditionalConstants.NO);
		repository.save(entity);
	}

	/**
	 * 删除规则事实
	 *
	 * @param factId 规则事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteFactClass(Integer factId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.OFFLINE, ConditionalStatus.DESIGN);

		FactClassEntity entity = doGetFactClass(factId, versionId, ruleId);

		doDependencyCheck(versionId, ruleId, entity.getCode());

		entity.setDeleted(ConditionalConstants.YES);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		repository.save(entity);
	}

	private void doDependencyCheck(Integer versionId, Integer ruleId, String code) {
		// 检查事实是否被参数依赖
		List<ArgumentClassEntity> arguments = argumentsRepository.findByRuleIdAndVersionIdAndTypeAndDeleted(ruleId,
						versionId,
						code,
						ConditionalConstants.NO
		);
		if (null != arguments && !arguments.isEmpty()) {
			throw new ConditionalException("规则事实被输入参数定义依赖，不能删除");
		}

		// 检查事实是否被返回结果依赖
		List<ReturnClassEntity> returns = returnRepository.findByRuleIdAndVersionIdAndTypeAndDeleted(ruleId,
						versionId,
						code,
						ConditionalConstants.NO
		);
		if (null != returns && !returns.isEmpty()) {
			throw new ConditionalException("规则事实被返回结果定义依赖，不能删除");
		}
	}

	/**
	 * 更新规则事实
	 *
	 * @param request 规则事实请求
	 * @param factId 规则事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateFactClass(FactClassRequest request, Integer factId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		FactClassEntity entity = doGetFactClass(factId, versionId, ruleId);
		if (!entity.getCode().equals(request.getCode())) {
			doDependencyCheck(versionId, ruleId, entity.getCode());
		}

		// 如果规则事实编码已存在，提示错误：规则事实编码已存在
		if (repository.findByCodeAndVersionIdAndRuleId(request.getCode(), versionId, ruleId) != null) {
			throw new ConditionalException("规则事实编码已存在");
		}

		entity.setCode(request.getCode());
		entity.setName(request.getName());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		repository.save(entity);

	}

	private FactClassEntity doGetFactClass(Integer factId, Integer versionId, Integer ruleId) {
		FactClassEntity entity = repository.findByIdAndVersionIdAndRuleId(factId, versionId, ruleId);
		if (null == entity) {
			throw new ConditionalException("规则事实不存在");
		}
		return entity;
	}

	/**
	 * 获取规则事实
	 *
	 * @param factId 规则事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 规则事实
	 */
	public FactClassResponse getFactClass(Integer factId, Integer versionId, Integer ruleId) {
		FactClassEntity entity = doGetFactClass(factId, versionId, ruleId);
		return FactClassMapper.MAPPER.toFactClassResponse(entity);
	}

	/**
	 * 获取规则事实列表
	 *
	 * @param code       事实编码
	 * @param name       事实名称
	 * @param pageNumber 分页页码
	 * @param pageSize 分页页面数量
	 * @param versionId  规则版本ID
	 * @param ruleId     规则ID
	 * @return 规则事实列表
	 */
	public Page<FactClassResponse> getFactClasses(String code, String name, Integer pageNumber, Integer pageSize,
																								Integer versionId, Integer ruleId) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<FactClassEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotEmpty(code)) {
				predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
			}
			if (StringUtils.isNotEmpty(name)) {
				predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
			}
			predicates.add(criteriaBuilder.equal(root.get("version_id"), versionId));
			predicates.add(criteriaBuilder.equal(root.get("rule_id"), ruleId));
			predicates.add(criteriaBuilder.equal(root.get("deleted"), ConditionalConstants.NO));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<FactClassEntity> result = repository.findAll(specification, pageable);
		return result.map(FactClassMapper.MAPPER::toFactClassResponse);

	}

	/**
	 * 添加规则事实属性
	 *
	 * @param request 规则事实属性请求
	 * @param factId 规则事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addFactPropertyClass(FactPropertyClassRequest request, Integer factId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		if (propertyRepository.findByCodeAndFactIdAndVersionIdAndRuleIdAndDeleted(request.getCode(),
						factId, versionId, ruleId,
						ConditionalConstants.NO) != null) {
			throw new ConditionalException("规则事实属性编码已存在");
		}

		FactPropertyClassEntity entity = FactPropertyClassMapper.MAPPER.toFactPropertyClassEntity(request);

		entity.setFactId(factId);
		entity.setVersionId(versionId);
		entity.setRuleId(ruleId);
		entity.setCreateTime(Instant.now().toEpochMilli());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.NO);
		propertyRepository.save(entity);
	}

	/**
	 * 删除规则事实属性
	 *
	 * @param factPropertyId 规则事实属性ID
	 * @param factId 规则事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteFactPropertyClass(Integer factPropertyId, Integer factId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.OFFLINE, ConditionalStatus.DESIGN);

		FactPropertyClassEntity entity = doGetFactPropertyClass(factPropertyId, factId, versionId, ruleId);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.YES);
		propertyRepository.save(entity);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateFactPropertyClass(FactPropertyClassRequest request, Integer factPropertyId, Integer factId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		FactPropertyClassEntity entity = doGetFactPropertyClass(factPropertyId, factId, versionId, ruleId);
		if (!request.getCode().equals(entity.getCode())) {
			throw new ConditionalException("规则事实属性被引用，不能删除");
		}

		entity.setName(request.getName());
		entity.setKeyType(request.getKeyType());
		entity.setType(request.getType());
		entity.setValueType(request.getValueType());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.NO);
		propertyRepository.save(entity);
	}

	/**
	 * 获取规则事实属性列表
	 *
	 * @param code 属性编码
	 * @param name 属性名称
	 * @param pageNumber 分页页码
	 * @param pageSize 分页页面数量
	 * @param factId 事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 规则事实属性列表
	 */
	public Page<FactPropertyClassResponse> getFactPropertyClasses(String code, String name,
																																Integer pageNumber, Integer pageSize,
																																Integer factId, Integer versionId, Integer ruleId) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<FactPropertyClassEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotEmpty(code)) {
				predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
			}
			if (StringUtils.isNotEmpty(name)) {
				predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
			}
			predicates.add(criteriaBuilder.equal(root.get("version_id"), versionId));
			predicates.add(criteriaBuilder.equal(root.get("rule_id"), ruleId));
			predicates.add(criteriaBuilder.equal(root.get("fact_id"), factId));
			predicates.add(criteriaBuilder.equal(root.get("deleted"), ConditionalConstants.NO));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<FactPropertyClassEntity> result = propertyRepository.findAll(specification, pageable);
		return result.map(FactPropertyClassMapper.MAPPER::toFactPropertyClassResponse);
	}

	/**
	 * 获取规则事实属性
	 *
	 * @param factPropertyId 规则事实属性ID
	 * @param factId 规则事实ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 规则事实属性
	 */
	public FactPropertyClassResponse getFactPropertyClass(Integer factPropertyId, Integer factId, Integer versionId, Integer ruleId) {
		FactPropertyClassEntity entity = doGetFactPropertyClass(factPropertyId, factId, versionId, ruleId);
		return FactPropertyClassMapper.MAPPER.toFactPropertyClassResponse(entity);
	}

	private FactPropertyClassEntity doGetFactPropertyClass(Integer factPropertyId, Integer factId,
																												 Integer versionId, Integer ruleId) {
		FactPropertyClassEntity entity = propertyRepository.findByIdAndFactIdAndVersionIdAndRuleIdAndDeleted(factPropertyId, factId,
						versionId, ruleId, ConditionalConstants.NO);
		if (null == entity) {
			throw new ConditionalException("规则事实属性不存在");
		}
		return entity;
	}

	@Mapper
	private interface FactClassMapper {

		FactClassMapper MAPPER = Mappers.getMapper(FactClassMapper.class);

		FactClassEntity toFactClassEntity(FactClassRequest request);

		FactClassResponse toFactClassResponse(FactClassEntity entity);
	}

	@Mapper
	private interface FactPropertyClassMapper {

		FactPropertyClassMapper MAPPER = Mappers.getMapper(FactPropertyClassMapper.class);


		FactPropertyClassResponse toFactPropertyClassResponse(FactPropertyClassEntity entity);

		FactPropertyClassEntity toFactPropertyClassEntity(FactPropertyClassRequest request);
	}
}
