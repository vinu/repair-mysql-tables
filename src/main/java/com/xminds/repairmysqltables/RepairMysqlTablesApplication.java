package com.xminds.repairmysqltables;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@Slf4j
public class RepairMysqlTablesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepairMysqlTablesApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            List<String> tables = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));

            log.info("Found {} tables", tables.size());

            for (String table : tables) {
                log.info("Optimizing: {}", table);
                jdbcTemplate.execute(String.format("REPAIR TABLE %s", table));
                jdbcTemplate.execute(String.format("ANALYZE TABLE %s", table));
                jdbcTemplate.execute(String.format("OPTIMIZE TABLE %s", table));
                log.info("Optimizing: {} done", table);
            }

            log.info("Reindex tables done");
        };
    }
}
