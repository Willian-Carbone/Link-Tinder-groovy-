package Terminais

import Enuns.Especialidades
import Metodos.GerenciadorBancoDados
import Metodos.Utilidades
import Objetos.Vaga
import groovy.sql.GroovyRowResult



class TerminalEmpresa {
    static void terminalPrincipal(String cnpj,GerenciadorBancoDados gbd,Scanner scan) {
        println("digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair")


        String acao = scan.nextLine()

        while (acao != "8") {

            while (acao != "1" && acao != "2" && acao != "3" && acao != "4" && acao != "5" && acao !="6" && acao!="7")  {
                println("Insira um valor valido")
                acao = scan.nextLine()
            }


            switch (acao) {
                case "1":
                    println("informe o nome da vaga")
                    String nome = scan.nextLine()

                    println("Informe uma descrição para a vaga")
                    String desc = scan.nextLine()


                    ArrayList<Especialidades> competencias = new ArrayList<>()

                    println("informe as habilidades necessárias para a vaga, ao menos 1 deve ser selecionada")

                    println("""
                   PT --> phyton
                   JAV --> java
                   Ang --> Angular
                   Spr --> spring
                   HT--> html
                   Cs --> para css
                   Fim --> saida
                                """)

                    String especialidade = scan.nextLine().toUpperCase()


                    while (especialidade != "FIM" || competencias.size() == 0) {


                        if (Utilidades.checarSeCompetenciaExiste(especialidade)) {

                            Especialidades espec = Especialidades.valueOf(especialidade)
                            if (!competencias.contains(espec)) {
                                competencias.add(espec)
                            }
                            especialidade = scan.nextLine().toUpperCase()
                        } else {

                            println("insira um valor válido")
                            especialidade = scan.nextLine().toUpperCase()

                        }


                    }


                    Vaga vaga = new Vaga(nome, desc, cnpj, competencias)

                    gbd.registrarVaga(vaga)


                    println("Vaga adicionada com sucesso")

                    break




                case "2":

                    List<GroovyRowResult> minhasVagas = gbd.capturarVagasDaEmpresa(cnpj)



                    if(minhasVagas) {

                        Map<String, Map> mapaDeMatch = [:]

                        minhasVagas.each { vaga ->

                            List<Map> candidatosCurtiramVaga = gbd.capturarCandidatosInteressados(vaga.id as Integer,cnpj)
                            println("*************************************")
                            println("Numero da Vaga:${vaga.id}")
                             println("Nome da Vaga: ${vaga.nome}")
                             println("Descrição da vaga:${vaga.descricao}")
                            println("_________________________________________")

                            println("Candidatos que curtiram a vaga e suas habilidades")

                            if(candidatosCurtiramVaga) {

                                candidatosCurtiramVaga.forEach {
                                    candidato->
                                        println("Numero do Candidato: ${candidato.id}")
                                        println("Habilidades : ${candidato.competencias}")
                                        println("_______________________________________")



                                        mapaDeMatch[(candidato.id as String)] = [
                                                cpf: candidato.cpf,
                                                vagaId: vaga.id as Integer
                                        ]
                                }


                            }

                            else{
                                println("Nenhum candidato demostrou interesse nessa vaga")
                            }



                        }


                        println("Digite o numero do candidato para curti-lo devolta ou fim para sair")
                        String decisao = scan.nextLine().toUpperCase()

                        while (decisao != "FIM") {
                            if (mapaDeMatch.containsKey(decisao)) {
                                Map dadosParaMatch = mapaDeMatch[decisao]

                                gbd.registrarMatch(dadosParaMatch.cpf as String, cnpj, dadosParaMatch.vagaId as Integer)

                                println("Match realizado com sucesso para o candidato ${decisao}!")


                                mapaDeMatch.remove(decisao)
                            } else {
                                println("Identificador não encontrado ou já processado.")

                            }

                            println("Digite outro ID ou 'FIM' para sair:")
                            decisao = scan.nextLine().toUpperCase()
                        }


                    }

                    else{
                        println("Vocẽ nao possui vagas cadastradas")
                    }

                    break

                case "3":
                    List<GroovyRowResult> minhasVagas = gbd.capturarVagasDaEmpresa(cnpj)
                    if(minhasVagas){

                        Map<String, Map> mapaDeVaga = [:]

                        println("Foram encontradas as seguintes vagas")

                        minhasVagas.forEach {vaga->
                            println("-----------------------------------------")
                            println("Numero da Vaga:${vaga.id}")
                            println("Nome da Vaga: ${vaga.nome}")
                            println("Descrição da vaga:${vaga.descricao}")

                            mapaDeVaga[(vaga.id as String)] = [
                                    vagaId: vaga.id as Integer,
                                    nome:vaga.nome,
                                    descricao:vaga.descricao
                            ]
                        }

                        List<String> vagasParaEditar=[]

                        println("Digite o numero da vaga para coloca-la na fila de edição  ou fim para sair")
                        String decisao = scan.nextLine().toUpperCase()


                        while (decisao != "FIM") {
                            if (mapaDeVaga.containsKey(decisao)) {
                                if (vagasParaEditar.add(decisao)) {
                                    println "Vaga ${decisao} adicionada à fila."
                                } else {
                                    println "Vaga ${decisao} já está na fila!"
                                }
                            } else {
                                println "Identificador [${decisao}] não encontrado."
                            }
                            println "Próximo ID ou 'FIM':"
                            decisao = scan.nextLine().trim().toUpperCase()
                        }

                        vagasParaEditar.forEach {identificador->
                            println("--------------------------------------------")
                            println("Edicão da vaga de numero ${identificador}")

                            if (Utilidades.confirmarAcaoEmTerminal(" Deseja deletar a vaga?", scan)){


                                gbd.removerVaga(identificador as Integer)

                                return


                            }

                            if (Utilidades.confirmarAcaoEmTerminal("o Nome atual da vaga é ${mapaDeVaga[identificador].nome} , deseja altera-lo?", scan)) {
                                println("Digite o novo nome da vaga")
                                String nome = scan.nextLine()


                                gbd.trocarNomeDaVaga(identificador as Integer, nome)


                            }

                            if (Utilidades.confirmarAcaoEmTerminal("a descrição atual da vaga é ${mapaDeVaga[identificador].descricao} , deseja altera-la?", scan)) {
                                println("Digite a nova descricao da vaga")
                                String desc = scan.nextLine()


                                gbd.trocarDescricaoDaVaga(identificador as Integer, desc)


                            }

                        }

                    }

                    else{
                        println("Você não possui vagas cadastradas")
                    }

                break



                case "4":

                    List<GroovyRowResult> listagemMatchs = gbd.buscarMatches(cnpj)

                    if(listagemMatchs){
                        println("Os seguintes candidatos possuem um match registardo com sua empresa")
                        println("---------------------------")

                        for (match in listagemMatchs){

                            println("Nome do candidato:${match.nome}")
                            println("Descricao do candidato:${match.descricao}")
                            println("Email de contato:${match.email}")
                            println("idade:${match.idade}")
                            println("Estado de atuação:${match.estado}")
                            println("Identificador da vaga onde ocorreu o match:${match.vaga}")
                            println("---------------------------")

                        }
                    }

                    else{

                        println("Não foram encontrados matchs realizados para seu perfil")

                    }



                    break

                case "5":
                    TerminalFiltro.filtragem(cnpj,gbd,scan)
                    break



                case "6":

                    TerminalEdicaoEmpresa.edicaoEmpresa(cnpj,gbd)
                    break

                case "7":

                    println("Essa ação nao poderá ser desfeita, se tiver certeza, digite seu cnpj para que seu perfil seja deletado, qualquer outra entrada retornara ")
                    String checkDelet=scan.nextLine()

                    if(checkDelet==cnpj){
                        gbd.removerPerfil(cnpj)

                        System.exit(0)

                    }


                    break


            }

            println("digite 1 para registrar uma vaga, 2 para checar suas vagas , 3 editar vagas , 4 para visualizar matchs, 5 para visualizar candidatos, 6 para editar perfil ,7 para exclui-lo  ou 8 para sair")
            acao = scan.nextLine()




        }
    }

}
