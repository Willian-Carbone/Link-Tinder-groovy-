package GerenciadoresDeBanco

import groovy.sql.GroovyRowResult

interface MatchableI {
    List<GroovyRowResult> buscarMatchs(String identificador)
    List<GroovyRowResult> buscarVagas(String identificador)


}