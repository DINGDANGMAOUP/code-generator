package com.yinlu.code.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yinlu.code.generator.mapper.TableInfoMapper;
import com.yinlu.code.generator.model.TableInfo;
import com.yinlu.code.generator.service.TableInfoService;
import org.springframework.stereotype.Service;

/**
 * @author dzhao1
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper,TableInfo> implements TableInfoService {

}
