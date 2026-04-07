
import Metodos.*
import Terminais.*



GerenciadorBancoDados gerenciadorBanco = new GerenciadorBancoDados()
ArrayList<String> opcoesDeEscolha =["1","2"]


Scanner scan = new Scanner(System.in)


String servico= ControladorTerminal.solicitarOpcao(scan,"Digite o serviço desejado, 1 para cadastro ou 2 para login",opcoesDeEscolha)


switch (servico) {

    case "1":

        String cadastroSelecionado = ControladorTerminal.solicitarOpcao(scan,"Digite 1 para cadastrar uma empresa ou 2 para cadastrar um candidato",opcoesDeEscolha)


        if (cadastroSelecionado == "1") {
            CadastroEmpresa.terminalEmpresa(gerenciadorBanco,scan)

        } else {

            CadastroCandidato.terminalCandidatos(gerenciadorBanco,scan)
        }

        break


    case "2":

        String identificador = ControladorTerminal.solicitarIdentificadorParaLogin(scan,gerenciadorBanco)


         if (Utilidades.validadorCnpj(identificador)){
             TerminalEmpresa.terminalPrincipal(Utilidades.padronizaInformacoesParaBancoDados(identificador),gerenciadorBanco,scan)
         }

        else {
             TerminalCandidato.terminalPrincipal(Utilidades.padronizaInformacoesParaBancoDados(identificador),gerenciadorBanco,scan)
         }


        scan.close()
}

