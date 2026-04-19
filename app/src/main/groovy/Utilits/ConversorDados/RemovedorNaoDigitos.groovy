package Utilits.ConversorDados

class RemovedorNaoDigitos implements ConversorDadoI<String ,String> {
    @Override
    String converterDado(String dado) {
        if (dado == null){
            return null
        }

        return dado.replaceAll(/\D/, "")
    }
}
