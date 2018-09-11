package me.silloy.config.db;

import com.zaxxer.hikari.HikariDataSource;
import me.silloy.common.Constant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by zsw on 2018/7/16.
 */
@Configuration
@MapperScan(basePackages = "com.zj.shop.operation.mapper.bi", sqlSessionTemplateRef = Constant.DATASOURCE_BI + "SqlSessionTemplate")
public class BiDBSource extends AbstractDataSource {

    @Autowired
    private DBProperties properties;

    private final static String DATASOURCE = Constant.DATASOURCE_BI;


    @Bean(name = DATASOURCE)
    public DataSource biDataSource() {
        HikariDataSource source = properties.getBi();
        return dataSource(source);
    }

    @Bean(name = DATASOURCE + "SqlSessionFactory")
    public SqlSessionFactory biSqlSessionFactory(@Qualifier(DATASOURCE) DataSource dataSource) throws Exception {
        return sqlSessionFactory(dataSource);
    }

    @Bean(name = DATASOURCE + "SqlSessionTemplate")
    public SqlSessionTemplate biSqlSessionTemplate(@Qualifier(DATASOURCE + "SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return sqlSessionTemplate(sqlSessionFactory);
    }

}
