import Daos.GerenciadorCandidato
import Model.Objetos.Candidato
import Model.Enuns.*
import Model.Objetos.Curtida
import groovy.sql.GroovyRowResult
import spock.lang.Shared

class GerenciadorCandidatoSpec extends FakeBancoConfigure {

    @Shared
    GerenciadorCandidato gerenciador

    def "Teste inserção candidato sucesso"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','emial@gm','12345678','RJ','desc')")

        gerenciador= new GerenciadorCandidato(sqlH2)

        Candidato candidato=new Candidato("candidato","12345678901",20,"em@gm.com","12345678901",Estados.BAHIA,"desc",[Especialidades.JAV])
        candidato.setIdentificador(1)


        when:

        gerenciador.criarPerfil(candidato)


        then:

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM candidato WHERE candidato_id=?",[candidato.identificador])

        linhaCapturada.cpf == candidato.cpf
        linhaCapturada.candidato_id == candidato.identificador
        linhaCapturada.idade==candidato.idade


    }

    def "Teste inserção candadidato Falha"(){
        given:
        Candidato candidatoInvalida = new Candidato(null,null,null,null,null,null,null,null)
        gerenciador=new GerenciadorCandidato(sqlH2)

        when:

        gerenciador.criarPerfil(candidatoInvalida)
        then:
        thrown(RuntimeException)
    }

    def"Teste de busca de ID de Candidato"()
    {

        given:

        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('candidatoTeste', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])

        gerenciador=new GerenciadorCandidato(sqlH2)


        when:

        Integer idObtido= gerenciador.capturarId("12345678901")

        then:
        idObtido==1


    }

    def "Teste de busca de informações de um candidato"(){
        given:

        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('nomeCandidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])
        sqlH2.executeInsert("INSERT INTO especialidade_usuario (especialidade,usuario) VALUES (?,?)",["JAV",1])

        when:
        gerenciador=new GerenciadorCandidato(sqlH2)

        GroovyRowResult resultadosObtidos = gerenciador.capturarInformacoesPerfil("12345678901")

        then:
        resultadosObtidos.nome=="nomeCandidato"
        resultadosObtidos.email=="y@x.com"
        resultadosObtidos.competencias =="JAV"

    }

    def "Teste buscar matchs do Candidato"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresaTeste','emailemp@gm','12345678','RJ','desc')")
        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('Candidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc","12345678901234"])
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,2])
        sqlH2.executeInsert("INSERT INTO matchs (candidato,empresa,vaga) VALUES(?,?,?)",["12345678901","12345678901234",1])

        gerenciador=new GerenciadorCandidato(sqlH2)

        when:

        List<GroovyRowResult> linhasCapturadas = gerenciador.buscarMatchs("12345678901")

        then:
        linhasCapturadas.size()==1
        linhasCapturadas[0].nome=="empresaTeste"


    }

    def "Teste busca vagas que o candidato ainda não interagiu"() {
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES (?,?,?,?,?)", ['emp1', 'emp1@gmail', '12345678', 'SP', 'des'])
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES (?,?,?,?,?)", ['emp2', 'emp2@gmail', '12345678', 'RJ', 'desc'])
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES (?,?,?,?,?)", ['candidato', 'cand@meta.com', '12345678', 'RJ', 'cand'])

        sqlH2.execute("INSERT INTO empresa (cnpj, pais, empresa_id) VALUES (?,?,?)", ["12345678901234", "X", 1])
        sqlH2.execute("INSERT INTO empresa (cnpj, pais, empresa_id) VALUES (?,?,?)", ["12345678901235", "X", 2])


        sqlH2.execute("INSERT INTO vaga (nome, descricao, contratante) VALUES (?,?,?)", ["V1", "Vaga 1", "12345678901234"])
        sqlH2.execute("INSERT INTO vaga (nome, descricao, contratante) VALUES (?,?,?)", ["V2", "Vaga 2", "12345678901235"])

        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,3])



        sqlH2.execute("INSERT INTO curtida (candidato, vaga) VALUES (?, ?)", ["12345678901", 1])

        gerenciador=new GerenciadorCandidato(sqlH2)

        when:
        List<GroovyRowResult> resultado = gerenciador.buscarVagas("12345678901")

        then:
        resultado.size() == 1
        resultado[0].nome == "V2"
        resultado[0].cnpj_empresa == "12345678901235"

    }

    def "Teste retorno lista vazia se o candidato já deu match com todas as empresas"() {


        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('Empresa', 'x@x.com', '12345678', 'RJ', '...')")
        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('Candidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.execute("INSERT INTO empresa (cnpj, pais, empresa_id) VALUES ('12345678901234', 'BR', 1)")
        sqlH2.execute("INSERT INTO vaga (nome, descricao, contratante) VALUES ('Vaga X', '...', '12345678901234')")

        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,2])


        sqlH2.execute("INSERT INTO matchs (candidato, empresa,vaga) VALUES (?, ?,?)", ["12345678901", "12345678901234",1])

        gerenciador = new GerenciadorCandidato(sqlH2)

        when:
        List<GroovyRowResult> resultado = gerenciador.buscarVagas("12345678901")

        then:
        resultado.isEmpty()
    }


    def "Teste troca idade candidato"(){
        given:

        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('nomeCandidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])

        gerenciador = new GerenciadorCandidato(sqlH2)

        when:

        gerenciador.editarPerfil("12345678901",30)

        GroovyRowResult linhaCapturada= sqlH2.firstRow("Select * FROM candidato WHERE cpf='12345678901'")

        then:
        linhaCapturada.idade==30
    }


    def "Teste Remocao de perfil candidato"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresaTeste','emailemp@gm','12345678','RJ','desc')")
        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('Candidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc","12345678901234"])
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,2])
        sqlH2.executeInsert("INSERT INTO matchs (candidato,empresa,vaga) VALUES(?,?,?)",["12345678901","12345678901234",1])

        gerenciador=new GerenciadorCandidato(sqlH2)

        when:

        gerenciador.removerPerfil("12345678901")

        List<GroovyRowResult> tabelaMatch= sqlH2.rows("SELECT * FROM matchs")
        List<GroovyRowResult> tabelaCandidato= sqlH2.rows("SELECT * FROM candidato")

        then:

        tabelaCandidato.size()==0
        tabelaMatch.size()==0

    }



    def "teste inserção curtida sucesso"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresa','emailemp@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc","12345678901234"])
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])


        Curtida curtida = new Curtida("12345678901",1)

       gerenciador = new GerenciadorCandidato(sqlH2)


        when:
        Integer idGerado= gerenciador.curtir(curtida)


        then:

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM curtida WHERE id=?",[idGerado])

        linhaCapturada.vaga==curtida.idVaga
        linhaCapturada.candidato==curtida.cpf




    }

    def "teste inserção curtida falha"(){

        given:

        Curtida curtidaInvalida = new Curtida(null,null)

        gerenciador=new GerenciadorCandidato(sqlH2)

        when:

        gerenciador.curtir(curtidaInvalida)

        then:

        thrown(Exception)




    }


}
