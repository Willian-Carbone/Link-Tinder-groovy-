
export class Match{

    cpf:string
    cnpj:string
    empresa:string
    candidato:string
    email_empresa:string
    email_candidato:string




    constructor(cpf:string , candidato:string,email_candidato:string,cnpj:string,empresa:string,email_empresa:string){
        this.cnpj = cnpj
        this.cpf = cpf
        this.empresa = empresa
        this.candidato = candidato
        this.email_empresa = email_empresa
        this.email_candidato = email_candidato
    
    
    
    }
}