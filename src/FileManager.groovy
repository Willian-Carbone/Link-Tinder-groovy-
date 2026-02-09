import groovy.json.JsonSlurper
import groovy.json.JsonOutput

abstract class FileManager {

    static adicionar (Object o, String tipo) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq) // conversao do arquivo para tipo json sluper , classe inteligente, faz conversoes de map e inserçoes de forma automatizada


        switch (tipo){
            case "empresa":
                bancodedados.empresas << o
                break

            case "candidato":
                bancodedados.candidatos << o

        }

        arq.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados)) // rescreve arquivo realizando conversões (sem buscas/ necessidade de map/ criar array para adicioanr novo objeto)


    }


    static boolean cpf_em_uso(String cpf){

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        return bancodedados.candidatos.any { it.cpf == cpf}


    }

    static boolean cnpj_em_uso(String cnpj){

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        return bancodedados.empresas.any { it.cnpj == cnpj}


    }







}
