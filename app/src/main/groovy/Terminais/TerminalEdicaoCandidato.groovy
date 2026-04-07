package Terminais

import Metodos.ControladorTerminal
import Metodos.GerenciadorBancoDados
import groovy.sql.GroovyRowResult


class TerminalEdicaoCandidato {

    static edicaoCandidato(String cpf, GerenciadorBancoDados gerenciadorBancoDados) {

        GroovyRowResult infosUsuario = gerenciadorBancoDados.capturarInfos(cpf)
        Integer idUsuario = gerenciadorBancoDados.capturarIdUsurio(cpf)
        Scanner scan = new Scanner(System.in)

        TerminalEdicaoBase.edicao(idUsuario, infosUsuario, gerenciadorBancoDados, scan)

        String escolha = ControladorTerminal.solicitarOpcao(scan, "A idade do seu perfil é ${infosUsuario.idade} , deseja altera-la? S/N", ["S", "N"])
        if (escolha == "S") {
            String idade = ControladorTerminal.solicitarIdadeValida(scan)
            gerenciadorBancoDados.trocarIdadeDoCandidato(cpf, Integer.parseInt(idade))

        }

        println("Edição realizada com sucesso!")


    }


}
