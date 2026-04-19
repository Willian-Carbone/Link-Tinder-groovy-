import Daos.GerenciadorUsuario
import Model.Objetos.Candidato
import Model.Objetos.EspecialidadeUsuario
import Model.Objetos.Usuario
import Model.Enuns.*
import groovy.sql.GroovyRowResult
import spock.lang.Shared


class GerenciadorUsuarioSpec extends FakeBancoConfigure{

    @Shared GerenciadorUsuario gerenciador

    def "Teste inserção usuário sucesso"(){

        given:

        Usuario usuarioExemplo = new Candidato("nome","cpf",20,"email","cep", Estados.GOIAS,"des",[Especialidades.C])

        gerenciador= new GerenciadorUsuario(sqlH2)

        when:

        Integer idDoUsuarioGravado =gerenciador.criarPerfilUsuario(usuarioExemplo)

        then:

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM usuario WHERE id=?",[idDoUsuarioGravado])

        linhaCapturada.nome==usuarioExemplo.nome
        linhaCapturada.email==usuarioExemplo.email
        linhaCapturada.cep==usuarioExemplo.cep
        linhaCapturada.estado==usuarioExemplo.estado.getSigla()
        linhaCapturada.descricao==usuarioExemplo.descricao


    }

    def "Teste inserção usuário Falha"(){
        given:
        Usuario usuarioInvalido = new Candidato(null,null,null,null,null,null,null,null)
        gerenciador= new GerenciadorUsuario(sqlH2)

        when:

        gerenciador.criarPerfilUsuario(usuarioInvalido)

        then:
        thrown(RuntimeException)
    }

    def "Teste edição de CEP de usuario"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome1','email1@gm','12345678','RJ','desc')")

        gerenciador=new GerenciadorUsuario(sqlH2)

        when:
        gerenciador.editarCepUsuario(1,"87654321")

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM usuario WHERE id=1")

        then:

        linhaCapturada.cep=="87654321"




    }

    def "Teste Edicao de descrição Usuario"() {
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome1','email1@gm','12345678','RJ','desc')")

        gerenciador=new GerenciadorUsuario(sqlH2)

        when:
        gerenciador.editarDescricaoUsuario(1,"novaDesc")

        GroovyRowResult linhaCapturada = sqlH2.firstRow("Select * FROM usuario WHERE id=1")

        then:

        linhaCapturada.descricao == "novaDesc"
    }

    def "Teste edição de email de usuario"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome1','email1@gm','12345678','RJ','desc')")

         gerenciador=new GerenciadorUsuario(sqlH2)

        when:
        gerenciador.editarEmailUsuario(1,"novoEmail@gmail")

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM usuario WHERE id=1")

        then:

        linhaCapturada.email=="novoEmail@gmail"


    }


    def "Teste edição de Estado de usuario"() {
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome1','email1@gm','12345678','RJ','desc')")

        gerenciador=new GerenciadorUsuario(sqlH2)

        when:
        gerenciador.editarEstado(1, Estados.BAHIA)

        GroovyRowResult linhaCapturada = sqlH2.firstRow("Select * FROM usuario WHERE id=1")

        then:

        linhaCapturada.estado == "BH"


    }


    def "Teste edição de nome de usuario"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome1','emial@gm','12345678','RJ','desc')")

        gerenciador=new GerenciadorUsuario(sqlH2)

        when:
        gerenciador.editarNomeUsuario(1,"nomeNovo")

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM usuario WHERE id=1")

        then:

        linhaCapturada.nome=="nomeNovo"




    }

    def "Teste inserção especialidade Usuario sucesso"(){
        given:

        gerenciador = new GerenciadorUsuario(sqlH2)

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','email','12345678','SP','desc')")


        EspecialidadeUsuario especialidadeUsuario = new EspecialidadeUsuario(1, Especialidades.JAV)

        when:

        Integer idDaEspecialidadeGravada = gerenciador.gravarEspecialidadeUsusario(especialidadeUsuario)

        then:

        GroovyRowResult linhaCapturada = sqlH2.firstRow ("Select * FROM especialidade_usuario WHERE id=?",[idDaEspecialidadeGravada])

        linhaCapturada.especialidade==especialidadeUsuario.especialidade.name()
        linhaCapturada.usuario == especialidadeUsuario.idUsuario


    }

    def "Teste inserção especialidade Usuario Falha"(){
        given:
        EspecialidadeUsuario especialidadeUsuario = new EspecialidadeUsuario(null,null)

       gerenciador = new GerenciadorUsuario(sqlH2)
        when:

        gerenciador.gravarEspecialidadeUsusario(especialidadeUsuario)

        then:
        thrown(RuntimeException)
    }

    def "Teste troca especialidade do usuario"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','email','12345678','SP','desc')")
        sqlH2.executeInsert "INSERT INTO especialidade_usuario (especialidade,usuario) VALUES ('JAV',1)"

        gerenciador= new GerenciadorUsuario(sqlH2)

        EspecialidadeUsuario especialidade1 = new EspecialidadeUsuario(1,Especialidades.CS)
        EspecialidadeUsuario especialidade2 = new EspecialidadeUsuario(1,Especialidades.HT)

        when:

        gerenciador.editarEspecialidadeDoUsuario(1,[especialidade1,especialidade2])

        List<GroovyRowResult> linhasCapturadas = sqlH2.rows("SELECT * FROM especialidade_usuario")

        then:

        linhasCapturadas.size()==2
        linhasCapturadas[0].especialidade==especialidade1.especialidade.name()
        linhasCapturadas[1].especialidade==especialidade2.especialidade.name()

    }



    def "Teste de busca de usuario especifico por Habilidade"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('user1','email','12345678','SP','desc')")

        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])

        sqlH2.executeInsert "INSERT INTO especialidade_usuario (especialidade,usuario) VALUES ('JAV',1)"

        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('user2','email2','12345678','SP','desc')")
        sqlH2.executeInsert "INSERT INTO especialidade_usuario (especialidade,usuario) VALUES ('ANG',2)"

        gerenciador = new GerenciadorUsuario(sqlH2)

        when:

        List<GroovyRowResult> usuariosEncontrados =gerenciador.buscarPorHabilidades([Especialidades.JAV],"candidato")

        then:
        usuariosEncontrados.size()==1
        usuariosEncontrados[0].id==1

    }



}
