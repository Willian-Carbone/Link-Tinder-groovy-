

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

        println("Insira o cep da empresa")
        String cep = scan.nextLine()

        while (!Metodos.validador_cep(cep)){
            println ("Insira um cep válido")
            cep=scan.nextLine()
        }


        println("insira o estado de atuação")
        String possivel_estado = scan.nextLine()

        while(Metodos.normalizador(possivel_estado)==null) {
            println("Insira um estado existente")
            possivel_estado = scan.next()
        }


        Estados estado_confirmado = Estados.valueOf(Metodos.normalizador(possivel_estado))




        ArrayList<Especialidades> competencias = new ArrayList<>()

        println("informe as habilidades que sua empresa busca em um candidato da lista abaixo ao menos uma deve ser selecionada")

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

        println("Empresa cadastrada com sucesso")

        Empresa cadastrado = new Empresa (nome,cnpj,pais,cep,email,estado_confirmado,desc,competencias)





    }

}
