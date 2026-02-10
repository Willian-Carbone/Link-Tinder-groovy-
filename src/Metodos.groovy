import java.text.Normalizer

abstract class Metodos {

    static boolean validador_cep (String cep){
        return cep ==~ /\d{5}-\d{3}/ || cep ==~ /\d{8}/
    }



    static String padronizar_entrada (String entrada){
        return entrada.replaceAll(/\D/, "")

    }




    static boolean checar_competencia(String competencia){
        return Especialidades.values().any {it.name() == competencia}
    }



    static String normalizador (String estado){
        String normalizado = Normalizer.normalize(estado,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll(" ","").toUpperCase() // biblioteca normalizadora de texto , ndf --> tarnsforma acento em caractere unico, troca todas as ocorrencias do regex (propriedade unicode referente a acento) por nada
       if ( Estados.values().any {it.name() ==normalizado}) // num array de enumns , pegue seu nome e compare com normalizado
       {
           return normalizado
       }
        else{
           return null
       }

    }




    static boolean validador_cnpj (String cnpj){
        return (cnpj ==~ /\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}/ || cnpj ==~ /\d{14}/) // regex referete ao cnpj


    }

    static boolean validador_email (String email){
        return (email ==~ /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}/) //regex referente ao email (letras nuemros . _ % + - em (1 ou mais) @ letras nuemors .- (1 ou mais) . minimo 2 letras
    }

    static boolean validador_cpf (String cpf){
        return (cpf ==~ /\d{3}\.\d{3}\.\d{3}-\d{2}/ || cpf ==~ /\d{11}/) // regex referente ao cpf


    }

    static boolean validador_idade(String idade){
        try{
            return Integer.parseInt(idade)>=18
        }
        catch (ignored){
            return false
        }
    }








}