package group.devtool.conditional.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.VariableClassEntity;
import group.devtool.conditional.web.repository.RuleClassVersionRepository;
import group.devtool.conditional.web.repository.VariableClassRepository;
import group.devtool.conditional.web.request.VariableClassRequest;
import group.devtool.conditional.web.response.VariableClassResponse;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
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

@Service
public class VariableClassService {

	@Autowired
	private VariableClassRepository variableClassRepository;

	@Autowired
	private RuleClassVersionRepository versionRepository;

	/**
	 * 添加变量定义
	 *
	 * @param request 添加变量定义请求
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addVariableClass(VariableClassRequest request, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		VariableClassEntity entity = variableClassRepository.findByCodeAndVersionIdAndRuleIdAndDeleted(request.getCode(),
						versionId, ruleId);
		if (entity != null) {
			throw new ConditionalException("规则变量编码已存在");
		}

		entity = VariableClassMapper.MAPPER.toEntity(request);

		entity.setVersionId(versionId);
		entity.setRuleId(ruleId);
		entity.setCreateTime(Instant.now().toEpochMilli());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.NO);

		variableClassRepository.save(entity);
	}

	/**
	 * 删除变量定义
	 *
	 * @param ruleId     规则ID
	 * @param versionId  规则版本ID
	 * @param variableId 变量定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteVariableClass(Integer variableId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.OFFLINE, ConditionalStatus.DESIGN);

		VariableClassEntity entity = doGetVariableClass(variableId, versionId, ruleId);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.YES);
		variableClassRepository.save(entity);
	}

	private VariableClassEntity doGetVariableClass(Integer variableId, Integer versionId, Integer ruleId) {
		VariableClassEntity entity = variableClassRepository.findByIdAndVersionIdAndRuleIdAndDeleted(variableId, versionId, ruleId, ConditionalConstants.NO);
		if (entity == null) {
			throw new ConditionalException("变量定义不存在");
		}
		return entity;
	}

	/**
	 * 更新变量定义
	 *
	 * @param request 更新变量定义请求
	 * @param ruleId  规则ID
	 * @param variableId 变量定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateVariableClass(VariableClassRequest request, Integer variableId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);
		VariableClassEntity entity = doGetVariableClass(variableId, versionId, ruleId);
		VariableClassEntity hadCodeEntity = variableClassRepository.findByCodeAndVersionIdAndRuleIdAndDeleted(request.getCode(),
						versionId, ruleId);
		if (hadCodeEntity != null && !hadCodeEntity.getId().equals(entity.getId())) {
			throw new ConditionalException("规则变量编码已存在");
		}
		VariableClassEntity hadOrderEntity = variableClassRepository.findByOrderAndVersionIdAndRuleIdAndDeleted(request.getOrder(),
						versionId, ruleId);
		if (hadOrderEntity != null && !hadOrderEntity.getId().equals(entity.getId())) {
			throw new ConditionalException("规则变量顺序已存在");
		}

		VariableClassEntity requestEntity = VariableClassMapper.MAPPER.toEntity(request);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setOrder(requestEntity.getOrder());
		entity.setExpression(requestEntity.getExpression());
		variableClassRepository.save(entity);
	}

	/**
	 * 获取变量定义
	 * @param variableId 变量定义ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 变量定义详情
	 */
	public VariableClassResponse getVariableClass(Integer variableId, Integer versionId, Integer ruleId) {
		VariableClassEntity entity = doGetVariableClass(variableId, versionId, ruleId);
		return VariableClassMapper.MAPPER.toResponse(entity);
	}

	/**
	 * 获取变量定义列表
	 *
	 * @param code 变量定义编码
	 * @param pageNumber 分页页码
	 * @param pageSize 分页大小
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 变量定义列表
	 */
	public Page<VariableClassResponse> getVariableClasses(String code, Integer pageNumber, Integer pageSize, Integer versionId, Integer ruleId) {
		Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
		Specification<VariableClassEntity> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(code)) {
				predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
			}
			predicates.add(criteriaBuilder.equal(root.get("version_id"), versionId));
			predicates.add(criteriaBuilder.equal(root.get("rule_id"), ruleId));
			predicates.add(criteriaBuilder.equal(root.get("deleted"), ConditionalConstants.NO));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<VariableClassEntity> result = variableClassRepository.findAll(specification, pageable);
		return result.map(VariableClassMapper.MAPPER::toResponse);

	}

	@Mapper
	public interface VariableClassMapper {

		VariableClassMapper MAPPER = Mappers.getMapper(VariableClassMapper.class);

		@Mappings(
						value = {
										@Mapping(target = "expression", expression = "java(VariableClassSerializer.serialize(request.getExpression(), new TypeReference<ExpressionRequest>(){}))"),
						}
		)
		VariableClassEntity toEntity(VariableClassRequest request);

		@Mappings(
						value = {
										@Mapping(target = "expression", expression = "java(VariableClassSerializer.deserialize(request.getExpression()))"),
						}
		)
		VariableClassResponse toResponse(VariableClassEntity entity);
	}

	public static class VariableClassSerializer {
		public static final ObjectMapper MAPPER = new ObjectMapper();

		public static <T> T deserialize(String json, TypeReference<T> typeReference) throws JsonProcessingException {
			return MAPPER.readValue(json, typeReference);
		}

		public static <T> String serialize(T object) throws JsonProcessingException {
			return MAPPER.writeValueAsString(object);
		}

	}
}
