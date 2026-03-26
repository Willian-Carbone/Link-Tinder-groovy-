package Terminais

import Metodos.GerenciadorBancoDados
import Metodos.Utilidades
import groovy.sql.GroovyRowResult


class TerminalEdicaoCandidato {

    static edicaoCandidato(String cpf,GerenciadorBancoDados gbd){

        GroovyRowResult infosUsuario = gbd.capturarInfos(cpf)
        Integer idUsuario = gbd.capturarIdUsurio(cpf)
        Scanner scan = new Scanner(System.in)

        TerminalEdicaoBase.edicao(idUsuario,infosUsuario,gbd,scan)

        if (Utilidades.confirmacao("A idade do seu perfil é ${infosUsuario.idade} , deseja altera-la?", scan)) {
            println("Digite a nova idade do seu perfil ")
            String idade = scan.nextLine()

            while (!Utilidades.validadorIdade(idade)){
                println("Insira uma idade válida e maior que 18 anos")
                idade=scan.nextLine()
            }

            gbd.trocarIdadeDoCandidato(cpf, Integer.parseInt(idade))
        }






    }









}
