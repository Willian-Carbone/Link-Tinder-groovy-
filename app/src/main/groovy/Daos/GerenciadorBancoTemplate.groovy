package Daos

import groovy.sql.Sql

abstract class GerenciadorBancoTemplate {
   protected Sql sql


    GerenciadorBancoTemplate(Sql sql){
       this.sql = sql
   }

    void desconectarDoBanco(){
        sql?.close()
    }

}
