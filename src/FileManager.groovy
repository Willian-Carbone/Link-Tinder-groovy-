import groovy.json.JsonSlurper
import groovy.json.JsonOutput


abstract class FileManager {

    static adicionar(Object o, String tipo) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        // conversao do arquivo para tipo json sluper , classe inteligente, faz conversoes de map e inserçoes de forma automatizada


        switch (tipo) {
            case "Empresa":
                bancodedados.Empresa << o
                break

            case "Candidato":
                bancodedados.Candidato << o

        }

        arq.text = JsonOutput.prettyPrint(JsonOutput.toJson(bancodedados))
        // rescreve arquivo realizando conversões (sem buscas/ necessidade de map/ criar array para adicioanr novo objeto)


    }


    static boolean cpf_em_uso(String cpf) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        return bancodedados.Candidato.any { it.cpf == cpf }


    }

    static boolean cnpj_em_uso(String cnpj) {

        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        return bancodedados.Empresa.any { it.cnpj == cnpj }


    }



    static <T> ArrayList<T> listagem(String opcao) {
        File arq = new File("../Dados.json")
        def bancodedados = new JsonSlurper().parse(arq)
        def dados = bancodedados[opcao] ?: []

        // define qual classe sera  instanciada e seu construtor, necessario poisa  automatização(obejto pra mapa)  utiliza o costrutor vazio, que nas classes nao existem

        def classeAlvo = (opcao == "Candidato") ? Candidato.class : Empresa.class
        def construtor = classeAlvo.constructors[0]

        //itera em dados, que é uma lista onde cada item é um mapa de itens da classe atual, retornanando um array list contendo obejtos especiicados

        return (ArrayList<T>) dados.collect { mapa ->



            String estadoRaw = mapa.estado?.toString()?.trim() ?: "" // garante queo estado nunca sera nulo, e sera convertido para tring e tera espaços removidos, remove inconsistencias entre enum e json



            def estadoEnum = null
            if (estadoRaw) {
                String nomeLimpo = Metodos.normalizador(estadoRaw)  // traduz o estadoraw (arquvio contendo o exato moodo de como esta salvo o dado estado no json, no formato salvo no enum
                if (nomeLimpo) {
                   estadoEnum = Estados.valueOf(nomeLimpo) // cria o objeto estato enum
                }
            }

            def args = []

            // manualmente , constroi cada objeto do tipo especificado

            if (opcao == "Candidato") {
                args = [
                        mapa.nome as String,
                        mapa.cpf as String,
                        mapa.idade as Integer,
                        mapa.email as String,
                        mapa.cep as String,
                        estadoEnum,
                        mapa.descricao as String,
                        mapa.competencias as ArrayList<String>
                ]
            } else {
                args = [
                        mapa.nome as String,
                        mapa.cnpj as String,
                        mapa.pais as String,
                        mapa.cep as String,
                        mapa.email as String,
                        estadoEnum,
                        mapa.descricao as String,
                        mapa.competencias as ArrayList<String>
                ]
            }

            return construtor.newInstance(args.toArray())
        }
    }


    }
















