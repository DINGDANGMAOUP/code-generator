package com.yinlu.code.generator.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
* @author dzhao1
*/
@Data
public class TableInfoBack {
@TableField("${tableCatalog}")
private String ${tableCatalog};
@TableField("${tableSchema}")
private String ${tableSchema};
@TableField("${tableName}")
private String ${tableName};
@TableField("${tableType}")
private String ${tableType};
}
