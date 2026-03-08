import  { Usuario } from "./usuario";
import type {Estado,Especialidade,Formacao} from "../tiposFixos"

export class Candidato extends Usuario {
    cpf: string;
    idade: number;
    formacao: Formacao;
    


    constructor(
        nome: string,
        cpf: string,
        idade:number,
        email: string,
        cep: string,
        estado: Estado,
        descricao:string,
        competencias: Especialidade[],
        formacao: Formacao
        
    )
    
    {
        super(nome,descricao, email, cep, estado, competencias);
        this.cpf = cpf;
        this.idade = idade;
        this.formacao = formacao;


    }


} 




