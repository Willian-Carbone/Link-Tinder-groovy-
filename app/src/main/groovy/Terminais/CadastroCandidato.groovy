package Terminais
import Enuns.*
import Metodos.*
import Objetos.Candidato

class CadastroCandidato {

    static void terminalCandidatos (GerenciadorBancoDados gbd){
        println("Digite o nome do candidato")
        Scanner scan = new Scanner(System.in)
        String nome = scan.nextLine()
        
        while(!Utilidades.validadorNome(nome)){
            println("Digite um nome de usuario válido")
            nome = scan.nextLine()
        }
        

        println("Digite o cpf do candidato")
        String entradaCpf =scan.nextLine()

        while ( !Utilidades.validadorCpf(entradaCpf) || gbd.cpfEmUso(Utilidades.padronizaEntrada(entradaCpf))){
            println("O  cpf informado não esta válido para cadastro, tente novamente com outro")
            entradaCpf=scan.nextLine()
        }

        String cpfFormatado = Utilidades.padronizaEntrada(entradaCpf)

        println("Digite o email do candidato")
        String email = scan.nextLine()

        while (!Utilidades.validadorEmail(email) || gbd.emailEmUso(email)){
            println("Insira um email válido que não esteja em uso")
            email = scan.nextLine()
        }
        println("Informe uma breve descrição do candidato")
        String desc= scan.nextLine()

        println("Informe a idade do candidato")
        String idade = scan.nextLine()
        while (!Utilidades.validadorIdade(idade)){
            println("Insira uma idade válida e maior que 18 anos")
            idade=scan.nextLine()
        }

        println("Insira o cep do candidato")
        String cep = scan.nextLine()


        while (!Utilidades.validadorCep(cep) ){
            println ("Insira um cep válido")
            cep=scan.nextLine()
        }

        String cepPadronizado= Utilidades.padronizaEntrada(cep)



        println("insira o estado de atuação")
        String possivelEstado = scan.nextLine()

        while(!Utilidades.normalizador(possivelEstado)) {
            println("Insira um estado existente")
            possivelEstado = scan.nextLine()
        }


        Estados EstadoConfirmado = Estados.valueOf(Utilidades.normalizador(possivelEstado))




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


            if (Utilidades.checarCompetencia(especialidade)){

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

        Candidato cadastrado = new Candidato (nome,cpfFormatado,Integer.parseInt(idade),email,cepPadronizado,EstadoConfirmado,desc,competencias)

        gbd.registrarCandidato(cadastrado)



    }
}
