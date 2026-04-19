package Model.Objetos


import Model.Enuns.Especialidades
import groovy.transform.Canonical

@Canonical

class Vaga {
    String nome
    String descricao
    String contratante
    ArrayList<Especialidades> requisitos


}
