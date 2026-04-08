package Terminais

import Enuns.*
import Metodos.*
import Objetos.Empresa

class CadastroEmpresa {

    static void terminalEmpresa (GerenciadorBancoDados gerenciadorBancoDados,Scanner scan){



        println "--- Cadastro de Empresa ---"

        String nome = ControladorTerminal.solicitarNomeValido(scan)
        String cnpj = ControladorTerminal.solicitarCnpjValido(scan ,gerenciadorBancoDados)
        String email = ControladorTerminal.solicitarEmailValido(scan,gerenciadorBancoDados)
        String desc= ControladorTerminal.solicitarDescricao(scan)
        String pais = ControladorTerminal.solicitarPais(scan)
        String cep = ControladorTerminal.solicitarCepValido(scan)
        Estados estado = ControladorTerminal.solicitarEstadoValido(scan)
        ArrayList<Especialidades> competencias = ControladorTerminal.solicitarConjuntoEspecialidadesValidas(scan)


        println("Empresa cadastrada com sucesso")

        Empresa empresa= new Empresa (nome,cnpj,pais,cep,email,estado,desc,competencias)

        gerenciadorBancoDados.registrarEmpresa(empresa)







    }

}
