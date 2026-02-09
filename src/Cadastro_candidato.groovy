class Cadastro_candidato {

    static void terminal_candidatos (){
        println("Digite o nome do candidato")
        Scanner scan = new Scanner(System.in)
        String nome = scan.nextLine()

        println("Digite o cpf do candidato")
        String cpf =scan.nextLine()

        while ( !Metodos.validador_cpf(cpf)){
            println("Insira um formato de cpf válido")
            cpf=scan.nextLine()
        }

        println("Digite o email do candidato")
        String email = scan.nextLine()

        while (!Metodos.validador_email(email)){
            println("Insira um email válido")
            email = scan.nextLine()
        }
        println("Informe uma breve descrição do candidato")
        String desc= scan.nextLine()

        println("Informe a idade do candidato")
        String idade = scan.nextLine()
        while (!Metodos.validador_idade(idade)){
            println("Insira uma idade válida e maior que 18 anos")
            idade=scan.nextLine()
        }

        println("Insira o cep do candidato")
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

        println("informe as habilidades que o candidato possui da lista abaixo ao menos uma deve ser selecionada")

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

        println("Candidato cadastrado com sucesso")

        Candidato cadastrado = new Candidato (nome,cpf,Integer.parseInt(idade),cep,email,estado_confirmado,desc,competencias)



    }
}
