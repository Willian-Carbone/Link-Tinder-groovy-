
import { FORMACAO,ESPECIALIDADES } from "../codigos_logicos/tiposFixos";



export function checagem_cep(cep: string) : boolean {

    return /^\d{5}-\d{3}$/.test(cep) || /^\d{8}$/.test(cep)

}

export function checagem_cnpj(cnpj: string) :boolean {

    return /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/.test(cnpj) || /^\d{14}$/.test(cnpj)

}


export function checagem_cpf(cpf: string) : boolean {
    return /^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(cpf) || /^\d{11}$/.test(cpf)
}

export function checagem_email(email: string) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}



export function padronizar_entrada(entrada:string) :string{

    if (entrada == null || entrada == ""){
            return "Dado não informado"
        }


    return entrada.replaceAll(/\D/g, "")

}


export function criptografar(entrada:string) : string{
    return btoa(entrada);
}

export function descriptografar(entrada:string) : string{
    return atob(entrada);
}





export function nomeFormacao(codigo: string){

    const form = FORMACAO.find(f => f.codigo === codigo);

    return form ? form.nome : codigo;

}

export function validador_nome(nome: string) : boolean{
    return /^[A-Zà-ü]{2,}( [A-Zà-ü]{2,})+$/i.test(nome)

}





export function nomeEspecialidade(codigo: string){

    const esp = ESPECIALIDADES.find(e => e.codigo === codigo);

    return esp ? esp.nome : codigo;

}
