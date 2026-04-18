package Terminais.Interacao

import GerenciadoresDeBanco.GerenciadorCandidato
import GerenciadoresDeBanco.GerenciadorVaga

import Modulos.GerenciadoresTerminal.Impressores.ExibicaoMatchCandidato
import Modulos.GerenciadoresTerminal.Impressores.ExibicaoVagasCandidato
import Modulos.GerenciadoresTerminal.Impressores.Impressor
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class TerminalCandidato implements TerminalInterativo {

    @Override

    void navegar(String cpf, Sql conexao, Scanner scan) {

        List<String> opcoesMenuCandidato = ["1", "2", "3", "4", "5", "6"]

        String opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair", opcoesMenuCandidato)

        Impressor impressora

        GerenciadorCandidato gerenciadorCandidato = new GerenciadorCandidato(conexao)

        while (opcaoSelecionada != "6") {

            switch (opcaoSelecionada) {
                case "1":

                    List<GroovyRowResult> listaVagas = gerenciadorCandidato.buscarVagas(cpf)

                    impressora= new ExibicaoVagasCandidato()

                    ArrayList<Integer> identificadoresDasVagas = new GerenciadorVaga(conexao).capturarIdentificadoresDasVagas(listaVagas)
                    impressora.exibirDado(listaVagas)

                    if (identificadoresDasVagas) {
                        RequisidorDeEntradas.darOpcaoDeCurtirVagas(scan, identificadoresDasVagas, gerenciadorCandidato, cpf)
                    }

                    break



                case "2":

                    impressora = new ExibicaoMatchCandidato()


                    List<GroovyRowResult> listagemMatchs = gerenciadorCandidato.buscarMatchs(cpf)
                    impressora.exibirDado(listagemMatchs)

                    break

                case "3":

                    TerminalInterativo terminal = new TerminalEdicaoCandidato()
                    terminal.navegar(cpf,conexao,scan)

                    break

                case "4":

                    RequisidorDeEntradas.solicitarRespostaRemocaoPerfil(cpf,scan,gerenciadorCandidato)

                    break

                case "5":

                    TerminalInterativo terminal = new TerminalFiltro()
                    terminal.navegar(cpf,conexao,scan)

                    break
            }

            opcaoSelecionada  = RequisidorDeEntradas.solicitarOpcao(scan, "Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair", opcoesMenuCandidato)

        }


    }


}
