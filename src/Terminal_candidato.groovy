

class Terminal_candidato {
    static void terminal_principal(String user_id){
        List lista_vagas = FileManager.listagem_vagas(user_id)
        Scanner scan= new Scanner(System.in)

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
                println("Curtida enviada a empresa ${entrada}")
                FileManager.registrar_curtida(entrada, user_id)
                entrada=scan.nextLine().toUpperCase()
            }

            else{
                println("Insira um identificador válido")
                entrada=scan.nextLine()toUpperCase()
            }

        }



    }
}
