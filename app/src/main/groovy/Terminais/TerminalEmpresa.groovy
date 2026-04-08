package Terminais

import Enuns.Especialidades
import Metodos.ControladorTerminal
import Metodos.GerenciadorBancoDados
import Objetos.Vaga
import groovy.sql.GroovyRowResult


class TerminalEmpresa {
    static void terminalPrincipal(String cnpj, GerenciadorBancoDados gerenciadorBancoDados, Scanner scan) {

        List<String> opcoesMenuEmpresa = ["1", "2", "3", "4", "5", "6", "7", "8"]
        String opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair", opcoesMenuEmpresa)


        while (opcaoSelecionada != "8") {


            switch (opcaoSelecionada) {

                case "1":
                    String nomeVaga = ControladorTerminal.solicitarNomeValido(scan)
                    String descricaoVaga = ControladorTerminal.solicitarDescricao(scan)
                    ArrayList<Especialidades> especialidadesDaVaga = ControladorTerminal.solicitarConjuntoEspecialidadesValidas(scan)

                    Vaga vaga = new Vaga(nomeVaga, descricaoVaga, cnpj, especialidadesDaVaga)

                    gerenciadorBancoDados.registrarVaga(vaga)

                    println("Vaga adicionada com sucesso")

                    break



                case "2":

                    List<GroovyRowResult> vagasDaEmpresa = gerenciadorBancoDados.capturarVagasDaEmpresa(cnpj)

                    if (!vagasDaEmpresa) {
                        println("Não foram encontradas vagas cadastradas")
                    } else {

                        vagasDaEmpresa.forEach { GroovyRowResult vaga ->
                            ControladorTerminal.exibirVagaParaEmpresas(vaga)

                            List<Map> candidatosQueCurtiramVaga = gerenciadorBancoDados.capturarCandidatosInteressados(vaga.id as Integer, cnpj)

                            if (!candidatosQueCurtiramVaga) {
                                println("Nenhum candidato curtiu a vaga")
                            } else {
                                ControladorTerminal.imprimirCandidatosInteressados(candidatosQueCurtiramVaga)
                            }

                        }

                        Map relacaoCandidatoEVagasCurtida = ControladorTerminal.capturarCandidatosEVagasQueCurtiram(cnpj, gerenciadorBancoDados)

                        if (relacaoCandidatoEVagasCurtida) {
                            ControladorTerminal.cederOpcaoCurtirCandidatoDevolta(scan, relacaoCandidatoEVagasCurtida, gerenciadorBancoDados, cnpj)
                        }
                    }


                    break


                case "3":
                    List<GroovyRowResult> vagas = gerenciadorBancoDados.capturarVagasDaEmpresa(cnpj)

                    if (!vagas) {
                        println("Não foram encontradas vagas registradas")
                    } else {

                        Map<String, Map> infosVagas = [:]
                        vagas.forEach { GroovyRowResult vaga ->

                            ControladorTerminal.exibirVagaParaEmpresas(vaga)

                            infosVagas[(vaga.id as String)] = [
                                    vagaId   : vaga.id as Integer,
                                    nome     : vaga.nome,
                                    descricao: vaga.descricao
                            ]

                        }

                        List<String> vagasParaEditar = ControladorTerminal.escolherVagasParaEditar(scan, infosVagas)
                        ArrayList<String> opcoesResposta = ["S", "N"]

                        vagasParaEditar.forEach { identificador ->

                            println("--------------------------------------------")
                            println("Edicão da vaga de numero ${identificador}")

                            opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "Deseja deletar a vaga? S/N", opcoesResposta)

                            if (opcaoSelecionada == "S") {
                                gerenciadorBancoDados.removerVaga(identificador as Integer)
                                return
                            }

                            opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "o Nome atual da vaga é ${infosVagas[identificador].nome} , deseja altera-lo? S/N", opcoesResposta)

                            if (opcaoSelecionada == "S") {
                                println("Digite o novo nome da vaga")
                                String nome = scan.nextLine()
                                gerenciadorBancoDados.trocarNomeDaVaga(identificador as Integer, nome)

                            }

                            opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "a descrição atual da vaga é ${infosVagas[identificador].descricao} , deseja altera-la? S/N", opcoesResposta)

                            if (opcaoSelecionada == "S") {
                                println("Digite a nova descricao da vaga")
                                String desc = scan.nextLine()
                                gerenciadorBancoDados.trocarDescricaoDaVaga(identificador as Integer, desc)

                            }




                        }
                    }

                    break



                case "4":
                    List<GroovyRowResult> listagemMatchs = gerenciadorBancoDados.buscarMatches(cnpj)
                    ControladorTerminal.exibirMatchsParaEmpresas(listagemMatchs)
                    break

                case "5":
                    TerminalFiltro.filtragem(cnpj, gerenciadorBancoDados, scan)
                    break



                case "6":
                    TerminalEdicaoEmpresa.edicaoEmpresa(cnpj, gerenciadorBancoDados)
                    break

                case "7":
                   ControladorTerminal.solicitarRespostaRemocaoPerfil(cnpj,scan,gerenciadorBancoDados,)
                    break


            }

            opcaoSelecionada = ControladorTerminal.solicitarOpcao(scan, "digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair", opcoesMenuEmpresa)


        }
    }

}
