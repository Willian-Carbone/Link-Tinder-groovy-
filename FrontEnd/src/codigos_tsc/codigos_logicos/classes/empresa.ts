import  { Usuario } from "./usuario";
import type {Estado,Especialidade} from "../tiposFixos"

export class Empresa extends Usuario {
    cnpj:string
    pais:string

   
    constructor(
        nome: string,
        cnpj: string,
        pais:string,
        email: string,
        cep: string,
        estado: Estado,
        descricao:string,
        competencias: Especialidade[],
    
        
    )
    
    {
        super(nome,descricao, email, cep, estado, competencias);
        this.cnpj = cnpj;
        this.pais = pais


    }
}


