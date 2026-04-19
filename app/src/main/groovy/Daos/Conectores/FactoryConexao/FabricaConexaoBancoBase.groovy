package Daos.Conectores.FactoryConexao

import Daos.Conectores.ConectorBancoPostgreBase
import groovy.sql.Sql

class FabricaConexaoBancoBase implements FabricaConexao{
    @Override
    Sql criarConexao() {

        return ConectorBancoPostgreBase.getConexao()

    }
}
