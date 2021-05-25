package com.yinlu.code.generator.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yinlu.code.generator.mapper")
public class MybatisPlusConfig {

}
