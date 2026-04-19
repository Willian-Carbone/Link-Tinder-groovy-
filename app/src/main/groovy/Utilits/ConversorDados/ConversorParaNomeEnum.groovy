package Utilits.ConversorDados


import java.text.Normalizer

class ConversorParaNomeEnum implements ConversorDadoI<String,String> {


    @Override
    String converterDado(String nome) {
       return  Normalizer.normalize(nome,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll(" ","").toUpperCase()
    }
}
