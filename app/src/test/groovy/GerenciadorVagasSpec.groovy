import Daos.CrudPerfilPrincipal
import Daos.GerenciadorCandidato
import Daos.GerenciadorVaga
import Model.Objetos.Vaga
import Model.Enuns.*
import groovy.sql.GroovyRowResult
import spock.lang.Shared



class GerenciadorVagasSpec extends FakeBancoConfigure {

    @Shared GerenciadorVaga gerenciador

    def "Teste inserção vaga sucesso"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','emial@gm','12345678','RJ','desc')")

        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234" ,"brasil", 1])

        gerenciador = new GerenciadorVaga(sqlH2)

        Vaga vaga = new Vaga("vagaNome","vagadesc","12345678901234",[Especialidades.HT])


        when:

        Integer idGerado=gerenciador.gravarVaga(vaga)





        then:

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM vaga WHERE id=?",[idGerado])

        linhaCapturada.contratante == vaga.contratante
        linhaCapturada.nome == vaga.nome
        linhaCapturada.descricao==vaga.descricao


    }

    def "Teste inserção vaga Falha"(){
        given:
        Vaga vagaFalha = new Vaga(null,null,null)
        gerenciador=new GerenciadorVaga(sqlH2)

        when:

        gerenciador.gravarVaga(vagaFalha)

        then:
        thrown(RuntimeException)
    }


    def "Teste edição nome e descrição vaga"(){

        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','emial@gm','12345678','RJ','desc')")

        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234" ,"brasil", 1])

        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES ('nomeAntigo','descAntiga','12345678901234')")

        gerenciador = new GerenciadorVaga(sqlH2)

        when:

        gerenciador.trocarNomeDaVaga(1,"nomeNovo")
        gerenciador.trocarDescricaoDaVaga(1,"novaDesc")

        GroovyRowResult vagaRegistradaNoBanco= sqlH2.firstRow("SELECT * FROM vaga WHERE id=1")

        then:
        vagaRegistradaNoBanco.nome=="nomeNovo"
        vagaRegistradaNoBanco.descricao=="novaDesc"




    }

    def"Teste busca de interessados numa determinada vaga"(){
        given:

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','x@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','y@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','z@gm','12345678','RJ','desc')")


        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234" ,"brasil", 1])

        sqlH2.executeInsert("INSERT INTO vaga (nome,descricao,contratante) VALUES ('nomeAntigo','descAntiga','12345678901234')")

        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,2])

        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678902", 22,3])

        sqlH2.execute("INSERT INTO curtida (candidato, vaga) VALUES (?, ?)", ["12345678901", 1])

        gerenciador=new GerenciadorVaga(sqlH2)

        CrudPerfilPrincipal controladorCandidato = new GerenciadorCandidato(sqlH2)



        when:

        List<GroovyRowResult> candidatosListados= gerenciador.capturarInteressadosNaVaga(1,"12345678901234",controladorCandidato)

        then:

        candidatosListados.size()==1
        candidatosListados[0].id==2



    }
}
