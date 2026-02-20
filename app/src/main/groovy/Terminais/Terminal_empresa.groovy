package Terminais

import Enuns.Especialidades
import Metodos.FileManager
import Metodos.Metodos
import Objetos.Vaga

class Terminal_empresa {
    static void terminal_principal(String user_id) {
        println("Informe a ação 1 para registrar uma vaga 2 para verificar vagas existentes ou 3 para verificar seus matchs ")
        Scanner scan = new Scanner(System.in)

        String acao = scan.nextLine()

        while (acao != "1" && acao != "2" && acao!="3") {
            println("Insira um valor valido")
            acao = scan.nextLine()
        }

        switch (acao) {
            case "1":
                println("informe o nome da vaga")
                String nome = scan.next()

                println("Informe uma descrição para a vaga")
                String desc = scan.next()



                ArrayList<Especialidades> competencias = new ArrayList<>()

                println("informe as habilidades necessárias para a vaga, ao menos 1 deve ser selecionada")

                println ("""
                   PT --> phyton
                   JAV --> java
                   Ang --> Angular
                   Spr --> spring
                   HT--> html
                   Cs --> para css
                   Fim --> saida
                                """)

                String especialidade = scan.nextLine().toUpperCase()


                while (especialidade != "FIM" || competencias.size() == 0){


                    if (Metodos.checar_competencia(especialidade)){

                        Especialidades espec = Especialidades.valueOf(especialidade)
                        if (!competencias.contains(espec)){
                            competencias.add(espec)
                        }
                        especialidade = scan.nextLine().toUpperCase()
                    }

                    else{

                        println("insira um valor válido")
                        especialidade = scan.nextLine().toUpperCase()

                    }


                }

                ArrayList<String> curtidas = new ArrayList<>()

                Vaga vaga = new Vaga(nome:nome,descricao:desc,contratante:user_id,requisitos:competencias, curtidas: curtidas )

                FileManager.adicionar_vaga(vaga)
                println("Vaga adicionada com sucesso")

                break

            case "2":
                List minhas_vagas= FileManager.listagem_vagas(user_id)

                ArrayList<String> id_candidatos = new ArrayList<>()

                if(minhas_vagas) {

                    minhas_vagas.each { vaga ->
                        println("Nome da vaga :${vaga.nome} || Candidatos que curtiram a vaga : ${vaga.curtidas}")
                        def id_vaga_atual = vaga.curtidas?.identificador ?: []
                        id_candidatos.addAll(id_vaga_atual)
                    }

                    println("Digite o identificador do candidato para curti-lo devolta ou fim para sair")
                    String decisao = scan.nextLine()

                    while(decisao != "fim") {

                     if (decisao in id_candidatos){
                         println("Candidato curtido com sucesso")

                         FileManager.salvar_match(user_id, Metodos.descriptografia(decisao))
                         decisao = scan.nextLine()

                     }

                     else{
                         println("identificador nao encontrado")
                         decisao = scan.nextLine()

                     }




                    }


                }


                else{
                    println("Vocẽ nao possui vagas cadastradas")
                }

                break

            case "3":
                List <String> matchs_feitos = FileManager.checar_matches(user_id)

                for (match in matchs_feitos){
                    println(match)
                }

        }

    }

}