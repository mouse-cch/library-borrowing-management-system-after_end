package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.RolePermissionDTO;
import com.example.afterend.entity.OperationLog;
import com.example.afterend.entity.Role;
import java.util.List;

public interface SysService {
    List<Role> getRoles();
    void updateRolePermissions(Integer roleId, RolePermissionDTO dto);
    Page<OperationLog> operationLogs(Integer pageNum, Integer pageSize, String module, String operator, String startTime, String endTime);
}
