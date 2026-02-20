import Enuns.Especialidades
import Enuns.Estados
import Objetos.Candidato
import Objetos.Empresa
import Objetos.Vaga
import spock.lang.Specification

import Metodos.FileManager

class FileManagerSpec extends Specification{

    def "Deve adicionar um objeto na lista correta do banco de dados"() {
        given: "estado inicial de dados"
        def bancoInicial = [Empresa: [], Candidato: []]
        def novoCandidato = [nome: "João", cpf: "123"]

        when: "Adiçaõ é requerida"
        def resultado = FileManager.processarAdicao(bancoInicial, novoCandidato, "Objetos.Candidato")

        then: "Apenas a lista de Candidatos deve ter crescido"
        resultado.Candidato.size() == 1
        resultado.Candidato[0].nome == "João"
        resultado.Empresa.isEmpty()
    }


    def "Deve validar se um CPF ou CNPJ já está cadastrado no mapa"() {
        given: "Estado inicial do banco"
        def bancoinicial = [
                Candidato: [[nome: "Ana", cpf: "12345678901"]],
                Empresa: [[nome:"empresaexemplo", cnpj:"12345678901234"]]
        ]

        expect: "A checagem deve retornar true se o valor informado ja tiver cadastrado em algun dos perfis "
        FileManager.checagem(valorBusca, bancoinicial) == resultadoEsperado

        where:
        valorBusca | resultadoEsperado
        "12345678901"   | true
        "12345678901234"   | true
        "asd"|false
        "123"|false
        ""|false
        null|false
    }

    def "Garantir a construção correta dos objetos Candidato ou Empresa"() {
        given: "Estado inicial da lista dados"

        Map Meubanco = [Candidato: [


                ["cpf"         : "12345678998",
                 "idade"       : 20,
                 "cep"         : "12345678",
                 "competencias": ["HT", "CS"],
                 "descricao"   : "estudante",
                 "email"       : "j@gmail.com",
                 "estado"      : "MARANHAO",
                 "nome"        : "joao"],

                ["cpf"         : "12345678901",
                 "idade"       : 28,
                 "cep"         : "12345678",
                 "competencias": ["ANG", "JAV"],
                 "descricao"   : "dev junior",
                 "email"       : "m@gmail.com",
                 "estado"      : "RIODEJANEIRO",
                 "nome"        : "maria"]


        ], Empresa               : [

                ["cnpj"        : "12345678901235",
                 "pais"        : "brasil",
                 "cep"         : "12345678",
                 "competencias": ["PT", "SPR"],
                 "descricao"   : "minas e energia",
                 "email"       : "val@gmai.com",
                 "estado"      : "CEARA",
                 "nome"        : "vale"],

                ["cnpj"        : "12345678901234",
                 "pais"        : "brasil",
                 "cep"         : "12345678",
                 "competencias": ["HT", "CS"],
                 "descricao"   : "empresa de combustivel",
                 "email"       : "petro@gmai.com",
                 "estado"      : "SAOPAULO",
                 "nome"        : "petrobras"]


        ]]

        String opcao1 = "Candidato"
        String opcao2 = "Empresa"

        when: "chamamos o contrutor de lista com o banco e o objeto escolhido"

        List<Candidato> candidatos = FileManager.ConstrucaoDoObjeto(Meubanco, opcao1)
        List<Empresa> empresas = FileManager.ConstrucaoDoObjeto(Meubanco, opcao2)


        then: "as listas devem ser instanciadas corretamente"

        candidatos[0].getClass() == Candidato
        candidatos[0].getCpf() == "12345678998"
        candidatos[0].getEstado() == Estados.MARANHAO

        and:
        empresas[0].getClass() == Empresa
        empresas[0].getCnpj() == "12345678901235"
        empresas[0].getEstado() == Estados.CEARA


    }

    def "Retorno da lista de vagas , filtrada por id (cpf ou cnpj)"(){
        given: "Situação inicial dos dados das vagas"

        Map dados = [vagas:

           [
               ["nome": "vaga1",
                 "contratante": "12345678901231",
                 "requisitos": ["HT"],


                "curtidas": [

                  [
                          "identificador": "MTIzNDU2Nzg5MDE=",
                          "email": "m@m.gamil.com",
                          "competencias": ["CS", "JAV", "HT"]
                  ]
                ],

                 "descricao": "vaga1"
               ],

               // vagas sem curtidas

               ["nome": "vaga2",
                "contratante": "12345678901232",
                "requisitos": ["PT"],
                "curtidas": [],
                "descricao": "vaga2"
               ],



               ["nome": "vaga3",
                "contratante": "1234567890123",
                "requisitos": ["HT"],
                "curtidas": [],
                "descricao": "vaga3"
               ]


           ]



        ]

        String cnpj = "1234567890123"
        String cpf = "12345678901"


        when: "chama-se a funçao de listagem de vagas com cada um dos ids"

        List<Vaga> vagasdocandidato = FileManager.logica_listagem(cpf,dados)
        List<Vaga> vagasdaempresa = FileManager.logica_listagem(cnpj,dados)


        then: "Cada empresa só ve suas vagas e cada candidato só ve as que nao curtiu"

        vagasdocandidato.size() == 2
        vagasdocandidato[0]["contratante"] == "12345678901232"
        vagasdocandidato[1]["contratante"] == "1234567890123"

        vagasdaempresa.size() ==1
        vagasdaempresa[0]["contratante"] == cnpj



    }

    def "Lógica de adesão de vaga ao banco de dados"(){

        given:

        Vaga minhavaga = new Vaga(nome: "vaga3", descricao: "vaga3", contratante: "12345678901234", requisitos: [Especialidades.ANG, Especialidades.JAV], curtidas: [])

        Map bancoDeDados = [vagas:
              [

                 ["nome": "vaga1",
                  "contratante": "12345678901232",
                  "requisitos": [Especialidades.ANG],
                  "curtidas": [],
                  "descricao": "vaga1"
                 ],



                 ["nome": "vaga2",
                  "contratante": "1234567890123",
                  "requisitos": [Especialidades.PT],
                  "curtidas": [],
                  "descricao": "vaga2"
                 ]

              ]
        ]

        when: "Uma chamada é acionada de adição de vaga"

        bancoDeDados = FileManager.logicAdicao(minhavaga,bancoDeDados)


        then: "A vaga é adicionada ao final do banco"

        bancoDeDados["vagas"][2]["nome"] == "vaga3"


    }

    def "Salvamento de uma curtida numa vaga feita por um candidato"(){
        given: "dados dos usuarios e dados das vagas ja registradas"

        Map bancoUsuarios = [

                Candidato:[

                        ["cpf"         : "12345678901",
                         "idade"       : 20,
                         "cep"         : "12345678",
                         "competencias": ["HT", "CS"],
                         "descricao"   : "estudante",
                         "email"       : "j@gmail.com",
                         "estado"      : "MARANHAO",
                         "nome"        : "joao"],

                        ["cpf"         : "12345678901",
                         "idade"       : 28,
                         "cep"         : "12345678",
                         "competencias": ["ANG", "JAV"],
                         "descricao"   : "dev junior",
                         "email"       : "m@gmail.com",
                         "estado"      : "RIODEJANEIRO",
                         "nome"        : "maria"]


                ],

                Empresa:[

                        ["cnpj"        : "12345678901235",
                         "pais"        : "brasil",
                         "cep"         : "12345678",
                         "competencias": ["PT", "SPR"],
                         "descricao"   : "minas e energia",
                         "email"       : "val@gmai.com",
                         "estado"      : "CEARA",
                         "nome"        : "vale"],


                        ["cnpj"        : "12345678901234",
                         "pais"        : "brasil",
                         "cep"         : "12345678",
                         "competencias": ["HT", "CS"],
                         "descricao"   : "empresa de combustivel",
                         "email"       : "petro@gmai.com",
                         "estado"      : "SAOPAULO",
                         "nome"        : "petrobras"]

                ]
        ]

        Map bancoVaga = [vagas:
          [

            ["nome": "vaga1",
             "contratante": "12345678901232",
             "requisitos": [Especialidades.ANG],
             "curtidas": [],
             "descricao": "vaga1"
            ],



             ["nome": "vaga2",
              "contratante": "12345678901234",
              "requisitos": [Especialidades.PT],
              "curtidas": [],
              "descricao": "vaga2"
             ]


          ]
        ]

        String id_empresa ="12345678901234"
        String id_pessoa = "12345678901"

        when: "O usuario realiza uma curtida em uma determinada vaga"

        bancoVaga = FileManager.logicaRegistroCurtida(bancoVaga,bancoUsuarios,id_empresa,id_pessoa)

        then:

        bancoVaga["vagas"][1]["curtidas"].size() == 1




    }

    def "Salvamento de match "(){
        given: "banco inicial"

        Map bancoMatch = [Match:[]]

        Map bancoUsuarios = [

                Candidato:[

                        ["cpf"         : "12345678901",
                         "idade"       : 20,
                         "cep"         : "12345678",
                         "competencias": ["HT", "CS"],
                         "descricao"   : "estudante",
                         "email"       : "j@gmail.com",
                         "estado"      : "MARANHAO",
                         "nome"        : "joao"],

                        ["cpf"         : "12345678901",
                         "idade"       : 28,
                         "cep"         : "12345678",
                         "competencias": ["ANG", "JAV"],
                         "descricao"   : "dev junior",
                         "email"       : "m@gmail.com",
                         "estado"      : "RIODEJANEIRO",
                         "nome"        : "maria"]


                ],

                Empresa:[

                        ["cnpj"        : "12345678901235",
                         "pais"        : "brasil",
                         "cep"         : "12345678",
                         "competencias": ["PT", "SPR"],
                         "descricao"   : "minas e energia",
                         "email"       : "val@gmai.com",
                         "estado"      : "CEARA",
                         "nome"        : "vale"],


                        ["cnpj"        : "12345678901234",
                         "pais"        : "brasil",
                         "cep"         : "12345678",
                         "competencias": ["HT", "CS"],
                         "descricao"   : "empresa de combustivel",
                         "email"       : "petro@gmai.com",
                         "estado"      : "SAOPAULO",
                         "nome"        : "petrobras"]

                ]
        ]

        String empresaId= "12345678901235"
        String candidatoId = "12345678901"

        when: "Uma empresa curte um usuario que curtiu uma vaga postada por ela"

        bancoMatch = FileManager.logicaCriarMatch(bancoUsuarios,bancoMatch,empresaId,candidatoId)

        then:"a curtida vira um match, que é registrado no banco de dados"

        bancoMatch["Match"].size() == 1
        bancoMatch["Match"][0]["empresa"]["cnpj"]==empresaId
        bancoMatch["Match"][0]["candidato"]["cpf"]==candidatoId




    }

    def "Verificação dos dados de match apra ambos os lados, filtrados por cpf ou cnpj"(){

        given: "banco matchs inicial"
        Map bancoMatchs = [
              "Match": [
                 ["empresa":
                     [  "cnpj": "12345678901234",
                        "nome": "petrobras",
                        "email": "petro@gmai.com",
                        "descricao": "empresa de combustivel"
                     ],

                     "candidato": [
                             "cpf": "12345678901",
                             "nome": "maria",
                             "email": "m@m.gamil.com",
                             "competencias": ["CS", "JAV", "HT"]
                     ]


                 ]
              ]
        ]


         when: "Candidata consulta seus matches"
            def listagemCandidato = FileManager.logicaChecagemMatchs(bancoMatchs, "12345678901")

            then: "Ela deve ver os dados da empresa"
            listagemCandidato.size() == 1
            listagemCandidato[0].contains("EMPRESA: petrobras")
            listagemCandidato[0].contains("petro@gmai.com")

            when: "Empresa consulta seus matches"
            def listagemEmpresa = FileManager.logicaChecagemMatchs(bancoMatchs, "12345678901234")

            then: "Ela deve ver o cpf de candidatos que curtiram a vaga criptografados"
            listagemEmpresa.size() == 1
            listagemEmpresa[0].contains("CANDIDATO:")
            !listagemEmpresa[0].contains("12345678901")
        }



    }





