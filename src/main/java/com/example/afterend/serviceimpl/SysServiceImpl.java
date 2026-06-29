package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.RolePermissionDTO;
import com.example.afterend.entity.OperationLog;
import com.example.afterend.entity.Role;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.OperationLogMapper;
import com.example.afterend.mapper.RoleMapper;
import com.example.afterend.service.SysService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysServiceImpl implements SysService {

    private final RoleMapper roleMapper;
    private final OperationLogMapper operationLogMapper;

    @Override
    public List<Role> getRoles() {
        return roleMapper.selectList(null);
    }

    @Override
    public void updateRolePermissions(Integer roleId, RolePermissionDTO dto) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) throw new BusinessException("角色不存在");
        role.setPermissions(JSON.toJSONString(dto.getPermissions()));
        roleMapper.updateById(role);
    }

    @Override
    public Page<OperationLog> operationLogs(Integer pageNum, Integer pageSize, String module, String operator, String startTime, String endTime) {
        LambdaQueryWrapper<OperationLog> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(module)) qw.eq(OperationLog::getModule, module);
        if (StringUtils.hasText(operator)) qw.like(OperationLog::getOperatorName, operator);
        if (StringUtils.hasText(startTime)) qw.ge(OperationLog::getCreateTime, startTime);
        if (StringUtils.hasText(endTime)) qw.le(OperationLog::getCreateTime, endTime);
        qw.orderByDesc(OperationLog::getCreateTime);
        return operationLogMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }
}
