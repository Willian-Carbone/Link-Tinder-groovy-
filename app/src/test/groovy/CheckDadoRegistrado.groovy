import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCnpj
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCpf
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaEmail

class CheckDadoRegistrado extends FakeBancoConfigure{

    def "Teste de confirmação da existencia de um cnpj no banco"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresa','emailemp@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO empresa (cnpj,pais,empresa_id) VALUES (?,?,?)", ["12345678901234", "brasil", 1])

        ConfirmadorExistenciaCnpj confirmadorExistenciaCnpj = new ConfirmadorExistenciaCnpj(sqlH2)

        when:

        boolean cnpjExistente =confirmadorExistenciaCnpj.buscarExistenciaDado("12345678901234")
        boolean  cnpjInexistente =confirmadorExistenciaCnpj.buscarExistenciaDado("123")
        boolean  testeNull = confirmadorExistenciaCnpj.buscarExistenciaDado(null)

        then:
        cnpjExistente
        !cnpjInexistente
        !testeNull


    }


    def "Teste de validação se cpf existe no banco"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('empresa','emailemp@gm','12345678','RJ','desc')")
        sqlH2.executeInsert("INSERT INTO candidato (cpf,idade,candidato_id) VALUES (?,?,?)", ["12345678901", 20,1])

        ConfirmadorExistenciaCpf confirmadorExistenciaCpf = new ConfirmadorExistenciaCpf(sqlH2)

        when:

        boolean cpfExistente =confirmadorExistenciaCpf.buscarExistenciaDado("12345678901")
        boolean  cpfInexistente =confirmadorExistenciaCpf.buscarExistenciaDado("123")
        boolean  testeNull = confirmadorExistenciaCpf.buscarExistenciaDado(null)

        then:
        cpfExistente
        !cpfInexistente
        !testeNull


    }

    def "Teste de validação se email existe no banco"(){
        given:
        sqlH2.executeInsert("INSERT INTO usuario (nome, email, cep, estado,descricao) VALUES ('nome','emailexemplo@gmail','12345678','RJ','desc')")


        ConfirmadorExistenciaEmail confirmadorExistenciaEmail = new ConfirmadorExistenciaEmail(sqlH2)

        when:

        boolean emailExistente =confirmadorExistenciaEmail.buscarExistenciaDado("emailexemplo@gmail")
        boolean  emailInexistente =confirmadorExistenciaEmail.buscarExistenciaDado("emailnaoExiste@gmail")
        boolean  testeNull = confirmadorExistenciaEmail.buscarExistenciaDado(null)

        then:
        emailExistente
        !emailInexistente
        !testeNull


    }


}
