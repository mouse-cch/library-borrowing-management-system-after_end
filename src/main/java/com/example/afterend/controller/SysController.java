package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.dto.RolePermissionDTO;
import com.example.afterend.entity.OperationLog;
import com.example.afterend.entity.Role;
import com.example.afterend.service.SysService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统管理模块")
@RestController
@RequestMapping("/api/sys")
@RequiredArgsConstructor
public class SysController {

    private final SysService sysService;

    @Operation(summary ="获取角色列表")
    @GetMapping("/roles")
    public R<List<Role>> getRoles() {
        return R.ok(sysService.getRoles());
    }

    @Operation(summary ="修改角色权限")
    @PutMapping("/roles/{roleId}")
    public R<?> updateRolePermissions(@PathVariable Integer roleId, @RequestBody RolePermissionDTO dto) {
        sysService.updateRolePermissions(roleId, dto);
        return R.ok("权限修改成功", null);
    }

    @Operation(summary ="操作日志查询")
    @GetMapping("/operation-logs")
    public R<PageResult<OperationLog>> operationLogs(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                                      @RequestParam(required = false) String module,
                                                      @RequestParam(required = false) String operator,
                                                      @RequestParam(required = false) String startTime,
                                                      @RequestParam(required = false) String endTime) {
        Page<OperationLog> page = sysService.operationLogs(pageNum, pageSize, module, operator, startTime, endTime);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }
}
