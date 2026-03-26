
import Metodos.*
import Terminais.*




GerenciadorBancoDados gc = new GerenciadorBancoDados()


println("Digite o serviço desejado, 1 para cadastro , 2 para login")
Scanner scan = new Scanner(System.in)
String entrada = scan.nextLine()

while (entrada != "1" && entrada != "2") {
    println("Insira um valor válido")
    entrada = scan.nextLine()
}

switch (entrada) {

    case "1":
        println("Deseja cadastrar uma empresa ou candidato? , 1 para empresa ou 2 candidato")
        entrada = scan.nextLine()

        while (entrada != "1" && entrada != "2") {
            println("Insira um valor válido")
            entrada = scan.nextLine()
        }


        if (entrada == "1") {
            CadastroEmpresa.terminalEmpresa(gc)

        } else {

            CadastroCandidato.terminalCandidatos(gc)
        }


        break


    case "2":
        println("Informe seu cpf para logar como empresa ou cnpj para logar como candidato")
        String entradaId = scan.nextLine()

        while ((!Utilidades.validadorCpf(entradaId) && !Utilidades.validadorCnpj(entradaId)) || (!gc.cnpjEmUso(Utilidades.padronizaEntrada(entradaId)) && !gc.cpfEmUso(Utilidades.padronizaEntrada(entradaId)))) {
            println("A entrada informada não é valida para login, tente novamente informando outra entrada")
            entradaId = scan.nextLine()
        }


         if (entradaId.length()!=11){
             TerminalEmpresa.terminalPrincipal(Utilidades.padronizaEntrada(entradaId),gc)
         }

        else (TerminalCandidato.terminalPrincipal(Utilidades.padronizaEntrada(entradaId),gc))


        scan.close()
}
2
