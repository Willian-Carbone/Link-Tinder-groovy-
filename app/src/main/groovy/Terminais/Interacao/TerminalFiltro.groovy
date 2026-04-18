package Terminais.Interacao

import Enuns.Especialidades
import Enuns.Estados

import GerenciadoresDeBanco.GerenciadorUsuario

import Modulos.ConversoresEntrada.ConversorParaNomeEnum
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import Modulos.LocalizadorDadoEmEnum.LocalizadorEstado
import Modulos.validadoresDeEntradas.ValidadorCnpj
import Modulos.validadoresDeEntradas.ValidadorCpf
import Modulos.validadoresDeEntradas.ValidadorEstado
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class TerminalFiltro implements  TerminalInterativo{

    @Override

    void navegar(String identificador, Sql conexao, Scanner scan)  {

        GerenciadorUsuario gerenciador= new GerenciadorUsuario(conexao)

        String escolhaFiltro = RequisidorDeEntradas.solicitarOpcao(scan, "Digite 1 para filtar os resultados por estado ou 2 para filtra-los pelas suas habilidades ", ["1", "2"])

        if (escolhaFiltro == "1") {

            String estadoNome = RequisidorDeEntradas.solicitarDadoBasicoValido( "ESTADO",scan,new ValidadorEstado(), new ConversorParaNomeEnum())
            Estados estadoEscolhido = new LocalizadorEstado().capturarDadoEnum(estadoNome)


            List<GroovyRowResult> usuariosNoEstado


            if (new ValidadorCpf().validarDado(identificador)) {
                usuariosNoEstado = gerenciador.buscarPorEstado(estadoEscolhido,"empresa")
            }

            else {
                usuariosNoEstado = gerenciador.buscarPorEstado(estadoEscolhido,"candidato")
            }


            if (usuariosNoEstado) {

                println("Foram encontrados os seguintes perfis")

                usuariosNoEstado.forEach { resultado ->
                    println("-------------------------------")
                    println("Numero do perfil: ${resultado.id}")
                    println("Areas atuantes : ${resultado.habilidades}")

                }

            }

            else {
                println("Não foram encontrados perfis para o estado informado")
            }


        }

        else {

            ArrayList<Especialidades> especialidades = RequisidorDeEntradas.solicitarConjuntoEspecialidadesValidas(scan)


            List<GroovyRowResult> usuariosHabilitados

            if (new ValidadorCnpj().validarDado(identificador)) {
                usuariosHabilitados = gerenciador.buscarPorHabilidades(especialidades,"candidato")
            } else {
                usuariosHabilitados =  gerenciador.buscarPorHabilidades(especialidades,"empresa")
            }


            if (!usuariosHabilitados) {
                println("Não foram encontrados perfis com as habilidades requeridas")
            } else {
                usuariosHabilitados.forEach { usuario ->
                    println("------------------------------------------")
                    println("Numero do perfil: ${usuario.id}")
                    println("Competencias: ${usuario.habilidades}")

                }
            }

        }


    }
}
