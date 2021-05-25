package com.yinlu.code.generator.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author dzhao1
 */
@Data
public class TableInfo {
  @TableField("TABLE_CATALOG")
  private String tableCatalog;
  @TableField("TABLE_SCHEMA")
  private String tableSchema;
  @TableField("TABLE_NAME")
  private String tableName;
  @TableField("TABLE_TYPE")
  private String tableType;


}
