import java.text.Normalizer

abstract class Metodos {

    static boolean normalizador (String estado){
        String normalizado = Normalizer.normalize(estado,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll(" ","").toUpperCase() // biblioteca normalizadora de texto , ndf --> tarnsforma acento em caractere unico, troca todas as ocorrencias do regex (propriedade unicode referente a acento) por nada
       return Estados.values().any {it.name() ==normalizado} // num array de enumns , pegue seu nome e compare com normalizado


    }

}
