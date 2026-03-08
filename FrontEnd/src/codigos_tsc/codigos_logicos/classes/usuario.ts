import type { Especialidade,Estado } from "../tiposFixos";

export abstract class Usuario {
    email: string;
    nome: string;
    cep: string;
    estado: string;
    competencias: Especialidade[];
    descricao:string;

    

    constructor( nome:string,descricao:string, email:string, cep:string,  estado:Estado,competencias:Especialidade[]) {
        this.nome = nome;
        this.email = email;
        this.cep = cep;
        this.estado = estado;
        this.competencias = competencias;
        this.descricao = descricao;
    
    }


}