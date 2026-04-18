package Terminais.Interacao

import groovy.sql.Sql

interface TerminalInterativo {

    void navegar(String identificador, Sql conexao, Scanner scan)

}