package Metodos

import Enuns.Especialidades
import Enuns.Estados
import groovy.sql.GroovyRowResult

class ControladorTerminal {


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


    static String solicitarIdentificadorParaLogin(Scanner scan, GerenciadorBancoDados gerenciadorBanco) {
        println("Informe seu cpf para logar como empresa ou cnpj para logar como candidato")
        String identificador = scan.nextLine()

        while (!verificarSeLoginValido(identificador, gerenciadorBanco)) {
            println("O identificador  não é valido para login, tente novamente informando outra entrada")
            identificador = scan.nextLine()
        }

        return identificador
    }


    private static boolean verificarSeLoginValido(String id, GerenciadorBancoDados gerenciadorBanco) {
        String idLimpo = Utilidades.padronizaInformacoesParaBancoDados(id)

        boolean formatoValido = Utilidades.validadorCpf(id) || Utilidades.validadorCnpj(id)
        boolean existeNoBanco = gerenciadorBanco.cnpjEmUso(idLimpo) || gerenciadorBanco.cpfEmUso(idLimpo)

        return formatoValido && existeNoBanco
    }


    static String solicitarNomeValido(Scanner scan) {
        println("Digite o nome")

        String nome = scan.nextLine()

        while (!Utilidades.validadorNome(nome)) {
            println("Digite um nome  válido")
            nome = scan.nextLine()
        }

        return nome
    }

    static String solicitarCpfValido(Scanner scan, GerenciadorBancoDados gerenciadorBancoDados) {

        println("Digite o cpf do candidato")
        String possivelCpf = scan.nextLine()

        while (!Utilidades.validadorCpf(possivelCpf) || gerenciadorBancoDados.cpfEmUso(Utilidades.padronizaInformacoesParaBancoDados(possivelCpf))) {
            println("O  cpf informado não esta válido para cadastro, tente novamente com outro")
            possivelCpf = scan.nextLine()
        }

        String cpfConfirmadoFormatado = Utilidades.padronizaInformacoesParaBancoDados(possivelCpf)
        return cpfConfirmadoFormatado
    }


    static String solicitarEmailValido(Scanner scan, GerenciadorBancoDados gerenciadorBancoDados) {
        println("Digite o email do usuario")
        String email = scan.nextLine()

        while (!Utilidades.validadorEmail(email) || gerenciadorBancoDados.emailEmUso(email)) {
            println("Insira um email válido que não esteja em uso")
            email = scan.nextLine()
        }
        return email
    }

    static String solicitarDescricao(Scanner scan) {
        println("Informe uma breve descrição ")
        String desc = scan.nextLine()
        return desc
    }


    static String solicitarIdadeValida(Scanner scan) {
        println("Informe a idade do candidato")
        String idade = scan.nextLine()
        while (!Utilidades.verificacarMaiorIdade(idade)) {
            println("Insira uma idade válida e maior que 18 anos")
            idade = scan.nextLine()
        }
        return idade
    }

    static String solicitarCepValido(Scanner scan) {
        println("Insira o cep do usuario")
        String cep = scan.nextLine()


        while (!Utilidades.validadorCep(cep)) {
            println("Insira um cep válido")
            cep = scan.nextLine()
        }

        String cepPadronizado = Utilidades.padronizaInformacoesParaBancoDados(cep)

        return cepPadronizado
    }


    static Estados solicitarEstadoValido(Scanner scan) {

        println("insira o estado de atuação")

        String possivelNomeEstado = scan.nextLine()

        while (!Utilidades.capturarEstado(possivelNomeEstado)) {
            println("Insira um estado existente")
            possivelNomeEstado = scan.nextLine()
        }

        Estados estadoConfirmado = Estados.valueOf(Utilidades.capturarEstado(possivelNomeEstado))


        return estadoConfirmado

    }

    static ArrayList<Especialidades> solicitarConjuntoEspecialidadesValidas(Scanner scan) {

        ArrayList<Especialidades> competencias = new ArrayList<>()



        println("informe o codigo das habilidades que deseja da lista abaixo , ao menos uma deve ser selecionada ")

        Especialidades.values().each {

            println("Codigo da habilidade : ${it.name()} Habilidade: ${it.getValor()}")
        }


        String especialidade = scan.nextLine().toUpperCase()


        while (especialidade != "FIM" || competencias.size() == 0) {


            if (Utilidades.checarSeCompetenciaExiste(especialidade)) {

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

    static String solicitarCnpjValido(Scanner scan, GerenciadorBancoDados gerenciadorBancoDados) {

        println("Insira o cnpj da empresa")

        String possivelCnpj = scan.nextLine()

        while (!Utilidades.validadorCnpj(possivelCnpj) || gerenciadorBancoDados.cnpjEmUso(Utilidades.padronizaInformacoesParaBancoDados(possivelCnpj))) {
            println("O  cpf informado não esta válido para cadastro, tente novamente com outro")
            possivelCnpj = scan.nextLine()
        }

        String cnpjConfirmadoFormatado = Utilidades.padronizaInformacoesParaBancoDados(possivelCnpj)
        return cnpjConfirmadoFormatado
    }


    static ArrayList<Integer> exibirVagasParaCandidatos(List<GroovyRowResult> vagas) {

        ArrayList<Integer> identificadoresVagas = new ArrayList<>()

        if (vagas) {

            for (vaga in vagas) {
                println("""
                       Nome:${vaga.nome}
                       Identificador da vaga:${vaga.id_vaga}
                       Identificador do contratante anonimo:${vaga.id_contratante}
                       Descriçao da vaga:${vaga.descricao}
                       Requisitos da vaga:${vaga.competencias}
                       -------------------------------------""")

                identificadoresVagas.add(vaga.id_vaga as Integer)
            }
        } else {
            println("Não foram encontradas Vagas para você")
        }
        return identificadoresVagas
    }


    static void darOpcaoDeCurtirVagas(Scanner scan, ArrayList<Integer> vagasId, GerenciadorBancoDados gerenciadorBancoDados, String cpf) {
        println("Digite fim para sair ou o numero do identificador da vaga para curti-la")
        String opcaoDigitada = scan.nextLine().toUpperCase()

        while (opcaoDigitada != "FIM") {

            try {
                Integer idDigitado = opcaoDigitada.toInteger()

                if (idDigitado in vagasId) {

                    Integer idGeradopelaCurtida = gerenciadorBancoDados.registrarCurtidaVaga(cpf, idDigitado)

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


    static void exibirMatchsParaCandidato(List<GroovyRowResult> matchs) {
        if (matchs) {
            println("As seguintes empresas curtiram seu perfil")
            println("---------------------------")

            for (match in matchs) {

                println("Nome da empresa:${match.nome}")
                println("Descricao da empresa:${match.descricao}")
                println("Email de contato:${match.email}")
                println("Pais de atuação:${match.pais}")
                println("Estado de atuação:${match.estado}")
                println("Identificador da vaga onde ocorreu o match:${match.vaga}")
                println("---------------------------")

            }
        }
    }


    static void exibirVagaParaEmpresas(GroovyRowResult dadosVaga) {

                println("*************************************")
                println("Numero da Vaga:${dadosVaga.id}")
                println("Nome da Vaga: ${dadosVaga.nome}")
                println("Descrição da vaga:${dadosVaga.descricao}")
                println("_________________________________________")


    }

    static void imprimirCandidatosInteressados(List<Map> candidatosQueCurtiramVaga){

        println("Candidatos que curtiram a vaga e suas habilidades")

        candidatosQueCurtiramVaga.forEach {
            candidato ->
                println("Numero do Candidato: ${candidato.id}")
                println("Habilidades : ${candidato.competencias}")
                println("_______________________________________")


        }

    }

    static Map<String, Map> capturarCandidatosEVagasQueCurtiram(String cnpj,GerenciadorBancoDados gerenciadorBancoDados){

        List<GroovyRowResult> vagas = gerenciadorBancoDados.capturarVagasDaEmpresa(cnpj)

        Map<String, Map> relacaoCandidatoVagaCurtida = [:]

        vagas.each {GroovyRowResult vaga->

            List<Map> candidatosCurtiramVaga = gerenciadorBancoDados.capturarCandidatosInteressados(vaga.id as Integer,cnpj)


            candidatosCurtiramVaga.forEach {
                candidato->

                    relacaoCandidatoVagaCurtida[(candidato.id as String)] = [
                            cpf: candidato.cpf,
                            vagaId: vaga.id as Integer
                    ]
            }

        }

        return relacaoCandidatoVagaCurtida


    }


    static void cederOpcaoCurtirCandidatoDevolta(Scanner scan, Map <String ,Map> candidatosQueCurtiramVaga, GerenciadorBancoDados gerenciadorBancoDados, String cnpj){

        println("Digite o numero do candidato para curti-lo devolta ou fim para sair")
        String decisao = scan.nextLine().toUpperCase()

        while (decisao != "FIM") {
            if (candidatosQueCurtiramVaga.containsKey(decisao)) {
                Map dadosParaMatch = candidatosQueCurtiramVaga[decisao]

                gerenciadorBancoDados.registrarMatch(dadosParaMatch.cpf as String, cnpj, dadosParaMatch.vagaId as Integer)

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

    static void exibirMatchsParaEmpresas(List<GroovyRowResult> matchs){
        if (matchs) {
            println("Os seguintes candidatos possuem um match registardo com sua empresa")
            println("---------------------------")

            for (match in matchs) {

                println("Nome do candidato:${match.nome}")
                println("Descricao do candidato:${match.descricao}")
                println("Email de contato:${match.email}")
                println("idade:${match.idade}")
                println("Estado de atuação:${match.estado}")
                println("Identificador da vaga onde ocorreu o match:${match.vaga}")
                println("---------------------------")

            }
        } else {

            println("Não foram encontrados matchs realizados para seu perfil")

        }

    }

    static void solicitarRespostaRemocaoPerfil(String identificador,Scanner scan,GerenciadorBancoDados gerenciadorBancoDados){
        println("Essa ação nao poderá ser desfeita, se tiver certeza, digite seu identificador para que seu perfil seja deletado, qualquer outra entrada retornara ")
        String checkDelet = scan.nextLine()

        if (checkDelet == identificador) {
            gerenciadorBancoDados.removerPerfil(identificador)

            System.exit(0)

        }
    }



}


