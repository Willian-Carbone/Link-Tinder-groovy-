package Daos.BuscadoresDeInformacao

import Daos.GerenciadorBancoTemplate
import groovy.sql.Sql

class ConfirmadorExistenciaCpf extends GerenciadorBancoTemplate implements CheckerDadoRegistradoI{

    ConfirmadorExistenciaCpf(Sql sql) {
        super(sql)
    }

    @Override
    Boolean buscarExistenciaDado(String cpf) {

        try {

            return sql.firstRow("SELECT 1 FROM candidato WHERE cpf = ?", [cpf]) ? true : false
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel se conectar ao banco ${e.message}", e)
        }
    }
}
