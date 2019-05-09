package com.songle.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author songle
 * @create 2019-05-09 上午 11:20
 */
public interface MyMapper<T> extends Mapper<T>,MySqlMapper<T> {


}
