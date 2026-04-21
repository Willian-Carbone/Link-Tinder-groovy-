package Daos.Conectores

import groovy.sql.Sql


class ConectorBancoPostgreBase {

    static Sql conexao


    private ConectorBancoPostgreBase() {



        try {

            String url = 'jdbc:postgresql://localhost:5432/LinkEtinder'
            String usuario = 'willian'
            String senha = '5550178'
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
