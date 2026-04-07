package Metodos

import Enuns.Especialidades
import Enuns.Estados

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
        println("Digite o nome do usuario")

        String nome = scan.nextLine()

        while (!Utilidades.validadorNome(nome)) {
            println("Digite um nome de usuario válido")
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
        println("Informe uma breve descrição para o usuario")
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

    static ArrayList<Especialidades> solicitarConjuntoEspecialidadesValidas(Scanner scan){

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


            if (Utilidades.checarSeCompetenciaExiste(especialidade)){

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

        return competencias
    }

    static String solicitarPais(Scanner scan){
        println("Insira o pais de atuação da empresa")
        String pais =scan.nextLine()
        return pais
    }

    static String solicitarCnpjValido(Scanner scan,GerenciadorBancoDados gerenciadorBancoDados){

        println("Insira o cnpj da empresa")

        String possivelCnpj = scan.nextLine()

        while (!Utilidades.validadorCnpj(possivelCnpj) || gerenciadorBancoDados.cnpjEmUso(Utilidades.padronizaInformacoesParaBancoDados(possivelCnpj))) {
            println("O  cpf informado não esta válido para cadastro, tente novamente com outro")
            possivelCnpj = scan.nextLine()
        }

        String cnpjConfirmadoFormatado = Utilidades.padronizaInformacoesParaBancoDados(possivelCnpj)
        return cnpjConfirmadoFormatado
    }




}
