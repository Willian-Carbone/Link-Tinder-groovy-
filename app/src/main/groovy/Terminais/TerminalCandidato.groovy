package Terminais

import GerenciadoresDeBanco.GerenciadorCandidato
import Modulos.GerenciadoresTerminal.ExibicaoInfos
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class TerminalCandidato {

    static void terminalPrincipal(String cpf, Sql conexao, Scanner scan) {

        List<String> opcoesMenuCandidato = ["1", "2", "3", "4", "5", "6"]

        String opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair", opcoesMenuCandidato)

        GerenciadorCandidato gerenciadorCandidato = new GerenciadorCandidato(conexao)

        while (opcaoSelecionada != "6") {

            switch (opcaoSelecionada) {
                case "1":
                    List<GroovyRowResult> listaVagas = gerenciadorCandidato.buscarVagas(cpf)

                    ArrayList<Integer> identificadoresDasVagas = ExibicaoInfos.exibirVagasParaCandidatos(listaVagas)

                    if (identificadoresDasVagas) {
                        RequisidorDeEntradas.darOpcaoDeCurtirVagas(scan, identificadoresDasVagas, gerenciadorCandidato, cpf)
                    }

                    break



                case "2":
                    List<GroovyRowResult> listagemMatchs = gerenciadorCandidato.buscarMatchs(cpf)
                    ExibicaoInfos.exibirMatchsParaCandidato(listagemMatchs)

                    break

                case "3":

                    TerminalEdicaoCandidato.edicaoCandidato(cpf,conexao,scan)

                    break

                case "4":

                    RequisidorDeEntradas.solicitarRespostaRemocaoPerfil(cpf,scan,gerenciadorCandidato)

                    break

                case "5":

                    TerminalFiltro.filtragem(cpf,conexao,scan)

                    break
            }

            opcaoSelecionada  = RequisidorDeEntradas.solicitarOpcao(scan, "Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair", opcoesMenuCandidato)

        }


    }


}
