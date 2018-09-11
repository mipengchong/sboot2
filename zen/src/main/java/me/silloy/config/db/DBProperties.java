package me.silloy.config.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 实际数据源配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "hikari")
public class DBProperties {

    private HikariDataSource order;
    private HikariDataSource returnOrder;
    private HikariDataSource goods;
    private HikariDataSource bi;
    private HikariDataSource member;
    private HikariDataSource user;
    private HikariDataSource distribution;
}