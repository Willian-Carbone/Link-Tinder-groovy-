package GerenciadoresDeBanco.Conectores

import groovy.sql.Sql

class ConectorBancoBasico implements CriadorConexao {
    @Override

   Sql criarconexao() {

        try {

            String url = 'jdbc:postgresql://localhost:5432/linkEtinder'
            String usuario = 'postgres'
            String senha = '5550178wcb'
            String driver = 'org.postgresql.Driver'

            return Sql.newInstance(url, usuario, senha, driver)
        }

        catch (Exception e ){
            throw new RuntimeException("Não foi possivel se conectar ao banco, ${e.message}",e)
        }



    }
}
