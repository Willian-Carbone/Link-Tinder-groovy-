class Cadastro_empresa {

    static void terminal_empresas (){

        println("Digite o nome da empresa")
        Scanner scan = new Scanner(System.in)
        String nome = scan.nextLine()

        println("Digite o CNPJ da empresa")
        String cnpj =scan.nextLine()

        while ( !Metodos.validador_cnpj(cnpj)){
            println("Insira um formato de cnpj válido")
            cnpj=scan.nextLine()
        }

        println("Digite o email comercial da empresa")
        String email = scan.nextLine()

        while (!Metodos.validador_email(email)){
            println("Insira um email válido")
            email = scan.nextLine()
        }
        println("Informe uma breve descrição da empresa")
        String desc= scan.nextLine()

        println("Insira o país de atuação da empresa")
        String pais = scan.nextLine()  // ideia de validação descartada


        println("insira o estado de atuação")
        String possivel_estado = scan.nextLine()

        while(Metodos.normalizador(possivel_estado)==null) {
            println("Insira um estado existente")
            possivel_estado = scan.nextLine()
        }


        Estados estado_confirmado = Estados.valueOf(Metodos.normalizador(possivel_estado))

        print(estado_confirmado.getsigla())






    }

}
