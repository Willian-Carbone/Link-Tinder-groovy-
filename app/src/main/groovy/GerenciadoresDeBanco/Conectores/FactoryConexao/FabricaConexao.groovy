package GerenciadoresDeBanco.Conectores.FactoryConexao

import groovy.sql.Sql

interface FabricaConexao {

    Sql criarConexao()

}