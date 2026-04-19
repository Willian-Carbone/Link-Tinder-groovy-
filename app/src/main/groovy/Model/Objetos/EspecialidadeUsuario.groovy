package Model.Objetos

import Model.Enuns.Especialidades
import groovy.transform.Canonical

@Canonical

class EspecialidadeUsuario {
    Integer idUsuario
    Especialidades especialidade
}
