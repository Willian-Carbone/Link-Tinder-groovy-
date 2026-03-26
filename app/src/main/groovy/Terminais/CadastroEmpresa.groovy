package Terminais

import Enuns.*
import Metodos.*
import Objetos.Empresa

class CadastroEmpresa {

    static void terminalEmpresa (GerenciadorBancoDados gbd){



        println("Digite o nome da empresa")
        Scanner scan = new Scanner(System.in)
        String nome = scan.nextLine()

        while(!Utilidades.validadorNome(nome)){
            println("Digite um nome de usuario válido")
            nome = scan.nextLine()
        }

        println("Digite o CNPJ da empresa")
        String entradaCnpj =scan.nextLine()

        while ( !Utilidades.validadorCnpj(entradaCnpj) || gbd.cnpjEmUso(entradaCnpj)){
            println("O cnpj informado não esta disponivel para cadastro ,tente novamente com outro")
            entradaCnpj=scan.nextLine()
        }

        String cnpjPadronizado= Utilidades.padronizaEntrada(entradaCnpj)

        println("Digite o email comercial da empresa")
        String email = scan.nextLine()

        while (!Utilidades.validadorEmail(email) || gbd.emailEmUso(email)){
            println("Insira um email válido que não estaja sendo usado")
            email = scan.nextLine()
        }
        println("Informe uma breve descrição da empresa")
        String desc= scan.nextLine()

        println("Insira o país de atuação da empresa")
        String pais = scan.nextLine()

        println("Insira o cep da empresa")
        String cep = scan.nextLine()

        while (!Utilidades.validadorCep(cep)){
            println ("Insira um cep válido")
            cep=scan.nextLine()
        }

        String cepPadronizado = Utilidades.padronizaEntrada(cep)


        println("insira o estado de atuação")
        String possivelEstado = scan.nextLine()

        while(Utilidades.normalizador(possivelEstado)==null) {
            println("Insira um estado existente")
            possivelEstado = scan.next()
        }


        Estados estadoConfirmado = Estados.valueOf(Utilidades.normalizador(possivelEstado))




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

        println("Empresa cadastrada com sucesso")

        Empresa cadastrado = new Empresa (nome,cnpjPadronizado,pais,cepPadronizado,email,estadoConfirmado,desc,competencias)

        gbd.registrarEmpresa(cadastrado)







    }

}
