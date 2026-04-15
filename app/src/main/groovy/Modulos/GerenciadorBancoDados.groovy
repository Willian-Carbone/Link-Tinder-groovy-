package Modulos

import Enuns.Especialidades
import Enuns.Estados
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import java.sql.SQLException


class GerenciadorBancoDados {

    private String url = 'jdbc:postgresql://localhost:5432/linkEtinder'
    private String usuario = 'postgres'
    private String senha = '5550178wcb'
    private String driver = 'org.postgresql.Driver'
    private Sql sql


    GerenciadorBancoDados(Sql sql = null) throws SQLException {


        try {
            if (sql) {
                this.sql = sql
            } else {
                this.sql = Sql.newInstance(url, usuario, senha, driver)
            }

            this.sql.connection

        } catch (Exception ignored) {
            throw new SQLException("Não foi possivel se conectar ao Banco de dados")
        }


    }



















}