package Terminais.Cadastros

import Enuns.*
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaCnpj
import GerenciadoresDeBanco.BuscadoresDeInformacao.ConfirmadorExistenciaEmail
import GerenciadoresDeBanco.GerenciadorEmpresa
import GerenciadoresDeBanco.GerenciadorUsuario
import Modulos.ConversoresEntrada.ConversorParaNomeEnum
import Modulos.ConversoresEntrada.RemovedorNaoDigitos
import Modulos.GerenciadoresTerminal.Requisitores.RequisidorDeEntradas
import Modulos.LocalizadorDadoEmEnum.LocalizadorEstado
import Modulos.validadoresDeEntradas.ValidadorCep
import Modulos.validadoresDeEntradas.ValidadorCnpj
import Modulos.validadoresDeEntradas.ValidadorEmail
import Modulos.validadoresDeEntradas.ValidadorEstado
import Modulos.validadoresDeEntradas.ValidadorNome
import Objetos.Empresa
import Objetos.EspecialidadeUsuario
import groovy.sql.Sql

class CadastroEmpresa implements TerminalCadastro {

    @Override
     void cadastrar(Sql conexao, Scanner scan){



        println "--- Cadastro de Empresa ---"

        GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario(conexao)

        String nome = RequisidorDeEntradas.solicitarDadoBasicoValido("nome",scan,new ValidadorNome())

        String cnpj = RequisidorDeEntradas.solicitarCredencialValida(scan ,new ValidadorCnpj(),new ConfirmadorExistenciaCnpj(conexao),"CNPJ", new RemovedorNaoDigitos())

        String email = RequisidorDeEntradas.solicitarCredencialValida(scan,new ValidadorEmail(), new ConfirmadorExistenciaEmail(conexao),"EMAIL")

        String desc= RequisidorDeEntradas.solicitarDescricao(scan)
        String pais = RequisidorDeEntradas.solicitarPais(scan)

        String cep = RequisidorDeEntradas.solicitarDadoBasicoValido("CEP" ,scan, new ValidadorCep())

        String estadoNome = RequisidorDeEntradas.solicitarDadoBasicoValido( "ESTADO",scan,new ValidadorEstado(), new ConversorParaNomeEnum())

        Estados estado = new LocalizadorEstado().capturarDadoEnum(estadoNome)

        ArrayList<Especialidades> competencias = RequisidorDeEntradas.solicitarConjuntoEspecialidadesValidas(scan)




        Empresa empresa= new Empresa (nome,cnpj,pais,cep,email,estado,desc,competencias)

        Integer idGerado = gerenciadorUsuario.criarPerfilUsuario(empresa)

        empresa.identificador=idGerado

        new GerenciadorEmpresa(conexao).criarPerfil(empresa)


        empresa.competencias.forEach {Especialidades especialidade->
            EspecialidadeUsuario especialidadeUsuario = new EspecialidadeUsuario(idGerado,especialidade)
            gerenciadorUsuario.gravarEspecialidadeUsusario(especialidadeUsuario)

        }

        println("Empresa cadastrada com sucesso")








    }

}
