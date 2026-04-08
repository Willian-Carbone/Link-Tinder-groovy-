package Terminais

import Enuns.Especialidades
import Enuns.Estados
import Metodos.ControladorTerminal
import Metodos.GerenciadorBancoDados
import Metodos.Utilidades
import groovy.sql.GroovyRowResult


class TerminalFiltro {

    static filtragem(String identificador, GerenciadorBancoDados gerenciadorBancoDados, Scanner scan) {

        String escolhaFiltro = ControladorTerminal.solicitarOpcao(scan, "Digite 1 para filtar os resultados por estado ou 2 para filtra-los pelas suas habilidades ", ["1", "2"])

        if (escolhaFiltro == "1") {

            Estados estadoEscolhido = ControladorTerminal.solicitarEstadoValido(scan)

            List<GroovyRowResult> usuariosNoEstado


            if (Utilidades.validadorCnpj(identificador)) {
                usuariosNoEstado = gerenciadorBancoDados.buscarCandidatosPorEstado(estadoEscolhido)
            }

            else {
                usuariosNoEstado = gerenciadorBancoDados.buscarEmpresasPorEstado(estadoEscolhido)
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

            ArrayList<Especialidades> especialidades = ControladorTerminal.solicitarConjuntoEspecialidadesValidas(scan)


            List<GroovyRowResult> usuariosHabilitados

            if (Utilidades.validadorCnpj(identificador)) {
                usuariosHabilitados = gerenciadorBancoDados.buscarPorHabilidades(especialidades,"candidato")
            } else {
                usuariosHabilitados = gerenciadorBancoDados.buscarPorHabilidades(especialidades,"empresa")
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
