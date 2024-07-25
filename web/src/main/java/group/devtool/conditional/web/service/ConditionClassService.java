package group.devtool.conditional.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.ConditionClassEntity;
import group.devtool.conditional.web.repository.ConditionClassRepository;
import group.devtool.conditional.web.repository.RuleClassVersionRepository;
import group.devtool.conditional.web.request.ConditionClassRequest;
import group.devtool.conditional.web.response.ConditionClassResponse;
import jakarta.persistence.criteria.Predicate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
public class ConditionClassService {

	@Autowired
	private ConditionClassRepository repository;

	@Autowired
	private RuleClassVersionRepository versionRepository;
	;

	/**
	 * 添加条件
	 *
	 * @param request 添加条件请求
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addConditionClass(ConditionClassRequest request, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		ConditionClassEntity entity = ConditionClassMapper.MAPPER.toConditionClassEntity(request);
		entity.setVersionId(versionId);
		entity.setRuleId(ruleId);
		entity.setCreateTime(Instant.now().toEpochMilli());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.NO);
		repository.save(entity);
	}

	/**
	 * 删除条件
	 *
	 * @param conditionId 规则条件ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteConditionClass(Integer conditionId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.OFFLINE, ConditionalStatus.DESIGN);

		ConditionClassEntity conditionEntity = doGetConditionClass(conditionId, versionId, ruleId);
		conditionEntity.setDeleted(ConditionalConstants.YES);
		conditionEntity.setUpdateTime(Instant.now().toEpochMilli());
		repository.save(conditionEntity);
	}

	/**
	 * 更新条件
	 *
	 * @param request 更新条件请求
	 * @param conditionId 规则条件ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateConditionClass(ConditionClassRequest request, Integer conditionId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		ConditionClassEntity entity = doGetConditionClass(conditionId, versionId, ruleId);

		ConditionClassEntity hadOrderEntity = repository.findByVersionIdAndRuleIdAndOrderAndDeleted(versionId, ruleId,
						request.getOrder(), ConditionalConstants.NO);

		if (hadOrderEntity != null && !Objects.equals(hadOrderEntity.getId(), entity.getId())) {
			throw new ConditionalException("条件顺序重复");
		}

		ConditionClassEntity update = ConditionClassMapper.MAPPER.toConditionClassEntity(request);
		entity.setOrder(request.getOrder());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setCondition(update.getCondition());
		entity.setFunctions(update.getFunctions());
		repository.save(entity);
	}

	private ConditionClassEntity doGetConditionClass(Integer conditionId, Integer versionId, Integer ruleId) {
		ConditionClassEntity entity = repository.findByIdAndVersionIdAndRuleIdAndDeleted(conditionId, versionId, ruleId,
						ConditionalConstants.NO);
		if (null == entity) {
			throw new ConditionalException("条件不存在");
		}
		return entity;
	}

	/**
	 * 获取条件详情
	 *
	 * @param ruleId 规则ID
	 * @param versionId 规则版本ID
	 * @param conditionId 规则ID
	 * @return 条件详情
	 */
	public ConditionClassResponse getConditionClass(Integer conditionId, Integer versionId, Integer ruleId) {
		ConditionClassEntity entity = doGetConditionClass(conditionId, versionId, ruleId);
		return ConditionClassMapper.MAPPER.toConditionClassResponse(entity);
	}

	/**
	 * 获取条件列表
	 *
	 * @param pageNumber 页码
	 * @param pageSize 每页数量
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 条件列表
	 */
	public Page<ConditionClassResponse> getConditionClasses(Integer pageNumber, Integer pageSize, Integer versionId, Integer ruleId) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<ConditionClassEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(root.get("version_id"), versionId));
			predicates.add(criteriaBuilder.equal(root.get("rule_id"), ruleId));
			predicates.add(criteriaBuilder.equal(root.get("deleted"), ConditionalConstants.NO));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<ConditionClassEntity> result = repository.findAll(specification, pageable);
		return result.map(ConditionClassMapper.MAPPER::toConditionClassResponse);
	}

	@Mapper
	public interface ConditionClassMapper {
		ConditionClassMapper MAPPER = Mappers.getMapper(ConditionClassMapper.class);

		@Mappings(
						value = {
										@Mapping(target = "functions", expression = "java(ConditionSerializer.serialize(request.getFunctions()))"),
										@Mapping(target = "condition", expression = "java(ConditionSerializer.serialize(request.getCondition()))"),
						}
		)
		ConditionClassEntity toConditionClassEntity(ConditionClassRequest request);

		@Mappings(
						value = {
										@Mapping(target = "functions", expression = "java(ConditionSerializer.deserialize(request.getFunctions(), new TypeReference<List<ConditionExpressionRequest>>(){}"),
										@Mapping(target = "condition", expression = "java(ConditionSerializer.deserialize(request.getCondition(), new TypeReference<ConditionExpressionRequest>(){}))"),
						}
		)
		ConditionClassResponse toConditionClassResponse(ConditionClassEntity entity);
	}

	public static class ConditionSerializer {

		public static final ObjectMapper MAPPER = new ObjectMapper();

		public static <T> String serialize(T object) throws JsonProcessingException {
			// jackson 序列化对象为字符串
			return MAPPER.writeValueAsString(object);
		}

		public static <T> T deserialize(String json, TypeReference<T> typeReference) throws JsonProcessingException {
			return MAPPER.readValue(json, new TypeReference<T>() {
			});
		}

	}
}
