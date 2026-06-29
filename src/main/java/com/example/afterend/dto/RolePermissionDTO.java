package com.example.afterend.dto;

import lombok.Data;
import java.util.List;

@Data
public class RolePermissionDTO {
    private List<String> permissions;
}
