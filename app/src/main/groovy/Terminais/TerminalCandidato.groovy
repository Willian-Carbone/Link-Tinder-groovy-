package Terminais

import Metodos.ControladorTerminal
import Metodos.GerenciadorBancoDados
import groovy.sql.GroovyRowResult


class TerminalCandidato {

    static void terminalPrincipal(String cpf, GerenciadorBancoDados gerenciadorBancoDados, Scanner scan) {

        List<String> opcoesMenuCandidato = ["1", "2", "3", "4", "5", "6"]

        String opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair", opcoesMenuCandidato)


        while (opcaoSelecionada != "6") {

            switch (opcaoSelecionada) {
                case "1":
                    List<GroovyRowResult> listaVagas = gerenciadorBancoDados.listagemVagasCandidato(cpf)
                    ArrayList<Integer> identificadoresDasVagas = ControladorTerminal.exibirVagasParaCandidatos(listaVagas)

                    if (identificadoresDasVagas) {
                        ControladorTerminal.darOpcaoDeCurtirVagas(scan, identificadoresDasVagas, gerenciadorBancoDados, cpf)
                    }

                    break


                case "2":
                    List<GroovyRowResult> listagemMatchs = gerenciadorBancoDados.buscarMatches(cpf)
                    ControladorTerminal.exibirMatchsParaCandidato(listagemMatchs)

                    break

                case "3":

                    TerminalEdicaoCandidato.edicaoCandidato(cpf, gerenciadorBancoDados)

                    break


                case "4":

                    ControladorTerminal.solicitarRespostaRemocaoPerfil(cpf,scan,gerenciadorBancoDados,)

                    break

                case "5":

                    TerminalFiltro.filtragem(cpf, gerenciadorBancoDados, scan)

                    break
            }

            opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair", opcoesMenuCandidato)

        }


    }


}
