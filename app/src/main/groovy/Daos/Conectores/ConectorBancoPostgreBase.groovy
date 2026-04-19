package Daos.Conectores

import groovy.sql.Sql


class ConectorBancoPostgreBase {

    static Sql conexao


    private ConectorBancoPostgreBase() {



        try {

            String url = 'jdbc:postgresql://localhost:5432/linkEtinder'
            String usuario = 'postgres'
            String senha = '5550178wcb'
            String driver = 'org.postgresql.Driver'

            conexao = Sql.newInstance(url, usuario, senha, driver)



        }

        catch (Exception e) {
            throw new RuntimeException("Não foi possivel se conectar ao banco, ${e.message}", e)
        }
    }



    static Sql getConexao(){
        if(conexao==null){
            return new ConectorBancoPostgreBase().conexao
        }
        return conexao
    }


}
