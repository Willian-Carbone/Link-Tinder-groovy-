package GerenciadoresDeBanco.BuscadoresDeInformacao

import GerenciadoresDeBanco.GerenciadorBancoTemplate
import groovy.sql.Sql

class ConfirmadorExistenciaEmail extends GerenciadorBancoTemplate implements CheckerDadoRegistradoI {
    ConfirmadorExistenciaEmail(Sql sql) {
        super(sql)
    }

    @Override
    Boolean buscarExistenciaDado(String email) {

        try {
            return sql.firstRow("SELECT 1 FROM usuario WHERE email = ? LIMIT 1", [email]) ? true : false
        }


        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel se conectar ao banco ${e.message}", e)
        }}
}


