package Controller.Cadastros

import groovy.sql.Sql

interface TerminalCadastro {

    void cadastrar(Sql conexao,Scanner scan)

}