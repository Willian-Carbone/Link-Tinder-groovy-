package Terminais

import Enuns.Especialidades
import Enuns.Estados
import Metodos.FileManager
import Metodos.Metodos
import Objetos.Empresa

class Cadastro_empresa {

    static void terminal_empresas (){

        println("Digite o nome da empresa")
        Scanner scan = new Scanner(System.in)
        String nome = scan.nextLine()

        println("Digite o CNPJ da empresa")
        String entrada_cnpj =scan.nextLine()

        while ( !Metodos.validador_cnpj(entrada_cnpj) || FileManager.cnpj_em_uso(Metodos.padronizar_entrada(entrada_cnpj))){
            println("Insira um  cnpj válido")
            entrada_cnpj=scan.nextLine()
        }

        String cnpj_padronizado= Metodos.padronizar_entrada(entrada_cnpj)

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

        String cep_padronizado = Metodos.padronizar_entrada(cep)


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

        println("Objetos.Empresa cadastrada com sucesso")

        Empresa cadastrado = new Empresa (nome,cnpj_padronizado,pais,cep_padronizado,email,estado_confirmado,desc,competencias)

        FileManager.adicionar(cadastrado,"Objetos.Empresa")





    }

}
