package GerenciadoresDeBanco.Conectores

import groovy.sql.Sql

interface CriadorConexao {

     Sql criarconexao()

}