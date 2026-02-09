enum Especialidades {

    PT ("phyton"),
    JAV ("java"),
    ANG ("Angular"),
    SPR ("Spring"),
    HT ("Html"),
    CS ("Css")

    String valor

    Especialidades (String valor){

        this.valor = valor

    }

    String get_valor (){
        return this.valor
    }



}