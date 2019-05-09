package com.songle.service;

import com.github.pagehelper.PageInfo;
import com.songle.model.Resources;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Map;

/**
 * songle
 */

public interface ResourcesService extends IService<Resources> {
    PageInfo<Resources> selectByPage(Resources resources, int start, int length);

    public List<Resources> queryAll();

    public List<Resources> loadUserResources(Map<String, Object> map);

    public List<Resources> queryResourcesListWithSelected(Integer rid);
}
