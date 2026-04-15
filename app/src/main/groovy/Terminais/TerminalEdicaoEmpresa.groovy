package Terminais


import GerenciadoresDeBanco.GerenciadorEmpresa
import GerenciadoresDeBanco.GerenciadorUsuario
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class TerminalEdicaoEmpresa {

    static edicaoEmpresa(String cnpj, Sql conexao, Scanner scan) {

        GerenciadorEmpresa gerenciador=new GerenciadorEmpresa(conexao)

        GroovyRowResult infosUsuario = gerenciador.capturarInformacoesPerfil(cnpj)
        Integer idUsuario = gerenciador.capturarId(cnpj)

        TerminalEdicaoBase.edicao(idUsuario, infosUsuario, new GerenciadorUsuario(conexao), scan ,conexao)

        String escolha = RequisidorDeEntradas.solicitarOpcao(scan, "O pais do seu perfil é ${infosUsuario.pais} , deseja altera-lo? S/N", ["S", "N"])
        if (escolha == "S") {
            String pais = RequisidorDeEntradas.solicitarPais(scan)
           gerenciador.editarPerfil(cnpj,pais)

        }

        println("Edição realizada com sucesso!")

    }

}














