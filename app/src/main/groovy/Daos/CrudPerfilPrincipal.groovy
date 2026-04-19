package Daos

import groovy.sql.GroovyRowResult

interface CrudPerfilPrincipal<T,O,P> {

    Integer criarPerfil(T objeto)

    void removerPerfil(String identificador)

    void editarPerfil(P id,O dadoNovo)

    GroovyRowResult capturarInformacoesPerfil(String identificador)

    Integer capturarId(String identificador)







}