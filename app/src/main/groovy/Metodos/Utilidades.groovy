package Metodos

import Enuns.Especialidades
import Enuns.Estados

import java.text.Normalizer

abstract class Utilidades {

    static boolean validadorCep (String cep){
        return cep ==~ /\d{5}-\d{3}/ || cep ==~ /\d{8}/
    }

    static boolean validadorNome(String nome){
        return (nome ==~ /(?U)^\p{L}{2,} [\p{L} ]+$/)
    }

    static boolean confirmacao(String mensagem, Scanner scan) {
        println("${mensagem} (S/N)")
        String entrada = scan.nextLine().toUpperCase()


        while (entrada != "S" && entrada != "N") {
            println("Entrada inválida. Por favor, insira S ou N:")
            entrada = scan.nextLine().toUpperCase()
        }

        return entrada == "S"
    }










    static String padronizaEntrada (String entrada){

        if (entrada == null){
            return "Dado não informado"
        }

        return entrada.replaceAll(/\D/, "")

    }




    static boolean checarCompetencia(String competencia){
        return Especialidades.values().any {it.name() == competencia}
    }



    static String normalizador (String estado){

        if(estado==null) {return null}

        String normalizado = Normalizer.normalize(estado,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll(" ","").toUpperCase()
       if ( Estados.values().any {it.name() ==normalizado})
       {
           return normalizado
       }

        else{
          return null
       }

    }




    static boolean validadorCnpj (String cnpj){
        return (cnpj ==~ /\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}/ || cnpj ==~ /\d{14}/)

    }

    static boolean validadorEmail (String email){
        return (email ==~ /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}/)
    }

    static boolean validadorCpf (String cpf){
        return (cpf ==~ /\d{3}\.\d{3}\.\d{3}-\d{2}/ || cpf ==~ /\d{11}/)


    }

    static boolean validadorIdade(String idade){
        try{
            return Integer.parseInt(idade)>=18
        }
        catch (ignored){
            return false
        }
    }











}