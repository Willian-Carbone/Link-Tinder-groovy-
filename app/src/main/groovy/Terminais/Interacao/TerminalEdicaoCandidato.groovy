package Terminais.Interacao

import GerenciadoresDeBanco.GerenciadorCandidato
import GerenciadoresDeBanco.GerenciadorUsuario
import Modulos.GerenciadoresTerminal.Requisitores.RequisidorDeEntradas
import Modulos.validadoresDeEntradas.ValidadorIdade
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class TerminalEdicaoCandidato implements TerminalInterativo{

    @Override

    void navegar(String cpf, Sql conexao,Scanner scan) {

        GerenciadorCandidato gerenciador = new GerenciadorCandidato(conexao)

        GroovyRowResult infosUsuario = gerenciador.capturarInformacoesPerfil(cpf)
        Integer idUsuario = gerenciador.capturarId(cpf)


        TerminalEdicaoBase.edicao(idUsuario, infosUsuario, new GerenciadorUsuario(conexao), scan,conexao)

        String escolha = RequisidorDeEntradas.solicitarOpcao(scan, "A idade do seu perfil é ${infosUsuario.idade} , deseja altera-la? S/N", ["S", "N"])
        if (escolha == "S") {
            String idade = RequisidorDeEntradas.solicitarDadoBasicoValido("idade",scan,new ValidadorIdade())
            gerenciador.editarPerfil(cpf, idade as Integer)

        }

        println("Edição realizada com sucesso!")


    }


}
