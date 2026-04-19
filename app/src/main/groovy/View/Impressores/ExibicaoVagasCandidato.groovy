package View.Impressores


import groovy.sql.GroovyRowResult

class ExibicaoVagasCandidato implements Impressor<List<GroovyRowResult>> {
    @Override
    void exibirDado(List<GroovyRowResult> vagas) {

        if (vagas) {

            for (vaga in vagas) {
                println("""
                       Nome:${vaga.nome}
                       Identificador da vaga:${vaga.id_vaga}
                       Identificador do contratante anonimo:${vaga.id_contratante}
                       Descriçao da vaga:${vaga.descricao}
                       Requisitos da vaga:${vaga.competencias}
                       -------------------------------------"""
                )
            }
        } else {
            println("Não foram encontradas Vagas para você")
        }

    }
}
