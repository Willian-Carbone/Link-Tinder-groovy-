static void main(String[] args) {

    println("Digite o serviço desejado, 1 para cadastro ,2 para listagem, 3 para login")
    Scanner scan = new Scanner(System.in)
    String entrada=scan.nextLine()

    while (entrada != "1" && entrada !="2" && entrada !="3" ){
        println("Insira um valor válido")
        entrada=scan.nextLine()
    }

    switch (entrada){

        case "1":
            println("Deseja cadastrar uma empresa ou candidato? , 1 para empresa ou 2 candidato")
            entrada= scan.nextLine()

            while (entrada != "1" && entrada !="2" ){
                println("Insira um valor válido")
                entrada=scan.nextLine()
            }

            // terminal dividido em 2 subterminais

            if (entrada=="1"){
                Cadastro_empresa.terminal_empresas()

            }
            else

                Cadastro_candidato.terminal_candidatos()


            break


        case "2" :




            println("Digite 1 para listar as empresas cadastradas ou 2 para listar os candidatos")

            String escolha_listagem = scan.nextLine()
            while (escolha_listagem != "1" && escolha_listagem!="2" ){
                println("Insira um valor válido")
                escolha_listagem=scan.nextLine()
            }

            def lista


            if (escolha_listagem=="2")

            {lista = FileManager.listagem("Candidato")}

            else
            { lista = FileManager.listagem("Empresa")}

            for (item in lista){

                println("Nome : $item.nome | Descrição: $item.descricao | Estado atuação:$item.estado | Competencias :$item.competencias  " )
            }

            break

        case "3":
            println("Informe seu cpf para logar como empresa ou cnpj para logar como candidato")
            String entrada_id = scan.nextLine()

            while ((!Metodos.validador_cpf(entrada_id) && !Metodos.validador_cnpj(entrada_id)) || (!FileManager.cnpj_em_uso(Metodos.padronizar_entrada(entrada_id)) && !FileManager.cpf_em_uso(Metodos.padronizar_entrada(entrada_id)))){
                println("Insira um valor válido")
                entrada_id = scan.nextLine()
            }

            //repartiçao em 2 terminais


            if (entrada_id.length()!=11){
                Terminal_empresa.terminal_principal(entrada_id)
            }

            else (Terminal_candidato.terminal_principal(entrada_id))


    }

    scan.close()
}
