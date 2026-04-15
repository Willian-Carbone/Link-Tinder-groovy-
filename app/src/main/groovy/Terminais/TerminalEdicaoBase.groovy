package Terminais

import Enuns.Especialidades
import Enuns.Estados
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaEmail
import GerenciadoresDeBanco.GerenciadorUsuario
import Modulos.ControladorTerminal
import Modulos.ConversoresEntrada.ConversorParaNomeEnum
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import Modulos.LocalizadorDadoEmEnum.LocalizadorEstado
import Modulos.validadoresDeEntradas.ValidadorCep
import Modulos.validadoresDeEntradas.ValidadorEmail
import Modulos.validadoresDeEntradas.ValidadorEstado
import Modulos.validadoresDeEntradas.ValidadorNome
import Objetos.EspecialidadeUsuario
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class TerminalEdicaoBase {
    static edicao(Integer idUsuario, GroovyRowResult infosUsuario, GerenciadorUsuario gerenciador, Scanner scan,Sql conexao) {

        ArrayList<String> opcoes = ["S", "N"]

        String escolha = RequisidorDeEntradas.solicitarOpcao(scan, "Seu nome de usuario atual é ${infosUsuario.nome} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String nome = RequisidorDeEntradas.solicitarDadoBasicoValido("nome",scan,new ValidadorNome())
            gerenciador.editarNomeUsuario(idUsuario,nome)
        }


        escolha = RequisidorDeEntradas.solicitarOpcao(scan, "Sua descrição de usuário  atual é ${infosUsuario.descricao} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String descricao = RequisidorDeEntradas.solicitarDescricao(scan)
            gerenciador.editarDescricaoUsuario(idUsuario,descricao)
        }


        escolha = RequisidorDeEntradas.solicitarOpcao(scan, "Seu email cadastrado atualmente é ${infosUsuario.email} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String email = RequisidorDeEntradas.solicitarCredencialValida(scan,new ValidadorEmail(), new ConfirmadorExistenciaEmail(conexao),"EMAIL")
            gerenciador.editarEmailUsuario(idUsuario,email)
        }


        escolha = RequisidorDeEntradas.solicitarOpcao(scan, "Seu CEP cadastrado atualmente é ${infosUsuario.cep} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String cep  = RequisidorDeEntradas.solicitarDadoBasicoValido("CEP" ,scan, new ValidadorCep())
            gerenciador.editarCepUsuario(idUsuario,cep)
        }


        escolha = RequisidorDeEntradas.solicitarOpcao(scan, "Seu estado cadastrado atualmente é ${infosUsuario.estado} , deseja altera-lo? S/N", opcoes)
        if (escolha == "S") {
            String estadoNome = RequisidorDeEntradas.solicitarDadoBasicoValido( "ESTADO",scan,new ValidadorEstado(), new ConversorParaNomeEnum())
            Estados estado = new LocalizadorEstado().capturarDadoEnum(estadoNome)

            gerenciador.editarEstado(idUsuario,estado)
        }


        escolha = RequisidorDeEntradas.solicitarOpcao(scan, "As habilidades atualmente no seu perfil sao ${infosUsuario.competencias} deseja altera-las? S/N", opcoes)
        if (escolha == "S") {
            ArrayList<Especialidades> especialidades = RequisidorDeEntradas.solicitarConjuntoEspecialidadesValidas(scan)

            ArrayList<EspecialidadeUsuario> novasEspecialidades=[]

            especialidades.forEach {
                EspecialidadeUsuario especialidadeUsuario = new EspecialidadeUsuario(idUsuario,it)
                novasEspecialidades.add(especialidadeUsuario)
            }
            gerenciador.editarEspecialidadeDoUsuario(idUsuario,novasEspecialidades)
        }


    }
}