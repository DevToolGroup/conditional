package group.devtool.conditional.web.service;

import group.devtool.conditional.web.ConditionalConstants;
import group.devtool.conditional.web.ConditionalException;
import group.devtool.conditional.web.ConditionalStatus;
import group.devtool.conditional.web.entity.ReturnClassEntity;
import group.devtool.conditional.web.repository.ReturnClassRepository;
import group.devtool.conditional.web.repository.RuleClassVersionRepository;
import group.devtool.conditional.web.request.ReturnClassRequest;
import group.devtool.conditional.web.response.ReturnClassResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ReturnClassService {

	@Autowired
	private ReturnClassRepository returnClassRepository;

	@Autowired
	private RuleClassVersionRepository versionRepository;

	/**
	 * 新建返回结果定义
	 *
	 * @param request   返回结果定义
	 * @param versionId 规则版本ID
	 * @param ruleId    规则ID
	 * @return 返回结果定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addReturnClass(ReturnClassRequest request, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		ReturnClassEntity returnEntity = returnClassRepository.findByVersionIdAndRuleIdAndDeleted(versionId, ruleId, ConditionalConstants.NO);
		if (returnEntity != null) {
			throw new ConditionalException("返回结果定义已存在");
		}
		ReturnClassEntity entity = ReturnClassMapper.MAPPER.toReturnClassEntity(request);
		entity.setRuleId(ruleId);
		entity.setVersionId(versionId);
		entity.setCreateTime(Instant.now().toEpochMilli());
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setDeleted(ConditionalConstants.NO);
		returnClassRepository.save(entity);
		return entity.getId();
	}

	/**
	 * 删除返回结果定义
	 * @param ruleId 规则ID
	 * @param versionId 规则版本ID
	 * @param returnId 返回结果定义ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteReturnClass(Integer returnId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.OFFLINE, ConditionalStatus.DESIGN);
		ReturnClassEntity entity = doGetReturnClassEntity(returnId, versionId, ruleId);
		entity.setDeleted(ConditionalConstants.YES);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		returnClassRepository.save(entity);
	}

	/**
	 * 更新返回结果定义
	 *
	 * @param request   返回结果定义
	 * @param returnId  返回结果ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateReturnClass(ReturnClassRequest request, Integer returnId, Integer versionId, Integer ruleId) {
		versionRepository.doLockVersion(versionId, ruleId, ConditionalStatus.DESIGN);

		ReturnClassEntity entity = doGetReturnClassEntity(returnId, versionId, ruleId);
		entity.setUpdateTime(Instant.now().toEpochMilli());
		entity.setCode(request.getCode());
		entity.setType(request.getType());
		returnClassRepository.save(entity);
	}

	/**
	 * 获取返回结果定义
	 *
	 * @param returnId 返回结果定义ID
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 返回结果定义
	 */
	public ReturnClassResponse getReturnClass(Integer returnId, Integer versionId, Integer ruleId) {
		ReturnClassEntity entity = doGetReturnClassEntity(returnId, versionId, ruleId);
		return ReturnClassMapper.MAPPER.toReturnClassResponse(entity);
	}

	private ReturnClassEntity doGetReturnClassEntity(Integer returnId, Integer versionId, Integer ruleId) {
		ReturnClassEntity entity = returnClassRepository.findByIdAndVersionIdAndRuleIdAndDeleted(returnId, versionId, ruleId, ConditionalConstants.NO);
		if (null == entity) {
			throw new ConditionalException("返回结果定义不存在");
		}
		return entity;
	}

	/**
	 * 获取返回结果定义列表
	 *
	 * @param versionId 规则版本ID
	 * @param ruleId 规则ID
	 * @return 返回结果定义列表
	 */
	public ReturnClassResponse getReturnClasses(Integer versionId, Integer ruleId) {
		ReturnClassEntity entity = returnClassRepository.findByVersionIdAndRuleIdAndDeleted(versionId, ruleId, ConditionalConstants.NO);
		return ReturnClassMapper.MAPPER.toReturnClassResponse(entity);
	}

	@Mapper
	public interface ReturnClassMapper {
		ReturnClassMapper MAPPER = Mappers.getMapper(ReturnClassMapper.class);

		ReturnClassEntity toReturnClassEntity(ReturnClassRequest request);

		ReturnClassResponse toReturnClassResponse(ReturnClassEntity entity);
	}
}
