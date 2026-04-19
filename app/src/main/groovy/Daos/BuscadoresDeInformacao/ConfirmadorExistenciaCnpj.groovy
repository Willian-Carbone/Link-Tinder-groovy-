package Daos.BuscadoresDeInformacao

import Daos.GerenciadorBancoTemplate
import groovy.sql.Sql

class ConfirmadorExistenciaCnpj extends GerenciadorBancoTemplate implements CheckerDadoRegistradoI {
    ConfirmadorExistenciaCnpj(Sql sql) {
        super(sql)
    }

    @Override
    Boolean buscarExistenciaDado(String cnpj) {

        try {

            return sql.firstRow("SELECT 1 FROM empresa WHERE cnpj = ?", [cnpj]) ? true : false
        }

        catch (Exception e) {
            throw new RuntimeException("Nao foi possivel se conectar ao banco ${e.message}", e)
        }

    }
}
