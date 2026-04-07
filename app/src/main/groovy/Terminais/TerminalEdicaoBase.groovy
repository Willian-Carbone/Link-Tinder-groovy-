package Terminais

import Enuns.Especialidades
import Enuns.Estados
import Metodos.ControladorTerminal
import Metodos.GerenciadorBancoDados
import groovy.sql.GroovyRowResult

class TerminalEdicaoBase {
    static edicao(Integer idUsuario, GroovyRowResult infosUsuario, GerenciadorBancoDados gerenciadorBancoDados, Scanner scan) {

        ArrayList<String> opcoes = ["S", "N"]

        String escolha = ControladorTerminal.solicitarOpcao(scan, "Seu nome de usuario atual é ${infosUsuario.nome} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String nome = ControladorTerminal.solicitarNomeValido(scan)
            gerenciadorBancoDados.trocarNomeDoUsuario(idUsuario, nome)
        }


        escolha = ControladorTerminal.solicitarOpcao(scan, "Sua descrição de usuário  atual é ${infosUsuario.descricao} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String descricao = ControladorTerminal.solicitarDescricao(scan)
            gerenciadorBancoDados.trocarDescricaoDoUsuario(idUsuario, descricao)
        }


        escolha = ControladorTerminal.solicitarOpcao(scan, "Seu email cadastrado atualmente é ${infosUsuario.email} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String email = ControladorTerminal.solicitarDescricao(scan)
            gerenciadorBancoDados.trocarEmailDoUsuario(idUsuario, email)
        }


        escolha = ControladorTerminal.solicitarOpcao(scan, "Seu CEP cadastrado atualmente é ${infosUsuario.cep} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String cep = ControladorTerminal.solicitarCepValido(scan)
            gerenciadorBancoDados.trocarCepDoUsuario(idUsuario, cep)
        }


        escolha = ControladorTerminal.solicitarOpcao(scan, "Seu estado cadastrado atualmente é ${infosUsuario.estado} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            Estados estado = ControladorTerminal.solicitarEstadoValido(scan)
            gerenciadorBancoDados.trocarEstadoDoUsuario(idUsuario, estado)
        }


        escolha = ControladorTerminal.solicitarOpcao(scan, "As habilidades atualmente no seu perfil sao ${infosUsuario.competencias} deseja altera-las? S/N", opcoes)
        if (escolha == "S") {
            ArrayList<Especialidades> especialidades = ControladorTerminal.solicitarConjuntoEspecialidadesValidas(scan)
            gerenciadorBancoDados.trocarEspecialidadesDoUsuario(idUsuario, especialidades)
        }


    }
}