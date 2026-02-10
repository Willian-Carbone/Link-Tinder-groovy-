static void main(String[] args) {

    println("Digite o serviço desejado, 1 para cadastro ,2 para listagem")
    Scanner scan = new Scanner(System.in)
    String entrada=scan.nextLine()

    while (entrada != "1" && entrada !="2" ){
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
                println(item)
            }

    }

    scan.close()
}
