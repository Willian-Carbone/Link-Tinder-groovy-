package Terminais

import Metodos.FileManager

class Terminal_candidato {
    static void terminal_principal(String user_id){
        List lista_vagas = FileManager.listagem_vagas(user_id)
        Scanner scan= new Scanner(System.in)

        println("Escolha 1 para ver vagas ou 2 para ver seus matchs")
        String escolha = scan.nextLine()

        while(escolha!="1" && escolha!="2"){
            println("insira um valor valido")
            escolha=scan.nextLine()
        }

        switch(escolha){
            case"1":
                ArrayList <String> empresas_contratantes = new ArrayList<>()

                for (vaga in lista_vagas){
                    println("""              Nome:${vaga.nome}
                       Contratante:${vaga.contratante}
                       Descriçao da vaga:${vaga.descricao}
                       Requisitos da vaga:${vaga.requisitos}
                       -------------------------------------""")

                    empresas_contratantes.add(vaga.contratante)
                }

                println("Digite fim para sair ou o numero do contratante para curti-lo")
                String entrada= scan.nextLine().toUpperCase()

                while (entrada!="FIM"){
                    if (entrada in empresas_contratantes) {
                        println("Objetos.Curtida enviada a empresa ${entrada}")
                        FileManager.registrar_curtida(entrada, user_id)
                        entrada=scan.nextLine().toUpperCase()
                    }

                    else{
                        println("Insira um identificador válido")
                        entrada=scan.nextLine()toUpperCase()
                    }

                }
                break

            case "2":
                List <String> matchs_feitos = FileManager.checar_matches(user_id)

                for (match in matchs_feitos){
                    println(match)
                }


        }



    }
}
