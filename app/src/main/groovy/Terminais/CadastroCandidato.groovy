package Terminais
import Enuns.*
import Metodos.*
import Objetos.Candidato

class CadastroCandidato {

    static void terminalCandidatos (GerenciadorBancoDados gerenciadorBancoDados,Scanner scan){

        println "--- Cadastro de Candidato ---"

         String nome = ControladorTerminal.solicitarNomeValido(scan)
         String cpf = ControladorTerminal.solicitarCpfValido(scan ,gerenciadorBancoDados)
         String email = ControladorTerminal.solicitarEmailValido(scan,gerenciadorBancoDados)
         String desc= ControladorTerminal.solicitarDescricao(scan)
         String idade = ControladorTerminal.solicitarIdadeValida(scan)
         String cep = ControladorTerminal.solicitarCepValido(scan)
         Estados estado = ControladorTerminal.solicitarEstadoValido(scan)
        ArrayList<Especialidades> competencias = ControladorTerminal.solicitarConjuntoEspecialidadesValidas(scan)


        println("Candidato cadastrado com sucesso")

        Candidato candidato = new Candidato (nome,cpf,Integer.parseInt(idade),email,cep,estado,desc,competencias)

        gerenciadorBancoDados.registrarCandidato(candidato)



    }
}
