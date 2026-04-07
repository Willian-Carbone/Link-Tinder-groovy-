package Terminais

import Metodos.GerenciadorBancoDados
import groovy.sql.GroovyRowResult


class TerminalCandidato {

    static void terminalPrincipal(String cpf,GerenciadorBancoDados gdb,Scanner scan){



        println("Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver empresas ou 6 para sair")
        String escolha = scan.nextLine()

        while(escolha!="6") {

            while (escolha != "1" && escolha != "2" && escolha != "3" && escolha != "4" && escolha != "5" ) {
                println("insira um valor valido")
                escolha = scan.nextLine()
            }

            switch (escolha) {
                case "1":
                    List listaVagas = gdb.listagemVagasCandidato(cpf)
                    ArrayList<Integer>  identificadoresVagas= new ArrayList<>()

                    if(listaVagas) {

                        for (vaga in listaVagas) {
                            println("""              Nome:${vaga.nome}
                       Identificador da vaga:${vaga.id_vaga}
                       Identificador do contratante anonimo:${vaga.id_contratante}
                       Descriçao da vaga:${vaga.descricao}
                       Requisitos da vaga:${vaga.competencias}
                       -------------------------------------""")

                            identificadoresVagas.add(vaga.id_vaga as Integer)
                        }

                        println("Digite fim para sair ou o numero do identificador da vaga para curti-la")
                        String entrada = scan.nextLine().toUpperCase()

                        while (entrada != "FIM") {
                            if (entrada.isInteger()) {

                                Integer idDigitado = entrada.toInteger()

                                if (idDigitado in identificadoresVagas) {
                                    if (gdb.registrarCurtidaVaga(cpf, idDigitado)) {
                                        println("Curtida enviada para a vaga : ${idDigitado}")

                                    } else {
                                        println("Vocẽ ja curtiu essa vaga , tente outra")
                                    }


                                } else {
                                    println("ID ${idDigitado} não está na lista de vagas mostradas.")
                                }
                            } else {
                                println("Entrada inválida! Digite um número ou 'FIM'.")
                            }

                            println("\nPróximo ID (ou FIM): ")
                            entrada = scan.nextLine().toUpperCase()
                        }
                    }

                    else{println("Não foram encontradas Vagas para você")}


                    break

                case "2":
                    List<GroovyRowResult> listagemMatchs = gdb.buscarMatches(cpf)

                    if(listagemMatchs){
                        println("As seguintes empresas curtiram seu perfil")
                        println("---------------------------")

                        for (match in listagemMatchs){

                            println("Nome da empresa:${match.nome}")
                            println("Descricao da empresa:${match.descricao}")
                            println("Email de contato:${match.email}")
                            println("Pais de atuação:${match.pais}")
                            println("Estado de atuação:${match.estado}")
                            println("Identificador da vaga onde ocorreu o match:${match.vaga}")
                            println("---------------------------")

                        }
                    }

                    else{

                        println("Não foram encontrados matchs realizados para seu perfil")

                    }



                    break

               case "3":

                    TerminalEdicaoCandidato.edicaoCandidato(cpf,gdb)

                    break

                case "4":
                    println("Essa ação nao poderá ser desfeita, se tiver certeza, digite seu cpf para que seu perfil seja deletado, qualquer outra entrada retornara ")
                    String checkDelet=scan.nextLine()

                    if(checkDelet==cpf){
                        gdb.removerPerfil(cpf)

                        System.exit(0)

                    }

                    break

                case "5":

                    TerminalFiltro.filtragem(cpf,gdb,scan)

                    break
            }

            println("Escolha 1 para ver vagas ou 2 para ver seus matchs,3 para editar seu perfil , 4 para exclui-lo, 5 para ver  empresas ou 6 para sair")
            escolha = scan.nextLine()

        }


    }



}
