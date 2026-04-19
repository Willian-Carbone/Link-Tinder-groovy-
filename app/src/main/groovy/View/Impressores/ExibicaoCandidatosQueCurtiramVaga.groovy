package View.Impressores


import groovy.sql.GroovyRowResult

class ExibicaoCandidatosQueCurtiramVaga implements Impressor<List<GroovyRowResult>> {
    @Override
    void exibirDado(List<GroovyRowResult> candidatosQueCurtiramVaga) {

        println("Candidatos que curtiram a vaga e suas habilidades")

        candidatosQueCurtiramVaga.forEach {
            candidato ->
                println("Numero do Candidato: ${candidato.id}")
                println("Habilidades : ${candidato.competencias}")
                println("_______________________________________")


        }

    }
}
