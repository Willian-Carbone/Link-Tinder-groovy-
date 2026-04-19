package View.Requisitores


import Model.Enuns.Especialidades

import Model.validadoresDeEntradas.*
import Daos.*
import Utilits.ConversorDados.ConversorDadoI
import groovy.sql.Sql
import Model.Objetos.*
import Daos.BuscadoresDeInformacao.*



class RequisidorDeEntradas {


    static String solicitarOpcao(Scanner scan, String mensagem, List<String> opcoes) {
        println(mensagem)
        String entrada = scan.nextLine().toUpperCase()

        while (!(entrada in opcoes)) {
            println("Entrada inválida. Opções permitidas: ${opcoes.join(', ')}")
            println("Tente novamente:")
            entrada = scan.nextLine().toUpperCase()
        }

        return entrada.toUpperCase()
    }


    static String solicitarDadoBasicoValido(String nomeDoDado, Scanner scan, ValidadadorI validadorDado, ConversorDadoI conversor=null){
        println("Insira o dado ${nomeDoDado}")

        def processarEntrada = {
            String entrada = scan.nextLine()
            return conversor ? conversor.converterDado(entrada) : entrada
        }



        String dado = processarEntrada()


        while(!validadorDado.validarDado(dado)){
            println("Informe um valor válido")
            dado=processarEntrada()

        }

        return dado
    }



    static String solicitarDescricao(Scanner scan) {
        println("Informe uma breve descrição ")
        String desc = scan.nextLine()
        return desc
    }



    static ArrayList<Especialidades> solicitarConjuntoEspecialidadesValidas(Scanner scan) {

        ArrayList<Especialidades> competencias = new ArrayList<>()


        println("informe o codigo das habilidades que deseja da lista abaixo , ao menos uma deve ser selecionada ")

        Especialidades.values().each {

            println("Codigo da habilidade : ${it.name()} Habilidade: ${it.getValor()}")
        }


        String especialidade = scan.nextLine().toUpperCase()


        while (especialidade != "FIM" || competencias.size() == 0) {


            if (Especialidades.any {it.name()==especialidade}) {

                Especialidades espec = Especialidades.valueOf(especialidade)
                if (!competencias.contains(espec)) {
                    competencias.add(espec)
                }
                especialidade = scan.nextLine().toUpperCase()
            } else {

                println("insira um valor válido")
                especialidade = scan.nextLine().toUpperCase()

            }


        }

        return competencias
    }


    static String solicitarPais(Scanner scan) {
        println("Insira o pais de atuação da empresa")
        String pais = scan.nextLine()
        return pais
    }

    static void darOpcaoDeCurtirVagas(Scanner scan, ArrayList<Integer> vagasId, CurtidorI curtidor, String cpf) {
        println("Digite fim para sair ou o numero do identificador da vaga para curti-la")
        String opcaoDigitada = scan.nextLine().toUpperCase()

        while (opcaoDigitada != "FIM") {

            try {
                Integer idDigitado = opcaoDigitada.toInteger()

                if (idDigitado in vagasId) {


                    Integer idGeradopelaCurtida = curtidor.curtir(new Curtida(cpf,idDigitado))

                    if (idGeradopelaCurtida) {

                        println("Curtida enviada para a vaga : ${idDigitado}")


                    } else {
                        println("Não é possivel curtir a vaga , tente outra")
                    }


                } else {
                    println("ID ${idDigitado} não está na lista de vagas mostradas.")
                }

            }


            catch (Exception ignored) {
                println("Entrada inválida! Digite um número ou 'FIM'.")

            }

            println("\nPróximo ID (ou FIM): ")
            opcaoDigitada = scan.nextLine().toUpperCase()
        }
    }





    static String solicitarCredencialValida(Scanner scan, ValidadadorI validadorFormato, CheckerDadoRegistradoI verificarSeEmUso, String tipoDado, ConversorDadoI conversor = null) {

        println("Insira o ${tipoDado} do perfil")

        def  processarEntrada ={
            String entrada = scan.nextLine()
            return conversor?.converterDado(entrada) ?: entrada
        }


        String possivelCredencial = processarEntrada()


        while (!validadorFormato.validarDado(possivelCredencial) || verificarSeEmUso.buscarExistenciaDado(possivelCredencial)) {
            println("O  ${tipoDado} informado não esta válido para cadastro, tente novamente com outro")
            possivelCredencial = processarEntrada()

        }

       return possivelCredencial
    }



    static boolean solicitarRespostaRemocaoPerfil(String identificador, Scanner scan){
        println("Essa ação nao poderá ser desfeita, se tiver certeza, digite seu identificador para que seu perfil seja deletado, qualquer outra entrada retornara ")
        String checkDelet = scan.nextLine()

        if (checkDelet == identificador) {
           return  true

        }
        return false
    }


    static void cederOpcaoCurtirCandidatoDevolta(Scanner scan, Map <String ,Map> candidatosQueCurtiramVaga, Sql conexao, String cnpj){

        println("Digite o numero do candidato para curti-lo devolta ou fim para sair")
        String decisao = scan.nextLine().toUpperCase()

        while (decisao != "FIM") {
            if (candidatosQueCurtiramVaga.containsKey(decisao)) {
                Map dadosParaMatch = candidatosQueCurtiramVaga[decisao]

                new GerenciadorEmpresa(conexao).criarMatch(new Match(dadosParaMatch["cpf"] as String,cnpj, dadosParaMatch["vagaId"] as Integer))

                println("Match realizado com sucesso para o candidato ${decisao}!")

                candidatosQueCurtiramVaga.remove(decisao)

            } else {
                println("Identificador não encontrado ou já processado.")

            }

            println("Digite outro ID ou 'FIM' para sair:")
            decisao = scan.nextLine().toUpperCase()
        }


    }


    static List<String> escolherVagasParaEditar(Scanner scan,Map<String,Map> infoDasVagas){

        List<String> vagasParaEditar=[]

        println("Digite o numero da vaga para coloca-la na fila de edição  ou fim para sair")
        String decisao = scan.nextLine().toUpperCase()


        while (decisao != "FIM") {
            if (infoDasVagas.containsKey(decisao)) {
                if (vagasParaEditar.add(decisao)) {
                    println "Vaga ${decisao} adicionada à fila."
                } else {
                    println "Vaga ${decisao} já está na fila!"
                }
            } else {
                println "Identificador [${decisao}] não encontrado."
            }
            println "Próximo ID ou 'FIM':"
            decisao = scan.nextLine().trim().toUpperCase()
        }
        return vagasParaEditar


    }

}
