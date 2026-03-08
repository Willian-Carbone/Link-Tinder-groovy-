import { Especialidade } from "../tiposFixos"
import { Candidato } from "./candidato"
export class Vaga{

    nome:string
    descricao:string
    contratante:string
    requisitos:Especialidade[]
    curtidas:string[]

    constructor(nome:string,descricao:string,contratante:string,requisitos:Especialidade[]){
        this.nome = nome
        this.descricao = descricao
        this.contratante = contratante
        this.requisitos = requisitos
        this.curtidas = []
    }



}


