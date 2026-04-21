package Controller.Cadastros

import Daos.BuscadoresDeInformacao.ConfirmadorExistenciaCnpj
import Daos.BuscadoresDeInformacao.ConfirmadorExistenciaEmail
import Daos.Facade.FacadeRegistroGeral
import Daos.GerenciadorEmpresa

import Model.Enuns.Especialidades
import Model.Enuns.Estados
import Model.Objetos.Empresa

import Model.validadoresDeEntradas.ValidadorCep
import Model.validadoresDeEntradas.ValidadorCnpj
import Model.validadoresDeEntradas.ValidadorEmail
import Model.validadoresDeEntradas.ValidadorEstado
import Model.validadoresDeEntradas.ValidadorNome
import Utilits.ConversorDados.ConversorParaNomeEnum
import Utilits.ConversorDados.RemovedorNaoDigitos
import Utilits.LocalizadorDadoEmEnum.LocalizadorEstadoEmEnum
import View.Requisitores.RequisidorDeEntradas

import groovy.sql.Sql

class CadastroEmpresa implements TerminalCadastro {

    @Override
     void cadastrar(Sql conexao, Scanner scan){



        println "--- Cadastro de Empresa ---"



        String nome = RequisidorDeEntradas.solicitarDadoBasicoValido("nome",scan,new ValidadorNome())

        String cnpj = RequisidorDeEntradas.solicitarCredencialValida(scan ,new ValidadorCnpj(),new ConfirmadorExistenciaCnpj(conexao),"CNPJ", new RemovedorNaoDigitos())

        String email = RequisidorDeEntradas.solicitarCredencialValida(scan,new ValidadorEmail(), new ConfirmadorExistenciaEmail(conexao),"EMAIL")

        String desc= RequisidorDeEntradas.solicitarDescricao(scan)
        String pais = RequisidorDeEntradas.solicitarPais(scan)

        String cep = RequisidorDeEntradas.solicitarDadoBasicoValido("CEP" ,scan, new ValidadorCep())

        String estadoNome = RequisidorDeEntradas.solicitarDadoBasicoValido( "ESTADO",scan,new ValidadorEstado(), new ConversorParaNomeEnum())

        Estados estado = new LocalizadorEstadoEmEnum().capturarDadoEnum(estadoNome)

        ArrayList<Especialidades> competencias = RequisidorDeEntradas.solicitarConjuntoEspecialidadesValidas(scan)


        Empresa empresa= new Empresa (nome,cnpj,pais,cep,email,estado,desc,competencias)

        new FacadeRegistroGeral().realizarRequisicao(conexao,empresa)

        new GerenciadorEmpresa(conexao).criarPerfil(empresa)

        println("Empresa cadastrada com sucesso")








    }

}
