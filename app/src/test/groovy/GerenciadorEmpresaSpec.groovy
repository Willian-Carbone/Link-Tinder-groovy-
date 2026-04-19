import Daos.GerenciadorEmpresa
import Model.Objetos.Empresa
import Model.Objetos.Match
import groovy.sql.GroovyRowResult
import Model.Enuns.*
import spock.lang.Shared

class GerenciadorEmpresaSpec extends FakeBancoConfigure {

    @Shared
    GerenciadorEmpresa gerenciador

    def "Teste inserção empresa sucesso"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','emial@gm','12345678','RJ','desc')")


        Empresa empresa = new Empresa ("nome","12345678901234","brasil","12345678","em@email.com", Estados.GOIAS,"des",[Especialidades.C])

        empresa.identificador=1


        gerenciador=new GerenciadorEmpresa(sqlH2)


        when:

        gerenciador.criarPerfil(empresa)


        then:

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM empresa WHERE empresa_id=?",[empresa.identificador])

        linhaCapturada.cnpj == empresa.cnpj
        linhaCapturada.empresa_id == empresa.identificador
        linhaCapturada.pais==empresa.pais


    }

    def "Teste inserção empresa Falha"(){

        given:
        Empresa empresaInvalida = new Empresa(null,null,null,null,null,null,null,null)

        gerenciador=new GerenciadorEmpresa(sqlH2)

        when:

       gerenciador.criarPerfil(empresaInvalida)

        then:
        thrown(RuntimeException)
    }

    def"Teste de busca de ID da empresa"()
    {


        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','emial@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])

        gerenciador=new GerenciadorEmpresa(sqlH2)
        when:


        Integer idObtido = gerenciador.capturarId("12345678901234")

        then:
        idObtido==1


    }

    def "Teste de busca de informações de uma empresa"(){


        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresaTeste','emailemp@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO especialidade_usuario (especialidade,usuario) VALUES (?,?)",["JAV",1])

        gerenciador=new GerenciadorEmpresa(sqlH2)

        when:


       GroovyRowResult resultadosObtidos= gerenciador.capturarInformacoesPerfil("12345678901234")

        then:
        resultadosObtidos.nome=="empresaTeste"
        resultadosObtidos.email=="emailemp@gm"
        resultadosObtidos.competencias =="JAV"

    }

    def "Teste buscar matchs da empresa"(){


        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresaTeste','emailemp@gm','12345678','RJ','desc')")
        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('candidatoTeste', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc", "12345678901234"])
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20, 2])
        sqlH2.executeInsert("INSERT INTO matchs (candidato,empresa,vaga) VALUES(?,?,?)", ["12345678901", "12345678901234", 1])

        gerenciador= new GerenciadorEmpresa(sqlH2)

        when:

        List<GroovyRowResult> linhasCapturadas = gerenciador.buscarMatchs("12345678901234")

        then:
        linhasCapturadas.size() == 1
        linhasCapturadas[0].nome == "candidatoTeste"

    }

    def "Teste busca vagas empresa"() {

        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresaTeste','emailemp@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc", "12345678901234"])


        gerenciador = new GerenciadorEmpresa(sqlH2)
        when:

        List<GroovyRowResult> resultadosObtidos= gerenciador.buscarVagas("12345678901234")

        then:

        resultadosObtidos.size()==1
        resultadosObtidos[0].contratante=="12345678901234"

    }



    def "Teste troca pais empresa"(){
        given:

        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('nomeCandidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])

        gerenciador = new GerenciadorEmpresa(sqlH2)

        when:

        gerenciador.editarPerfil("12345678901234","USA")

        GroovyRowResult linhaCapturada= sqlH2.firstRow("Select * FROM empresa WHERE cnpj='12345678901234'")

        then:
        linhaCapturada.pais=="USA"
    }


    def "Teste Remocao de perfil empresa"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresaTeste','emailemp@gm','12345678','RJ','desc')")
        sqlH2.execute("INSERT INTO usuario (nome, email, cep, estado, descricao) VALUES ('Candidato', 'y@x.com', '12345678', 'RJ', '...')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc","12345678901234"])
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,2])
        sqlH2.executeInsert("INSERT INTO matchs (candidato,empresa,vaga) VALUES(?,?,?)",["12345678901","12345678901234",1])

        gerenciador=new GerenciadorEmpresa(sqlH2)

        when:

        gerenciador.removerPerfil("12345678901234")

        List<GroovyRowResult> tabelaMatch= sqlH2.rows("SELECT * FROM matchs")
        List<GroovyRowResult> tabelaEmpresa= sqlH2.rows("SELECT * FROM empresa")
        List<GroovyRowResult> tabelaVagas = sqlH2.rows("SELECT * FROM vaga")

        then:

        tabelaEmpresa.size()==0
        tabelaMatch.size()==0
        tabelaVagas.size()==0

    }


    def "Teste inserção match sucesso"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresa','emailemp@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])
        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES (?,?,?)", ["vaganome", "vagaDesc","12345678901234"])
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])

        gerenciador = new GerenciadorEmpresa(sqlH2)

        Match match = new Match("12345678901","12345678901234",1)


        when:

        Integer idGerado = gerenciador.criarMatch(match)

        then:

        GroovyRowResult linhaGerada = sqlH2.firstRow("SELECT * FROM matchs WHERE id=?",[idGerado])

        linhaGerada.empresa==match.cnpj
        linhaGerada.candidato==match.cpf
        linhaGerada.vaga==match.idVaga

    }

    def "Teste inserção match Falha"() {
        given:
        Match matchInvalido = new Match(null, null, null)

        gerenciador = new GerenciadorEmpresa(sqlH2)

        when:

        gerenciador.criarMatch(matchInvalido)

        then:
        thrown(Exception)
    }


}
