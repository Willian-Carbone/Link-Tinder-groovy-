package GerenciadoresDeBanco.Conectores.FactoryConexao

import GerenciadoresDeBanco.Conectores.ConectorBancoPostgreBase
import groovy.sql.Sql

class FabricaConexaoBancoBase implements FabricaConexao{
    @Override
    Sql criarConexao() {

        return ConectorBancoPostgreBase.getConexao()

    }
}
