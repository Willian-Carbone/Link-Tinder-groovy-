package Terminais

import Metodos.ControladorTerminal
import Metodos.GerenciadorBancoDados

import groovy.sql.GroovyRowResult

class TerminalEdicaoEmpresa {

    static edicaoEmpresa(String cnpj,GerenciadorBancoDados gerenciadorBancoDados) {

        GroovyRowResult infosUsuario = gerenciadorBancoDados.capturarInfos(cnpj)
        Integer idUsuario = gerenciadorBancoDados.capturarIdUsurio(cnpj)
        Scanner scan = new Scanner(System.in)

        TerminalEdicaoBase.edicao(idUsuario, infosUsuario, gerenciadorBancoDados, scan)

        String escolha = ControladorTerminal.solicitarOpcao(scan, "O pais do seu perfil é ${infosUsuario.pais} , deseja altera-lo? S/N", ["S", "N"])
        if (escolha == "S") {
            String pais = ControladorTerminal.solicitarPais(scan)
            gerenciadorBancoDados.trocarPaisDaEmpresa(cnpj,pais)

        }

        println("Edição realizada com sucesso!")

    }

}














