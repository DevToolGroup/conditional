package group.devtool.conditional.web.service;

import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.RuleClassEntity;
import group.devtool.conditional.web.entity.RuleClassVersionEntity;
import group.devtool.conditional.web.repository.RuleClassVersionRepository;
import group.devtool.conditional.web.request.RuleClassRequest;
import group.devtool.conditional.web.response.RuleClassVersionResponse;
import group.devtool.conditional.web.repository.RuleClassRepository;
import group.devtool.condition.web.request.*;
import group.devtool.conditional.web.response.RuleClassResponse;
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
import java.util.Objects;

@Service
public class RuleClassService {

	@Autowired
	private RuleClassRepository repository;

	@Autowired
	private RuleClassVersionRepository versionRepository;

	/**
	 * 新增规则定义
	 *
	 * @param request 规则定义请求
	 * @return 规则定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addRuleClass(RuleClassRequest request) {
		RuleClassEntity rule = repository.findByCodeAndDeleted(request.getCode(), ConditionalConstants.NO);
		if (rule != null) {
			throw new ConditionalException("规则编码已存在");
		}

		RuleClassEntity entity = RuleClassMapper.MAPPER.toRuleClassEntity(request);
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setDeleted("N");
		entity = repository.save(entity);
		return entity.getId();
	}

	/**
	 * 根据查询请求获取规则类信息。
	 *
	 * @return 规则列表
	 */
	public Page<RuleClassResponse> queryRuleClass(String code, String description, Integer pageNumber, Integer pageSize) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<RuleClassEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotEmpty(code)) {
				predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
			}
			if (StringUtils.isNotEmpty(description)) {
				predicates.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		return repository.findAll(specification, pageable).map(RuleClassMapper.MAPPER::toRuleClassResponse);
	}

	/**
	 * 根据规则ID获取规则类信息。
	 * @param ruleId 规则ID
	 * @return 规则详情
	 */
	public RuleClassResponse getRuleClass(Integer ruleId) {
		return RuleClassMapper.MAPPER.toRuleClassResponse(doGetRuleClass(ruleId));
	}

	private RuleClassEntity doGetRuleClass(Integer ruleId) {
		RuleClassEntity entity = repository.findById(ruleId).orElse(null);
		if (entity == null) {
			throw new ConditionalException("规则不存在");
		}
		return entity;
	}

	/**
	 * 更新规则详情
	 *
	 * @param request 规则请求
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRuleClass(RuleClassRequest request) {
		RuleClassEntity result = doGetRuleClass(request.getId());

		RuleClassEntity entity = repository.findByCodeAndDeleted(request.getCode(), ConditionalConstants.NO);
		if (null != entity && !Objects.equals(entity.getId(), result.getId())) {
			throw new ConditionalException("规则编码已存在");
		}

		RuleClassEntity updatedEntity = RuleClassMapper.MAPPER.toRuleClassEntity(request);
		updatedEntity.setUpdateTime(System.currentTimeMillis());
		repository.save(updatedEntity);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteRuleClass(Integer ruleId) {
		RuleClassEntity entity = doGetRuleClass(ruleId);
		List<RuleClassVersionEntity> versions = versionRepository.findByRuleIdAndDeleted(ruleId, ConditionalConstants.NO);
		if (null != versions && !versions.isEmpty()) {
			throw new ConditionalException("存在规则版本，需删除规则版本");
		}
		Long currentEpoch = Instant.now().toEpochMilli();
		entity.setDeleted(ConditionalConstants.YES);
		entity.setUpdateTime(currentEpoch);
		repository.save(entity);
	}

	/**
	 * 新增版本发布记录
	 *
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addVersion(Integer ruleId) {
		RuleClassEntity ruleClass = doGetRuleClass(ruleId);

		RuleClassVersionEntity entity = new RuleClassVersionEntity();
		entity.setRuleId(ruleClass.getId());
		entity.setStatus(ConditionalStatus.DESIGN.name());
		versionRepository.save(entity);

		return entity.getId();
	}

	/**
	 * 获取版本记录
	 *
	 * @param versionId 版本ID
	 * @param ruleId  规则ID
	 * @return 版本记录
	 */
	public RuleClassVersionResponse getVersion(Integer versionId, Integer ruleId) {
		RuleClassVersionEntity entity = versionRepository.findByIdAndRuleIdAndDeleted(versionId, ruleId, ConditionalConstants.NO);
		if (null == entity) {
			throw new ConditionalException("版本不存在");
		}
		return RuleClassMapper.MAPPER.toRuleClassVersionResponse(entity);
	}

	/**
	 * 获取版本列表
	 *
	 * @param ruleId 规则ID
	 * @param pageNumber 分页页码
	 * @param pageSize 分页页大小
	 * @return 版本列表
	 */
	public Page<RuleClassVersionResponse> getVersions(Integer ruleId, Integer pageNumber, Integer pageSize) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<RuleClassVersionEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(root.get("deleted"), ConditionalConstants.NO));
			predicates.add(criteriaBuilder.equal(root.get("ruleId"), ruleId));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<RuleClassVersionEntity> result = versionRepository.findAll(specification, pageable);
		return result.map(RuleClassMapper.MAPPER::toRuleClassVersionResponse);
	}

	/**
	 * 更新版本记录
	 *
	 * @param versionId 版本ID
	 * @param ruleId    规则ID
	 * @param status    状态
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateVersion(Integer versionId, Integer ruleId, String status) {
		if (Objects.equals(status, ConditionalStatus.ONLINE.name())) {
			// 更改部署的版本状态为0
			changeDeployedVersion(ruleId);
		}
		RuleClassVersionEntity entity = versionRepository.findByIdAndRuleIdAndDeleted(versionId, ruleId, ConditionalConstants.NO);
		entity.setStatus(status);
		versionRepository.save(entity);
	}

	/**
	 * 更改部署的版本状态为0，其中并发操作情况下，可能抛出状态异常
	 *
	 * @param ruleId 规则ID
	 */
	private void changeDeployedVersion(Integer ruleId) {
		// 查询出部署的版本
		RuleClassVersionEntity deployedVersion = versionRepository.findByRuleIdAndStatus(ruleId, ConditionalStatus.ONLINE.name());
		if (deployedVersion == null) {
			return;
		}
		int rows = versionRepository.changeDeployedVersion(System.currentTimeMillis(), deployedVersion.getLock(), deployedVersion.getId(), ruleId);
		if (rows == 0) {
			throw new ConditionalException("规则版本状态更新失败，规则版本不存在或状态已改变");
		}
	}

	// 删除版本
	@Transactional(rollbackFor = Exception.class)
	public void deleteVersion(Integer versionId, Integer ruleId) {
		RuleClassVersionEntity entity = versionRepository.findByIdAndRuleIdAndDeleted(versionId, ruleId, ConditionalConstants.NO);
		if (entity == null) {
			throw new ConditionalException("规则版本不存在");
		}
		if (entity.getStatus().equals(ConditionalStatus.ONLINE.name())) {
			throw new ConditionalException("仅已下线，设计中的版本支持删除");
		}
		int rows = versionRepository.deleteVersion(entity.getLock(), System.currentTimeMillis(), versionId, ruleId);
		if (rows == 0) {
			throw new ConditionalException("规则版本删除失败，规则版本不存在或状态已改变");
		}
		// 删除事实定义
		// 删除参数定义
		// 删除返回值定义
		// 删除变量定义
		// 删除条件定义
	}


	@Mapper
	public interface RuleClassMapper {

		RuleClassMapper MAPPER = Mappers.getMapper(RuleClassMapper.class);

		RuleClassResponse toRuleClassResponse(RuleClassEntity ruleClass);

		RuleClassEntity toRuleClassEntity(RuleClassRequest request);

		RuleClassVersionResponse toRuleClassVersionResponse(RuleClassVersionEntity entity);

	}
}
