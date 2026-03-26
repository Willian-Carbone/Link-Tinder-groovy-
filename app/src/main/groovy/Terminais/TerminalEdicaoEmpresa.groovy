package Terminais
import Metodos.GerenciadorBancoDados
import Metodos.Utilidades
import groovy.sql.GroovyRowResult

class TerminalEdicaoEmpresa {

    static edicaoEmpresa(String cnpj,GerenciadorBancoDados gbd) {

        GroovyRowResult infosUsuario = gbd.capturarInfos(cnpj)
        Integer idUsuario = gbd.capturarIdUsurio(cnpj)
        Scanner scan = new Scanner(System.in)

        TerminalEdicaoBase.edicao(idUsuario, infosUsuario, gbd, scan)

        if (Utilidades.confirmacao("O pais do seu perfil é ${infosUsuario.pais} , deseja altera-lo?", scan)) {
            println("Digite o novo pais do seu perfil ")
            String pais = scan.nextLine()

            gbd.trocarPaisDaEmpresa(cnpj,pais)


        }


    }

}














