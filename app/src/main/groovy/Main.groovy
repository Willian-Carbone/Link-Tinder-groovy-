import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCnpj
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCpf
import GerenciadoresDeBanco.Conectores.ConectorBancoBasico
import GerenciadoresDeBanco.Conectores.CriadorConexao
import Modulos.ConversoresEntrada.RemovedorNaoDigitos
import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import Terminais.*
import groovy.sql.Sql


CriadorConexao conectorAobanco = new ConectorBancoBasico()

Sql conexao = conectorAobanco.criarconexao()

ArrayList<String> opcoesDeEscolha =["1","2"]


Scanner scan = new Scanner(System.in)


String servico= RequisidorDeEntradas.solicitarOpcao(scan, "Digite o serviço desejado, 1 para cadastro ou 2 para login", opcoesDeEscolha)


switch (servico) {

    case "1":

        String cadastroSelecionado = RequisidorDeEntradas.solicitarOpcao(scan,"Digite 1 para cadastrar uma empresa ou 2 para cadastrar um candidato",opcoesDeEscolha)


        if (cadastroSelecionado == "1") {
            CadastroEmpresa.terminalEmpresa(conexao,scan)

        } else {

            CadastroCandidato.terminalCandidatos(conexao,scan)
        }

        break


    case "2":

        println("Informe o identificador para login")

        ConfirmadorExistenciaCnpj checarParaCnpj= new ConfirmadorExistenciaCnpj(conexao)
        ConfirmadorExistenciaCpf checarParaCpf = new ConfirmadorExistenciaCpf(conexao)

        RemovedorNaoDigitos cleaner= new RemovedorNaoDigitos()


        String identificador = cleaner.converterDado(scan.nextLine())

        while(!checarParaCnpj.buscarExistenciaDado(identificador) && !checarParaCpf.buscarExistenciaDado(identificador)){
            println("Não a nenhuma conta com o identificador informado, tente outro")
            identificador = cleaner.converterDado(scan.nextLine())

        }



         if (identificador.size()>11){
             TerminalEmpresa.terminalPrincipal(identificador,conexao,scan)
         }

        else {
             TerminalCandidato.terminalPrincipal(identificador,conexao,scan)
         }

        conexao.close()
        scan.close()
}

