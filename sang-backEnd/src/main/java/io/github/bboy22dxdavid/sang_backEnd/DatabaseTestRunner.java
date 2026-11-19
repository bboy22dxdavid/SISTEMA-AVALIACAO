package io.github.bboy22dxdavid.sang_backEnd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Component
public class DatabaseTestRunner  implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseTestRunner(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================================");
        System.out.println("🔍 VALIDANDO CONEXÃO COM POSTGRESQL (SANG)...");

        try (Connection conn = dataSource.getConnection()) {
            List<String> tabelas = jdbcTemplate.queryForList(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name",
                    String.class
            );

            System.out.println("✅ CONECTADO EM: " + conn.getMetaData().getURL());
            System.out.println("📋 TABELAS ENCONTRADAS (" + tabelas.size() + "): " + tabelas);
        }

        System.out.println("=================================================");
    }
}
