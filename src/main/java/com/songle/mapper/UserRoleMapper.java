package com.songle.mapper;



import com.songle.model.UserRole;
import com.songle.util.MyMapper;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {
    public List<Integer> findUserIdByRoleId(Integer roleId);
}