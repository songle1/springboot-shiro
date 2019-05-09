package com.songle.mapper;


import com.songle.model.Role;
import com.songle.util.MyMapper;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {
    public List<Role> queryRoleListWithSelected(Integer id);
}