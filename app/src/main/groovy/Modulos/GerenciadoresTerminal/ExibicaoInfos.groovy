package Modulos.GerenciadoresTerminal

import GerenciadoresDeBanco.GerenciadorCandidato
import GerenciadoresDeBanco.GerenciadorEmpresa
import GerenciadoresDeBanco.GerenciadorVaga
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class ExibicaoInfos {

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


    static void imprimirCandidatosInteressados(List<GroovyRowResult> candidatosQueCurtiramVaga){

        println("Candidatos que curtiram a vaga e suas habilidades")

        candidatosQueCurtiramVaga.forEach {
            candidato ->
                println("Numero do Candidato: ${candidato.id}")
                println("Habilidades : ${candidato.competencias}")
                println("_______________________________________")


        }

    }

    static Map<String, Map> capturarCandidatosEVagasQueCurtiram(String cnpj, Sql conexao){

        List<GroovyRowResult> vagas = new GerenciadorEmpresa(conexao).buscarVagas(cnpj)

        Map<String, Map> relacaoCandidatoVagaCurtida = [:]

        vagas.each {GroovyRowResult vaga->

            List<GroovyRowResult> candidatosCurtiramVaga = new  GerenciadorVaga(conexao).capturarInteressadosNaVaga(vaga.id as Integer,cnpj,new GerenciadorCandidato(conexao))


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
}
