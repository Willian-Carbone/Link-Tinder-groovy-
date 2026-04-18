import GerenciadoresDeBanco.BuscadoresDeInformacao.CheckerDadoRegistradoI
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCnpj
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCpf
import GerenciadoresDeBanco.Conectores.ConectorBancoPostgreBase
import Modulos.ConversoresEntrada.RemovedorNaoDigitos

import Modulos.GerenciadoresTerminal.RequisidorDeEntradas
import Terminais.Cadastros.CadastroCandidato
import Terminais.Cadastros.CadastroEmpresa
import Terminais.Cadastros.TerminalCadastro
import Terminais.Interacao.TerminalCandidato
import Terminais.Interacao.TerminalEmpresa
import Terminais.Interacao.TerminalInterativo
import groovy.sql.Sql


Sql conexao = ConectorBancoPostgreBase.getConexao()

ArrayList<String> opcoesDeEscolha = ["1", "2"]

Scanner scan = new Scanner(System.in)


String servico = RequisidorDeEntradas.solicitarOpcao(scan, "Digite o serviço desejado, 1 para cadastro ou 2 para login", opcoesDeEscolha)




switch (servico) {

    case "1":

        TerminalCadastro terminal

        String cadastroSelecionado = RequisidorDeEntradas.solicitarOpcao(scan, "Digite 1 para cadastrar uma empresa ou 2 para cadastrar um candidato", opcoesDeEscolha)


        if (cadastroSelecionado == "1") {
            terminal = new CadastroEmpresa()

        } else {

            terminal = new CadastroCandidato()
        }

        terminal.cadastrar(conexao, scan)

        break


    case "2":

        TerminalInterativo terminal

        println("Informe o identificador para login")


        RemovedorNaoDigitos cleaner = new RemovedorNaoDigitos()


        CheckerDadoRegistradoI checarParaCpf= new ConfirmadorExistenciaCpf(conexao)
        CheckerDadoRegistradoI cheacarParaCnpj = new ConfirmadorExistenciaCnpj(conexao)



        String identificador = cleaner.converterDado(scan.nextLine())


        while (!checarParaCpf.buscarExistenciaDado(identificador) && !cheacarParaCnpj.buscarExistenciaDado(identificador)) {
            println("Não a nenhuma conta com o identificador informado, tente outro")
            identificador = cleaner.converterDado(scan.nextLine())

        }


        if (identificador.size() > 11) {
            terminal = new TerminalEmpresa()
        } else {

            terminal = new TerminalCandidato()
        }

        terminal.navegar(identificador,conexao,scan)


}


conexao.close()
scan.close()

