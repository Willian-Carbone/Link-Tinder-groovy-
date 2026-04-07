import Enuns.Especialidades
import Enuns.Estados
import Metodos.GerenciadorBancoDados
import Objetos.Candidato
import Objetos.Empresa
import Objetos.Vaga
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

import java.sql.SQLException
import java.sql.SQLNonTransientConnectionException

class GerenciadorBancoDadosSpec extends Specification {

    @Shared Sql sqlH2
    @Shared GerenciadorBancoDados gbd
    @Shared  ArrayList<Especialidades> competencias = [Especialidades.JAV,Especialidades.ANG,Especialidades.PT,Especialidades.HT]

    
    def setupSpec (){

        sqlH2 = Sql.newInstance("jdbc:h2:mem:linkEtinder;DATABASE_TO_UPPER=FALSE", "u", "", "org.h2.Driver")

        URL scriptCriacaoBanco= getClass().getResource('/comandosCriacaoBancoDados.sql')

        sqlH2.execute(scriptCriacaoBanco.text)

        gbd = new GerenciadorBancoDados(sqlH2)


    }

    def cleanupSpec(){
        sqlH2.close()
    }

    def cleanup() {

        sqlH2.execute("SET REFERENTIAL_INTEGRITY FALSE")


        def tabelas = [
                "especialidade_usuario", "candidato", "usuario",
                "curtida", "empresa", "especialidade_vaga",
                "vaga", "matchs"
        ]

        tabelas.each { tabela ->
            sqlH2.execute("TRUNCATE TABLE ${tabela} RESTART IDENTITY".toString())
        }


        sqlH2.execute("SET REFERENTIAL_INTEGRITY TRUE")
    }





    def "Teste de conexão com banco - url invalida ou banco offline"() {
        given: "uma conexão invalida"

        String urlInvalida = "erro"

        when: "tentativa de instanciar o controlador com conexão invalida"

        GerenciadorBancoDados c = new GerenciadorBancoDados(Sql.newInstance(urlInvalida,"postgres","", "org.postgresql.Driver"))

        then: "Uma exceção sql deve ser lançada"

        SQLException e = thrown(SQLException)

    }


    def "Teste de conexão com banco - sucesso com mock"() {

        given: "Um banco de dados em memória válido"


        when: "Passamos um banco válido para o construtor"

        GerenciadorBancoDados c= new GerenciadorBancoDados(sqlH2)

        then: "Nenhuma exceção deve ser lançada"
        notThrown(SQLNonTransientConnectionException)


    }

    def "Teste inserção candidato"(){

        given: "um objeto candidato e um banco de dados validos "



        Candidato c= new Candidato("candidatoTeste","12345678901",20,"testemail@gmail.com","12345678",Estados.ACRE,"descricaoexemplo",competencias)

        when: "A função é chamada passando o objeto  "

         gbd.registrarCandidato(c)



        then: " as propriedas do objeto são dividas e salvas no banco organizadamente "



        GroovyRowResult linhaCandidato=sqlH2.firstRow "SELECT * FROM candidato WHERE cpf=?" ,["12345678901"]
        GroovyRowResult linhaUsuario =sqlH2.firstRow "SELECT * FROM usuario WHERE id=?" ,[linhaCandidato.candidato_id]
        List<GroovyRowResult> linhasEspecialidadesUsuario=sqlH2.rows "SELECT * FROM especialidade_usuario WHERE usuario=?", [linhaCandidato.candidato_id]


        linhaCandidato.idade==20
        linhaCandidato.cpf=="12345678901"

        linhaUsuario.nome=="candidatoTeste"
        linhaUsuario.email=="testemail@gmail.com"
        linhaUsuario.cep=="12345678"
        linhaUsuario.estado=="AC"
        linhaUsuario.descricao=="descricaoexemplo"


        linhasEspecialidadesUsuario.size()==competencias.size()


    }


    def "Teste inserção empresa"(){

        given: "um objeto empresa e um banco de dados validos "



        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","testmail@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)

        when: "A função é chamada passando o objeto  "

        gbd.registrarEmpresa(emp)



        then: " as propriedas do objeto são dividas e salvas no banco organizadamente "



        GroovyRowResult linhaEmpresa=sqlH2.firstRow "SELECT * FROM empresa WHERE cnpj=?" ,["12345678901234"]
        GroovyRowResult linhaUsuario =sqlH2.firstRow "SELECT * FROM usuario WHERE id=?" ,[linhaEmpresa.empresa_id]
        List<GroovyRowResult> linhasEspecialidadesUsuario=sqlH2.rows "SELECT * FROM especialidade_usuario WHERE usuario=?", [linhaEmpresa.empresa_id]


        linhaEmpresa.cnpj=="12345678901234"
        linhaEmpresa.pais=="Brazil"

        linhaUsuario.nome=="empresaTeste"
        linhaUsuario.email=="testmail@gmail"
        linhaUsuario.cep=="12345678"
        linhaUsuario.estado=="RJ"
        linhaUsuario.descricao=="descricaoexemplo"


        linhasEspecialidadesUsuario.size()==competencias.size()


    }


    def "Teste de insercao vaga de empresa cadastrada"(){
        given:"Um objeto vaga ,um objeto empresa e um banco valido"



        Vaga v= new Vaga("vaga exemplo","exemplo devaga","12345678901234",competencias)
        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","testmail@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)

        when:"chamada da função passando o objeto como parametro , a empresa deve estar previamente registrada, por tanto para o teste há a necessidade de implementa-la"

        gbd.registrarEmpresa(emp)
        Integer idVagaCriada=gbd.registrarVaga(v)



        then:"As propriedades de vaga devem ser dividas e adicionadas em suas respectivas tabelas"

        GroovyRowResult linhaVaga = sqlH2.firstRow "SELECT * FROM vaga WHERE id=?" ,[idVagaCriada]
        List<GroovyRowResult> linhasPropriedasVagas = sqlH2.rows "SELECT * FROM especialidade_vaga WHERE vaga=?",[idVagaCriada]

        linhaVaga.nome=="vaga exemplo"
        linhaVaga.descricao=="exemplo devaga"
        linhaVaga.contratante=="12345678901234"

        linhasPropriedasVagas.size()==competencias.size()

    }

    def "teste inserçaõ curtida"(){
        given: "um cpf de um candidato registrado  um id de vaga ja cadastrado"



        Vaga v= new Vaga("vagaexemplo","exemplodevaga","12345678901234",competencias)
        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","testmail@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        Candidato c= new Candidato("candidatoTeste","12345678901",20,"testemail@gmail.com","12345678",Estados.ACRE,"descricaoexemplo",competencias)

        gbd.registrarEmpresa(emp)
        Integer idVaga = gbd.registrarVaga(v)
        gbd.registrarCandidato(c)



        when: "um usuario portador do cpf realizar uma curtida "

        Integer idRegistrada = gbd.registrarCurtidaVaga(c.getCpf(),idVaga)

        GroovyRowResult linha = sqlH2.firstRow "SELECT * FROM curtida WHERE vaga=? AND candidato=? ",[idRegistrada,c.getCpf()]

        then:"uma linha referente a tabela curtida deve ser criada na tabela curtida"

        linha.candidato==c.getCpf()
        linha.vaga==idRegistrada

    }

    def"teste insercao match"(){
        given: "uma empresa cadastrada, um candidato cadastrado , e uma vaga cadastrada"


        Vaga v= new Vaga("vagaexemplo","exemplodevaga","12345678901234",competencias)
        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","testmail@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        Candidato c= new Candidato("candidatoTeste","12345678901",20,"testemail@gmail.com","12345678",Estados.ACRE,"descricaoexemplo",competencias)


        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)
       Integer idVaga = gbd.registrarVaga(v)

        when: "uma requisicao de salvamento de match é feita"

        gbd.registrarMatch(c.getCpf(),emp.getCnpj(),idVaga)

        then:"uma linha deve ser criada em matchs, com as informaçoes corretas"

        GroovyRowResult linha = sqlH2.firstRow "SELECT * FROM matchs WHERE empresa=? AND candidato=? AND vaga=?",[emp.getCnpj(),c.getCpf(),idVaga]

        linha.empresa==emp.getCnpj()
        linha.candidato==c.getCpf()
        linha.vaga==idVaga




    }



    def "teste cpf em uso"(){
        given: "um candidato registrado com um dado cpf e um cpf ainda nao registrado"

        Candidato c= new Candidato("candidatoTeste","12345678901",20,"testemail@gmail.com","12345678",Estados.ACRE,"descricaoexemplo",competencias)
        String cpfDisponivel="12345678902"

        gbd.registrarCandidato(c)

        when:"verificasse se os cpfs estao em uso"

        boolean cpfJaUsado = gbd.cpfEmUso(c.getCpf())
        boolean cpfNaoUsado = gbd.cpfEmUso(cpfDisponivel)

        then: "Deve retornar true para um cpf ja utilizao e false para um disponivel"

        cpfJaUsado
        !cpfNaoUsado

    }

    def "teste cnpj em uso"(){
        given: "uma empresa registrada com um dado cnpj e um cnpj ainda nao registrado"

        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","testmail@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        String cnpjDisponivel="12345678901235"

        gbd.registrarEmpresa(emp)

        when:"verificasse se os cnpjs estao em uso"

        boolean cnpjJaUsado = gbd.cnpjEmUso(emp.getCnpj())
        boolean cnpjNaoUsado = gbd.cnpjEmUso(cnpjDisponivel)

        then: "Deve retornar true para um cpf ja utilizao e false apra um disponivel"

        cnpjJaUsado
        !cnpjNaoUsado

    }

    def "teste email em uso"(){
        given: "uma empresa (candiato) registrada com um dado email e um cnpj ainda nao registrado"

        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","emailusado@gmail.com",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        String emailDisponivel="emaillivre@gmail.com"

        gbd.registrarEmpresa(emp)

        when:"verificasse se os emails estao em uso"

        boolean emailJaUsado = gbd.emailEmUso(emp.getEmail())
        boolean emailNaoUsado = gbd.emailEmUso(emailDisponivel)

        then: "Deve retornar true para um cpf ja utilizao e false apra um disponivel"

        emailJaUsado
        !emailNaoUsado

    }



    def "Teste de captura de vagas nao interagidas por um usuario"(){
        given: "3 empresas cadastras , cada uma cadastra uma vaga , um candidato curte uma vaga , da match  com um uma empresa e nao intereja com a vaga da terceira"

        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","emp1@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        Empresa emp2= new Empresa("empresaTeste","12345678901235","Brazil","12345678","emp2@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        Empresa emp3= new Empresa("empresaTeste","12345678901236","Brazil","12345678","emp3@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)

        Candidato c= new Candidato("candidatoTeste","12345678901",20,"testemail@gmail.com","12345678",Estados.ACRE,"descricaoexemplo",competencias)


        Vaga v= new Vaga("vagaexemplo1","exemplodevaga1","12345678901234",competencias)
        Vaga v2= new Vaga("vagaexemplo2","exemplodevaga2","12345678901235",competencias)
        Vaga v3= new Vaga("vagaexemplo3","exemplodevaga3","12345678901236",competencias)



        gbd.registrarEmpresa(emp)
        Integer idEmpresaDesejado= gbd.registrarEmpresa(emp2)
        gbd.registrarEmpresa(emp3)

        gbd.registrarCandidato(c)

        Integer vagaCurtida=gbd.registrarVaga(v)
        Integer IdEsperado = gbd.registrarVaga(v2)
        Integer vagaMatchId = gbd.registrarVaga(v3)
        gbd.registrarCurtidaVaga(c.getCpf(),vagaCurtida)
        gbd.registrarMatch(c.getCpf(),emp3.getCnpj(),vagaMatchId)

        when:"o candidao realiza uma solitação para ver vagas"
        List<GroovyRowResult> linhas= gbd.listagemVagasCandidato(c.getCpf())

        then: "só deverão ser retornadas vagas noa interagidas pelo candidato "

        linhas.size()==1
        linhas[0].id_vaga==IdEsperado
        linhas[0].nome=="vagaexemplo2"
        linhas[0].id_contratante==idEmpresaDesejado


    }

    def "buscador matchs para empresa / candidato"() {
        given: "um match entre empresa e candidato ,um candidato extra para teste e uma vaga como base"

        Empresa emp = new Empresa("empresaTeste", "12345678901234", "Brazil", "12345678", "emp1@gmail", Estados.RIODEJANEIRO, "descricaoexemplo", competencias)
        Candidato c1 = new Candidato("candidatoTeste", "12345678901", 20, "testemail1@gmail.com", "12345678", Estados.ACRE, "descricaoexemplo", competencias)
        Candidato c2 = new Candidato("candidatoTeste", "12345678902", 20, "testemail2@gmail.com", "12345678", Estados.ACRE, "descricaoexemplo", competencias)

        Vaga v = new Vaga("vagaexemplo1", "exemplodevaga1", "12345678901234", competencias)

        gbd.registrarEmpresa(emp)
        Integer idVaga = gbd.registrarVaga(v)
        gbd.registrarCandidato(c1)
        gbd.registrarCandidato(c2)
        gbd.registrarCurtidaVaga(c2.getCpf(), idVaga)

        gbd.registrarMatch(c1.getCpf(), emp.getCnpj(), idVaga)

        when: "os usuarios realizam a busca por seus matchs"

        List<GroovyRowResult> matchsEmpresa = gbd.buscarMatches(emp.getCnpj())
        List<GroovyRowResult> matchsC1 = gbd.buscarMatches(c1.getCpf())
        List<GroovyRowResult> matchsC2 = gbd.buscarMatches(c2.getCpf())

        then: "Empresa e candidato com match devem ser apresentados um pro outro , e o candidato match nao deve ter matchs registrados"

        matchsEmpresa.size() == 1
        matchsC1.size() == 1
        matchsC2.size() == 0

        matchsEmpresa[0].nome == c1.getNome()
        matchsC1[0].nome == emp.getNome()



    }


    def "teste remoção perfil"(){
        given:"dois perfis cadastrados, com vagas , curtidas e matchs os envolvendo"

        Empresa emp = new Empresa("empresaTeste", "12345678901234", "Brazil", "12345678", "emp1@gmail", Estados.RIODEJANEIRO, "descricaoexemplo", competencias)
        Candidato c = new Candidato("candidatoTeste", "12345678901", 20, "testemail1@gmail.com", "12345678", Estados.ACRE, "descricaoexemplo", competencias)

        Vaga v = new Vaga("vagaexemplo1", "exemplodevaga1", "12345678901234", competencias)

        gbd.registrarEmpresa(emp)
        Integer idVaga = gbd.registrarVaga(v)
        gbd.registrarCandidato(c)
        gbd.registrarCurtidaVaga(c.getCpf(), idVaga)
        gbd.registrarMatch(c.getCpf(),emp.getCnpj(),idVaga)

        when:"ambos os usuarios deletam seus perfis"

        gbd.removerPerfil(emp.cnpj)
        gbd.removerPerfil(c.cpf)

        List<GroovyRowResult> UsuariosCadastrados = sqlH2.rows"SELECT * FROM usuario"
        List<GroovyRowResult> especialidades = sqlH2.rows"SELECT * FROM especialidade_usuario"
        List<GroovyRowResult> especialidadesVagas = sqlH2.rows "SELECT * FROM especialidade_vaga"
        List<GroovyRowResult> matchs =sqlH2.rows "SELECT * FROM matchs"
        List<GroovyRowResult> curtidas =sqlH2.rows "SELECT * FROM curtida"
        List<GroovyRowResult> empresas =sqlH2.rows "SELECT * FROM empresa"
        List<GroovyRowResult> candidatos = sqlH2.rows "SELECT * FROM candidato"
        List<GroovyRowResult> vagas = sqlH2.rows "SELECT * FROM vaga"

        List<List<GroovyRowResult>> valoresDeletados=[UsuariosCadastrados,especialidades,especialidadesVagas,matchs,curtidas,empresas,candidatos,vagas]

        then: "todas as linhas do banco que envolverem de algum modo o perfil deletado devem ser excluidas "

        valoresDeletados.forEach {
            valores-> !valores
        }



    }

    def "Teste de captura de informaçoes de registro"(){

        given:"empresa e candidato cadastrados"

        Empresa emp = new Empresa("empresaTeste", "12345678901234", "Brazil", "12345678", "empresa@gmail", Estados.RIODEJANEIRO, "descricaoempresa", competencias)
        Candidato c = new Candidato("candidatoTeste", "12345678901", 20, "candidatocontato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidato", competencias)

        gbd.registrarCandidato(c)
        gbd.registrarEmpresa(emp)


        when:"os dados sao solicitados"

        GroovyRowResult dadosCandidato = gbd.capturarInfos(c.getCpf())
        GroovyRowResult dadosEmpresa= gbd.capturarInfos((emp.getCnpj()))

        then:"os dados devem ser retornados de forma correta"

        dadosCandidato.nome=="candidatoTeste"
        dadosCandidato.email=="candidatocontato@gmail.com"
        dadosCandidato.cep=="87654321"
        dadosCandidato.estado=="AC"
        dadosCandidato.descricao=="descricaoCandidato"
        dadosCandidato.idade==20
        dadosCandidato.competencias=="JAV, ANG, PT, HT"

        dadosEmpresa.nome=="empresaTeste"
        dadosEmpresa.email=="empresa@gmail"
        dadosEmpresa.cep=="12345678"
        dadosEmpresa.descricao=="descricaoempresa"
        dadosEmpresa.pais=="Brazil"
        dadosEmpresa.competencias=="JAV, ANG, PT, HT"


    }

    def "Teste captura id"(){
        given:"dois usuarios cadastrados"

        Empresa emp = new Empresa("empresaTeste", "12345678901234", "Brazil", "12345678", "empresa@gmail", Estados.RIODEJANEIRO, "descricaoempresa", competencias)
        Candidato c = new Candidato("candidatoTeste", "12345678901", 20, "candidatocontato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidato", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)

        when:"É feita a requissiçãos seus ids"

        Integer idEmpresa=gbd.capturarIdUsurio(emp.getCnpj())
        Integer idCandidato= gbd.capturarIdUsurio(c.getCpf())

        then:"Os ids devem ser retornados corretamente"
        idEmpresa==1
        idCandidato==2
    }

    def "teste edição nome"(){
        given:"uma empresa cadastrada e um candidato cadastrado"

        Empresa emp = new Empresa("nomeAntigoEmpresa", "12345678901234", "Brazil", "12345678", "empresa@gmail", Estados.RIODEJANEIRO, "descricaoempresa", competencias)
        Candidato c = new Candidato("nomeAntigoCandidato", "12345678901", 20, "candidatocontato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidato", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)

        Integer idEmpresa=gbd.capturarIdUsurio(emp.getCnpj())
        Integer idCandidato=gbd.capturarIdUsurio(c.getCpf())

        when:"uma mudança de nome é solicitada"

        gbd.trocarNomeDoUsuario(idEmpresa,"novoNomeEmpresa")
        gbd.trocarNomeDoUsuario(idCandidato,"novoNomeCandidato")

        GroovyRowResult informacoesEmpresa = gbd.capturarInfos(emp.getCnpj())
        GroovyRowResult informacesCandidato= gbd.capturarInfos(c.getCpf())

        then:"Os nomes devem ser trocados"
        informacoesEmpresa.nome=="novoNomeEmpresa"
        informacesCandidato.nome=="novoNomeCandidato"

    }


    def "teste edição email"(){
        given:"uma empresa cadastrada e um candidato cadastrado"

        Empresa emp = new Empresa("nomeAntigoEmpresa", "12345678901234", "Brazil", "12345678", "velhoempresa@gmail", Estados.RIODEJANEIRO, "descricaoempresa", competencias)
        Candidato c = new Candidato("nomeAntigoCandidato", "12345678901", 20, "velhocandidato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidato", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)

        Integer idEmpresa=gbd.capturarIdUsurio(emp.getCnpj())
        Integer idCandidato=gbd.capturarIdUsurio(c.getCpf())

        when:"uma mudança de email é solicitada"

        gbd.trocarEmailDoUsuario(idEmpresa,"novoemailempresa@gmail.com")
        gbd.trocarEmailDoUsuario(idCandidato,"novoemmailcandidato@gmail.com")

        GroovyRowResult informacoesEmpresa = gbd.capturarInfos(emp.getCnpj())
        GroovyRowResult informacesCandidato= gbd.capturarInfos(c.getCpf())

        then:"Os emails devem ser trocados"
        informacoesEmpresa.email=="novoemailempresa@gmail.com"
        informacesCandidato.email=="novoemmailcandidato@gmail.com"

    }

    def "teste edição cep"(){
        given:"uma empresa cadastrada e um candidato cadastrado"

        Empresa emp = new Empresa("nomeAntigoEmpresa", "12345678901234", "Brazil", "12345678", "velhoempresa@gmail", Estados.RIODEJANEIRO, "descricaoempresa", competencias)
        Candidato c = new Candidato("nomeAntigoCandidato", "12345678901", 20, "velhocandidato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidato", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)

        Integer idEmpresa=gbd.capturarIdUsurio(emp.getCnpj())
        Integer idCandidato=gbd.capturarIdUsurio(c.getCpf())

        when:"uma mudança de cep é solicitada"

        gbd.trocarCepDoUsuario(idEmpresa,"23456789")
        gbd.trocarCepDoUsuario(idCandidato,"76543289")

        GroovyRowResult informacoesEmpresa = gbd.capturarInfos(emp.getCnpj())
        GroovyRowResult informacesCandidato= gbd.capturarInfos(c.getCpf())

        then:"Os ceps devem ser trocados"
        informacoesEmpresa.cep=="23456789"
        informacesCandidato.cep=="76543289"

    }

    def "teste edição descricao"(){
        given:"uma empresa cadastrada e um candidato cadastrado"

        Empresa emp = new Empresa("nomeAntigoEmpresa", "12345678901234", "Brazil", "12345678", "velhoempresa@gmail", Estados.RIODEJANEIRO, "descricaoempresavelha", competencias)
        Candidato c = new Candidato("nomeAntigoCandidato", "12345678901", 20, "velhocandidato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidatovelha", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)

        Integer idEmpresa=gbd.capturarIdUsurio(emp.getCnpj())
        Integer idCandidato=gbd.capturarIdUsurio(c.getCpf())

        when:"uma mudança de descricao é solicitada"

        gbd.trocarDescricaoDoUsuario(idEmpresa,"novaDescricaoEmpresa")
        gbd.trocarDescricaoDoUsuario(idCandidato,"novaDescricaoCandidato")

        GroovyRowResult informacoesEmpresa = gbd.capturarInfos(emp.getCnpj())
        GroovyRowResult informacesCandidato= gbd.capturarInfos(c.getCpf())

        then:"As descrições devem ser trocadas"
        informacoesEmpresa.descricao=="novaDescricaoEmpresa"
        informacesCandidato.descricao=="novaDescricaoCandidato"

    }

    def"Teste de troca de idade de candidato"(){
        given:"Dois candidatos cadastrados"

        Candidato c1 = new Candidato("nomeAntigoCandidato", "12345678901", 20, "candidato1@gmail.com", "87654321", Estados.ACRE, "descricaoCandidatovelha", competencias)
        Candidato c2 = new Candidato("nomeAntigoCandidato", "12345678902", 25, "candidato2@gmail.com", "87654321", Estados.ACRE, "descricaoCandidatovelha", competencias)

        gbd.registrarCandidato(c1)
        gbd.registrarCandidato(c2)

        when:"Os candidato solicitam mudaça de idade"

        gbd.trocarIdadeDoCandidato(c1.getCpf(),18)
        gbd.trocarIdadeDoCandidato(c2.getCpf(),20)


        GroovyRowResult informacesCandidato1= gbd.capturarInfos(c1.getCpf())
        GroovyRowResult informacesCandidato2= gbd.capturarInfos(c2.getCpf())

        then:"as idades devem ser trocadas de forma correta"
        informacesCandidato1.idade==18
        informacesCandidato2.idade==20



    }

    def "troca de especialidade do usuario" () {

        given: "um candidato/empresa cadastrado"

        Candidato c = new Candidato("nomeAntigoCandidato", "12345678901", 20, "velhocandidato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidatovelha", competencias)

        Integer idUsuario = gbd.registrarCandidato(c)
        ArrayList<Especialidades> novasEspecialidades = [Especialidades.HT,Especialidades.CS]

        when:"candidato solicita troca de especilidades do perfil"
        gbd.trocarEspecialidadesDoUsuario(idUsuario,novasEspecialidades)

        GroovyRowResult infosUsuario = gbd.capturarInfos(c.getCpf())

        then:"especialidades devems ser trocadas de acordo"
        infosUsuario.competencias=="HT, CS"



    }

    def "troca de pais da empresa"(){
        given:"uma empresa cadastrada "

        Empresa emp = new Empresa("nomeAntigoEmpresa", "12345678901234", "paisVelho", "12345678", "velhoempresa@gmail", Estados.RIODEJANEIRO, "descricaoempresavelha", competencias)

        gbd.registrarEmpresa(emp)

        when:"Solicitação de mundança de pais"

        gbd.trocarPaisDaEmpresa(emp.getCnpj(),"novoPais")

        GroovyRowResult infoEmpresa=gbd.capturarInfos(emp.getCnpj())

        then:"a mudnaça de apis deve ser aplicada"

        infoEmpresa.pais=="novoPais"
    }

    def "teste captura de vagas"(){
        given:"duas empresas com vagas cadastradas"

        Empresa emp= new Empresa("empresaTeste","12345678901234","Brazil","12345678","emp1@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)
        Empresa emp2= new Empresa("empresaTeste","12345678901235","Brazil","12345678","emp2@gmail",Estados.RIODEJANEIRO,"descricaoexemplo",competencias)


        Vaga v= new Vaga("vagaexemplo1","exemplodevaga1","12345678901234",competencias)
        Vaga v2= new Vaga("vagaexemplo2","exemplodevaga2","12345678901235",competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarEmpresa(emp2)

        gbd.registrarVaga(v)
        gbd.registrarVaga(v2)

        when :"As respectivas empresas requisitarem suas vagas"
        List<GroovyRowResult> vagasemp = gbd.capturarVagasDaEmpresa(emp.getCnpj())
        List<GroovyRowResult> vagasemp2 = gbd.capturarVagasDaEmpresa(emp2.getCnpj())

        then:"Devem serr retonadas apenas as vagas da empresa requerente"

        vagasemp.size()==1
        vagasemp2.size()==1

        vagasemp[0].contratante==emp.getCnpj()
        vagasemp2[0].contratante==emp2.getCnpj()
    }


    def "Teste de captura de candidatos interessados sem match"() {
        given: "Uma empresa, um candidato e uma vaga cadastrados"
        Empresa emp = new Empresa("Empresa Teste", "12345678000199", "brazil", "12345678", "emp@teste.com", Estados.RIODEJANEIRO, "Desc", competencias)
        Candidato c = new Candidato("nomeCandidato", "12345678901", 20, "velhocandidato@gmail.com", "87654321", Estados.ACRE, "descricaoCandidatovelha", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(c)
        Vaga v = new Vaga("nomeExemplo", "descExemplo", emp.getCnpj(), competencias)
        Integer idVaga = gbd.registrarVaga(v)




        when: "A empresa busca por candidatos interessados e o candiato ja curtiu a vaga"
        gbd.registrarCurtidaVaga(c.getCpf(), idVaga)
        List<Map> interessados = gbd.capturarCandidatosInteressados(idVaga, emp.getCnpj())

        then: "O candidato deve aparecer na lista"
        interessados.size() == 1
        interessados[0].nome == "nomeCandidato"
        interessados[0].competencias == "JAV, ANG, PT, HT"
    }

    def "Teste de captura de candidatos interessados ignorando quem já possui match"() {
        given: "Uma empresa, dois candidatos e uma vaga, onde ambos curtem , mas apenas 1 tem match"

        Empresa emp = new Empresa("empresaTeste", "12345678901234", "brazil", "12345678", "emp@teste.com", Estados.RIODEJANEIRO, "desc", competencias)
        Candidato candidatoComMatch = new Candidato("c1", "12345678901", 20, "c1@teste.com", "11111111", Estados.ACRE, "desc", competencias)
        Candidato candidatoSemMatch = new Candidato("c2", "12345678902", 30, "c2@teste.com", "22222222", Estados.ALAGOAS, "desc", competencias)

        gbd.registrarEmpresa(emp)
        gbd.registrarCandidato(candidatoComMatch)
        gbd.registrarCandidato(candidatoSemMatch)

        Vaga v = new Vaga("vagaTeste", "desc", emp.getCnpj(), competencias)
        Integer idVaga = gbd.registrarVaga(v)



        gbd.registrarCurtidaVaga(candidatoComMatch.getCpf(), idVaga)
        gbd.registrarCurtidaVaga(candidatoSemMatch.getCpf(), idVaga)
        gbd.registrarMatch(candidatoComMatch.getCpf(), emp.getCnpj(), idVaga)

        when: "A empresa busca por candidatos interessados"
        List<Map> interessados = gbd.capturarCandidatosInteressados(idVaga, emp.getCnpj())

        then: "Apenas o candidato SEM match deve ser retornado "

        interessados.size() == 1
        interessados[0].nome == "c2"


    }

    def "Teste edicao nome/descricao da vaga"(){
        given:"uma empresa e uma vaga cadastrada"

        Empresa emp = new Empresa("empresaTeste", "12345678901234", "brazil", "12345678", "emp@teste.com", Estados.RIODEJANEIRO, "desc", competencias)
        gbd.registrarEmpresa(emp)

        Vaga v = new Vaga("vagaTeste", "desc", emp.getCnpj(), competencias)
        Integer idVaga = gbd.registrarVaga(v)



        when:"Empresa soliciata edicao"

        gbd.trocarNomeDaVaga(idVaga,"novo nome")
        gbd.trocarDescricaoDaVaga(idVaga,"nova descricao")

        List<GroovyRowResult> vagas = gbd.capturarVagasDaEmpresa(emp.getCnpj())

        then: "Nome e descrição devem ser alterados corretamente"

        vagas[0].nome=="novo nome"
        vagas[0].descricao=="nova descricao"
    }

    def "Teste de filtro por estado"() {

        given: "candidatos/empresas cadastrados em diferentes estados"

        Candidato candidatoRio= new Candidato("cRio", "12345678902", 30, "c@teste.com", "22222222", Estados.RIODEJANEIRO, "desc", competencias)
        Candidato candidatoAlagoas = new Candidato("cAlagoas", "12345678903", 30, "c2@teste.com", "22222222", Estados.ALAGOAS, "desc", competencias)

        gbd.registrarCandidato(candidatoRio)
        Integer idCandidatoAlagoas= gbd.registrarCandidato(candidatoAlagoas)

        Empresa empRio = new Empresa("pRio", "12345678901234", "brazil", "12345678", "email@teste.com", Estados.RIODEJANEIRO, "desc", competencias)
        Empresa empAlagoas = new Empresa("pAlagoas", "12345678901235", "brazil", "12345678", "emp@teste.com", Estados.ALAGOAS, "desc", competencias)

        Integer idEmpresaRio = gbd.registrarEmpresa(empRio)
        gbd.registrarEmpresa(empAlagoas)

        when:"há uma requisição de filtragem passando um estado feita por usuario "

        List<GroovyRowResult> resultadoCandidatoRio=gbd.buscarEmpresasPorEstado(Estados.RIODEJANEIRO)
        List<GroovyRowResult> resultadoEmpresaAlagoas=gbd.buscarCandidatosPorEstado(Estados.ALAGOAS)

        then:"Devem ser retornados os usuarios de outra categoria com o estado informado "

        resultadoCandidatoRio.size()==1
        resultadoCandidatoRio[0].id==idEmpresaRio

        resultadoEmpresaAlagoas.size()==1
        resultadoEmpresaAlagoas[0].id==idCandidatoAlagoas



    }

    def "Teste de filtro por habilidades mínimas"() {

        given:
        List<Especialidades> habilidadesObrigatorias = [Especialidades.JAV, Especialidades.ANG]
        List<Especialidades> habilidadesInsuficientes = [Especialidades.JAV,Especialidades.PT]


        Candidato candPart = new Candidato("nomeCandidatoInvalido","12345678901",20,"mail@gamil.com","12345678",Estados.ALAGOAS,"desc",habilidadesInsuficientes)
        Candidato candFull = new Candidato("nomeCandidatoValido","12345678902",20,"mail2@gamil.com","12345678",Estados.ALAGOAS,"desc",habilidadesObrigatorias)

        Integer idCandFull = gbd.registrarCandidato(candFull)
        gbd.registrarCandidato(candPart)

        Empresa empRequisitante = new Empresa("Emp Busca", "12345678901234", "brazil", "12345678", "e@t.com", Estados.RIODEJANEIRO, "desc",habilidadesInsuficientes)
        gbd.registrarEmpresa(empRequisitante)

        List<Especialidades> filtroHabilidades = [Especialidades.JAV,Especialidades.ANG]

        when:
        List<GroovyRowResult> resultadoDeCandidatos = gbd.buscarCandidatosPorHabilidades(filtroHabilidades)
        List<GroovyRowResult> resultadoBuscandoEmpresa= getGbd().buscarEmpresasPorHabilidades(filtroHabilidades)

        then:
        resultadoDeCandidatos.size() == 1
        resultadoDeCandidatos[0].id == idCandFull
        resultadoDeCandidatos[0].habilidades.contains("JAV")
        resultadoDeCandidatos[0].habilidades.contains("ANG")


        resultadoBuscandoEmpresa.size()==0

    }

    def "Teste remoção de vagas"(){
        given: "uma empresa e uma vaga cadastrada"


        Empresa emp = new Empresa("empresaTeste", "12345678901234", "brazil", "12345678", "emp@teste.com", Estados.RIODEJANEIRO, "desc", competencias)

        gbd.registrarEmpresa(emp)


        Vaga v = new Vaga("vagaTeste", "desc", emp.getCnpj(), competencias)
        Integer idVaga = gbd.registrarVaga(v)

        when:"empresa solicita a remoção da vaga"

        gbd.removerVaga(idVaga)

        List<GroovyRowResult> vagas= gbd.capturarVagasDaEmpresa(emp.getCnpj())

        then:"a vaga deve ser removida corretamente"

        vagas.size()==0





    }


}