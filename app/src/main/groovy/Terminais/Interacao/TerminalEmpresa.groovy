package Terminais.Interacao

import Enuns.Especialidades

import GerenciadoresDeBanco.GerenciadorCandidato
import GerenciadoresDeBanco.GerenciadorEmpresa
import GerenciadoresDeBanco.GerenciadorVaga
import Modulos.GerenciadoresTerminal.Impressores.ExibicaoCandidatosQueCurtiramVaga

import Modulos.GerenciadoresTerminal.Impressores.ExibicaoMatchEmpresa
import Modulos.GerenciadoresTerminal.Impressores.ExibicaoVagasEmpresa
import Modulos.GerenciadoresTerminal.Impressores.Impressor
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import Objetos.Vaga
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class TerminalEmpresa implements TerminalInterativo {

    @Override
    void navegar(String cnpj, Sql conexao, Scanner scan) {

        List<String> opcoesMenuEmpresa = ["1", "2", "3", "4", "5", "6", "7", "8"]
        String opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair", opcoesMenuEmpresa)

        Impressor impressora

       GerenciadorEmpresa gerenciadorEmpresa=new GerenciadorEmpresa(conexao)
        GerenciadorVaga gerenciadorVaga = new GerenciadorVaga(conexao)



        while (opcaoSelecionada != "8") {


            switch (opcaoSelecionada) {

                case "1":



                    println("Informe o nome da Vaga")

                    String nomeVaga=scan.nextLine()

                    println("Informe a descrição da vaga")

                    String descricaoVaga= scan.nextLine()

                    ArrayList<Especialidades> especialidadesDaVaga = RequisidorDeEntradas.solicitarConjuntoEspecialidadesValidas(scan)

                    Vaga vaga = new Vaga(nomeVaga, descricaoVaga, cnpj, especialidadesDaVaga)

                    gerenciadorVaga.gravarVaga(vaga)

                    println("Vaga adicionada com sucesso")

                    break



                case "2":

                    ExibicaoCandidatosQueCurtiramVaga impressorCandidatos = new ExibicaoCandidatosQueCurtiramVaga()
                    ExibicaoVagasEmpresa impressorVaga = new ExibicaoVagasEmpresa()


                    List<GroovyRowResult> vagasDaEmpresa = gerenciadorEmpresa.buscarVagas(cnpj)

                    if (!vagasDaEmpresa) {
                        println("Não foram encontradas vagas cadastradas")
                    } else {


                        vagasDaEmpresa.forEach { GroovyRowResult vaga ->
                            impressora = impressorVaga
                            impressora.exibirDado(vaga)

                            List<GroovyRowResult> candidatosQueCurtiramVaga=gerenciadorVaga.capturarInteressadosNaVaga(vaga.id as Integer,cnpj,new GerenciadorCandidato(conexao))


                            if (!candidatosQueCurtiramVaga) {
                                println("Nenhum candidato curtiu a vaga")
                            } else {

                                impressora = impressorCandidatos
                                impressora.exibirDado(candidatosQueCurtiramVaga)


                            }

                        }

                        Map relacaoCandidatoEVagasCurtida = gerenciadorEmpresa.capturarCandidatosEVagasQueCurtiram(cnpj,vagasDaEmpresa,conexao)

                        if (relacaoCandidatoEVagasCurtida) {
                            RequisidorDeEntradas.cederOpcaoCurtirCandidatoDevolta(scan,relacaoCandidatoEVagasCurtida,conexao,cnpj)
                        }
                    }


                    break


                case "3":

                    impressora = new ExibicaoVagasEmpresa()



                    List<GroovyRowResult> vagas = gerenciadorEmpresa.buscarVagas(cnpj)

                    if (!vagas) {
                        println("Não foram encontradas vagas registradas")
                    } else {

                        Map<String, Map> infosVagas = [:]
                        vagas.forEach { GroovyRowResult vaga ->

                            impressora.exibirDado(vaga)

                            infosVagas[(vaga.id as String)] = [
                                    vagaId   : vaga.id as Integer,
                                    nome     : vaga.nome,
                                    descricao: vaga.descricao
                            ]

                        }

                        List<String> vagasParaEditar = RequisidorDeEntradas.escolherVagasParaEditar(scan,infosVagas)
                        ArrayList<String> opcoesResposta = ["S", "N"]



                        vagasParaEditar.forEach { identificador ->

                            println("--------------------------------------------")
                            println("Edicão da vaga de numero ${identificador}")

                            opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "Deseja deletar a vaga? S/N", opcoesResposta)

                            if (opcaoSelecionada == "S") {
                               gerenciadorVaga.removerVaga(identificador as Integer)
                                return
                            }

                            opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "o Nome atual da vaga é ${infosVagas[identificador].nome} , deseja altera-lo? S/N", opcoesResposta)

                            if (opcaoSelecionada == "S") {
                                println("Digite o novo nome da vaga")
                                String nome = scan.nextLine()
                                gerenciadorVaga.trocarNomeDaVaga(identificador as Integer,nome)

                            }

                            opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "a descrição atual da vaga é ${infosVagas[identificador].descricao} , deseja altera-la? S/N", opcoesResposta)

                            if (opcaoSelecionada == "S") {
                                println("Digite a nova descricao da vaga")
                                String desc = scan.nextLine()
                                gerenciadorVaga.trocarDescricaoDaVaga(identificador as Integer,desc)

                            }




                        }
                    }

                    break



                case "4":
                    impressora = new ExibicaoMatchEmpresa()
                    List<GroovyRowResult> listagemMatchs = gerenciadorEmpresa.buscarMatchs(cnpj)
                    impressora.exibirDado(listagemMatchs)
                    break

                case "5":
                    TerminalInterativo terminal = new TerminalFiltro()
                   terminal.navegar(cnpj, conexao, scan)
                    break



                case "6":

                    TerminalInterativo terminal = new TerminalEdicaoEmpresa()
                    terminal.navegar(cnpj, conexao, scan)
                    break

                case "7":
                   RequisidorDeEntradas.solicitarRespostaRemocaoPerfil(cnpj,scan,new GerenciadorEmpresa(conexao))
                    break


            }

            opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair", opcoesMenuEmpresa)

        }
    }

}
