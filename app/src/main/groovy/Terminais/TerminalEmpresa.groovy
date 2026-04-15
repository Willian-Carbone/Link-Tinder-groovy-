package Terminais

import Enuns.Especialidades
import GerenciadoresDeBanco.GerenciadorCandidato
import GerenciadoresDeBanco.GerenciadorEmpresa
import GerenciadoresDeBanco.GerenciadorVaga
import Modulos.GerenciadoresTerminal.ExibicaoInfos
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import Objetos.Vaga
import groovy.sql.GroovyRowResult
import groovy.sql.Sql


class TerminalEmpresa {
    static void terminalPrincipal(String cnpj, Sql conexao, Scanner scan) {

        List<String> opcoesMenuEmpresa = ["1", "2", "3", "4", "5", "6", "7", "8"]
        String opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair", opcoesMenuEmpresa)


        while (opcaoSelecionada != "8") {


            switch (opcaoSelecionada) {

                case "1":

                    GerenciadorVaga gerenciadorVaga = new GerenciadorVaga(conexao)

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

                    GerenciadorEmpresa gerenciador = new GerenciadorEmpresa(conexao)

                    List<GroovyRowResult> vagasDaEmpresa = gerenciador.buscarVagas(cnpj)

                    if (!vagasDaEmpresa) {
                        println("Não foram encontradas vagas cadastradas")
                    } else {

                        GerenciadorVaga gerenciadorVaga =new GerenciadorVaga(conexao)

                        vagasDaEmpresa.forEach { GroovyRowResult vaga ->
                            ExibicaoInfos.exibirVagaParaEmpresas(vaga)

                            List<GroovyRowResult> candidatosQueCurtiramVaga=gerenciadorVaga.capturarInteressadosNaVaga(vaga.id as Integer,cnpj,new GerenciadorCandidato(conexao))


                            if (!candidatosQueCurtiramVaga) {
                                println("Nenhum candidato curtiu a vaga")
                            } else {

                                ExibicaoInfos.imprimirCandidatosInteressados(candidatosQueCurtiramVaga)

                            }

                        }

                        Map relacaoCandidatoEVagasCurtida = ExibicaoInfos.capturarCandidatosEVagasQueCurtiram(cnpj, conexao)

                        if (relacaoCandidatoEVagasCurtida) {
                            RequisidorDeEntradas.cederOpcaoCurtirCandidatoDevolta(scan,relacaoCandidatoEVagasCurtida,conexao,cnpj)
                        }
                    }


                    break


                case "3":

                   GerenciadorEmpresa gerenciador= new GerenciadorEmpresa(conexao)


                    List<GroovyRowResult> vagas = gerenciador.buscarVagas(cnpj)

                    if (!vagas) {
                        println("Não foram encontradas vagas registradas")
                    } else {

                        Map<String, Map> infosVagas = [:]
                        vagas.forEach { GroovyRowResult vaga ->

                            ExibicaoInfos.exibirVagaParaEmpresas(vaga)

                            infosVagas[(vaga.id as String)] = [
                                    vagaId   : vaga.id as Integer,
                                    nome     : vaga.nome,
                                    descricao: vaga.descricao
                            ]

                        }

                        List<String> vagasParaEditar = RequisidorDeEntradas.escolherVagasParaEditar(scan,infosVagas)
                        ArrayList<String> opcoesResposta = ["S", "N"]

                        GerenciadorVaga gerenciadorVaga = new GerenciadorVaga(conexao)


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
                    List<GroovyRowResult> listagemMatchs = new GerenciadorEmpresa(conexao).buscarMatchs(cnpj)
                    ExibicaoInfos.exibirMatchsParaEmpresas(listagemMatchs)
                    break

                case "5":
                    TerminalFiltro.filtragem(cnpj, conexao, scan)
                    break



                case "6":
                    TerminalEdicaoEmpresa.edicaoEmpresa(cnpj, conexao,scan)
                    break

                case "7":
                   RequisidorDeEntradas.solicitarRespostaRemocaoPerfil(cnpj,scan,new GerenciadorEmpresa(conexao))
                    break


            }

            opcaoSelecionada = RequisidorDeEntradas.solicitarOpcao(scan, "digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair", opcoesMenuEmpresa)

        }
    }

}
