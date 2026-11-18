package io.github.bboy22dxdavid.sang_backEnd.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseConnectionTestRunner implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionTestRunner(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================================");
        System.out.println("🔍 TESTANDO CONEXÃO COM O BANCO DE DADOS SANG...");

        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            String produto = connection.getMetaData().getDatabaseProductName();
            String versao = connection.getMetaData().getDatabaseProductVersion();

            // Consulta quantas tabelas foram criadas no schema public pelo Flyway
            Integer totalTabelas = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'",
                    Integer.class
            );

            System.out.println("✅ CONEXÃO ESTABELECIDA COM SUCESSO!");
            System.out.println("📦 Banco: " + produto + " (" + versao + ")");
            System.out.println("🔗 URL: " + url);
            System.out.println("📊 Total de tabelas no schema 'public' (incluindo Flyway): " + totalTabelas);
        } catch (Exception e) {
            System.err.println("❌ FALHA AO CONECTAR NO BANCO DE DADOS: " + e.getMessage());
            throw e;
        }

        System.out.println("=================================================");
    }
}
