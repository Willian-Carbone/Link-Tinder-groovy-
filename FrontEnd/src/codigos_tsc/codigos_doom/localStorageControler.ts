import { Candidato } from "../codigos_logicos/classes/candidato";
import { Empresa } from "../codigos_logicos/classes/empresa";
import { Vaga } from "../codigos_logicos/classes/vaga";
import { ESTADOS, Formacao} from "../codigos_logicos/tiposFixos";
import { capturarEspecialidades } from "./utilidades_domm";
import { Match} from "../codigos_logicos/classes/Match";
import { criptografar } from "../codigos_logicos/utilitarios";



export function checagem_cadastro(entrada: string): boolean {
    if (entrada.length === 11) {
        const dados = localStorage.getItem("Candidatos");
        if (!dados) return false;

        const candidatos: Candidato[] = JSON.parse(dados);
        return candidatos.some(c => c.cpf === entrada);
    }
    
    else {
        const dados = localStorage.getItem("Empresas");
        if (!dados) return false;

        const empresas: Empresa[] = JSON.parse(dados);
        return empresas.some(e => e.cnpj === entrada);
    }
}



export function cadastrar_empresa(
    dados: Record<string, string>,
    containerEspecialidades: HTMLElement
) {

    if (!localStorage.getItem("Empresas")) {
        localStorage.setItem("Empresas", JSON.stringify([]));
    }

    if (!localStorage.getItem("empresa_tecnologia")) {
        localStorage.setItem("empresa_tecnologia", JSON.stringify({}));
    }

    if (!localStorage.getItem("total_empresas")) {
        localStorage.setItem("total_empresas", "0");
    }

    if (!localStorage.getItem("total_cadastros")) {
        localStorage.setItem("total_cadastros", "0");
    }

    const especialidadesSelecionadas = capturarEspecialidades(containerEspecialidades);

    const estadoSelecionado = ESTADOS.find(e => e.sigla === dados.estado);

    if (!estadoSelecionado) {
        throw new Error("Estado inválido");
    }

    const empresa = new Empresa(
        dados.nome_empresa || "",
        dados.cnpj_empresa || "",
        dados.pais || "",
        dados.email_empresa || "",
        dados.cep_empresa || "",
        estadoSelecionado.sigla,
        dados.descricao || "",
        especialidadesSelecionadas
    );

    const empresas = JSON.parse(localStorage.getItem("Empresas")!);

    const tecnologias = JSON.parse(localStorage.getItem("empresa_tecnologia")!);

    let totalEmpresas = Number(localStorage.getItem("total_empresas"));

    let totalCadastros = Number(localStorage.getItem("total_cadastros"));

    empresas.push(empresa);

    totalEmpresas += 1;
    totalCadastros += 1;

    especialidadesSelecionadas.forEach(codigo => {

        if (!tecnologias[codigo]) {
            tecnologias[codigo] = 0;
        }

        tecnologias[codigo] += 1;

    });

    localStorage.setItem("Empresas", JSON.stringify(empresas));
    localStorage.setItem("empresa_tecnologia", JSON.stringify(tecnologias));
    localStorage.setItem("total_empresas", totalEmpresas.toString());
    localStorage.setItem("total_cadastros", totalCadastros.toString());
}




export function cadastrar_candidato(
    dados: Record<string, string>,
    containerEspecialidades: HTMLElement
) {

    if (!localStorage.getItem("Candidatos")) {
        localStorage.setItem("Candidatos", JSON.stringify([]));
    }

    if (!localStorage.getItem("candidatos_tecnologias")) {
        localStorage.setItem("candidatos_tecnologias", JSON.stringify({}));
    }

    if (!localStorage.getItem("cadastros_candidato")) {
        localStorage.setItem("cadastros_candidato", "0");
    }

    if (!localStorage.getItem("total_cadastros")) {
        localStorage.setItem("total_cadastros", "0");
    }

    const especialidadesSelecionadas = capturarEspecialidades(containerEspecialidades);

    const estadoSelecionado = ESTADOS.find(e => e.sigla === dados.estado);

    if (!estadoSelecionado) {
        throw new Error("Estado inválido");
    }

    const candidato = new Candidato(
        dados.nome_candidato || "",
        dados.cpf_candidato || "",
        Number(dados.idade) || 0,
        dados.email_candidato || "",
        dados.cep_candidato || "",
        estadoSelecionado.sigla,
        dados.descricao || "",
        especialidadesSelecionadas,
        dados.formacao as Formacao
    );

    const candidatos = JSON.parse(localStorage.getItem("Candidatos")!);

    const tecnologias = JSON.parse(localStorage.getItem("candidatos_tecnologias")!);

    let totalCandidatos = Number(localStorage.getItem("cadastros_candidato"));

    let totalCadastros = Number(localStorage.getItem("total_cadastros"));

    candidatos.push(candidato);

    totalCandidatos += 1;
    totalCadastros += 1;

    especialidadesSelecionadas.forEach(codigo => {

        if (!tecnologias[codigo]) {
            tecnologias[codigo] = 0;
        }

        tecnologias[codigo] += 1;

    });

    localStorage.setItem("Candidatos", JSON.stringify(candidatos));
    localStorage.setItem("candidatos_tecnologias", JSON.stringify(tecnologias));
    localStorage.setItem("cadastros_candidato", totalCandidatos.toString());
    localStorage.setItem("total_cadastros", totalCadastros.toString());

}


export function cadastrar_vaga(vaga: Vaga):void {
    if (!localStorage.getItem("Vagas")) {
        localStorage.setItem("Vagas", JSON.stringify([]));
    }

    const vagas = JSON.parse(localStorage.getItem("Vagas")!);
    vagas.push(vaga);
    localStorage.setItem("Vagas", JSON.stringify(vagas));
}


export function edicao_candidato(dados: Record<string, string>,containerEspecialidades: HTMLElement, identificador:string){

    const candidatos = capturar_candidatos()

    const tecnologias = JSON.parse(localStorage.getItem("candidatos_tecnologias") || "{}");

    const especialidadesSelecionadas = capturarEspecialidades(containerEspecialidades);

    const estadoSelecionado = ESTADOS.find(e => e.sigla === dados.estado);

    if (!estadoSelecionado) {
        throw new Error("Estado inválido");
    }

    
    const posicao = candidatos.findIndex(
        (c: any) => c.cpf=== identificador
    );

    if (posicao === -1) return;

    const candidatoAntigo = candidatos[posicao];

    const especialidadesAntigas: string[] = candidatoAntigo?.competencias || [];

    
    const removidas = especialidadesAntigas.filter(
        esp => !especialidadesSelecionadas.includes(esp as any)
    );

   
    const adicionadas = especialidadesSelecionadas.filter(
        esp => !especialidadesAntigas.includes(esp)
    );


    removidas.forEach(codigo => {

        if (tecnologias[codigo]) {
            tecnologias[codigo] = Math.max(0, tecnologias[codigo] - 1);
        }

    });


    adicionadas.forEach(codigo => {

        if (!tecnologias[codigo]) {
            tecnologias[codigo] = 0;
        }

        tecnologias[codigo] += 1;

    });



   

    candidatos[posicao] = new Candidato(
        dados.nome_candidato || "",
        identificador ,
        Number(dados.idade) || 0,
        dados.email_candidato || "",
        dados.cep_candidato || "",
        estadoSelecionado.sigla,
        dados.descricao || "",
        especialidadesSelecionadas,
        dados.formacao as Formacao
    );



   

    localStorage.setItem("Candidatos", JSON.stringify(candidatos));
    localStorage.setItem("candidatos_tecnologias", JSON.stringify(tecnologias));

   


}




export function edicao_empresa(dados: Record<string, string>,containerEspecialidades: HTMLElement, identificador:string){

    const empresas = capturar_empresas()

    const tecnologias = JSON.parse(localStorage.getItem("empresa_tecnologia") || "{}");

    const especialidadesSelecionadas = capturarEspecialidades(containerEspecialidades);

    const estadoSelecionado = ESTADOS.find(e => e.sigla === dados.estado);

    if (!estadoSelecionado) {
        throw new Error("Estado inválido");
    }

    
    const posicao =empresas.findIndex(
        (c: any) => c.cnpj=== identificador
    );

    if (posicao === -1) return;

    const candidatoAntigo = empresas[posicao];

    const especialidadesAntigas: string[] = candidatoAntigo?.competencias || [];

    
    const removidas = especialidadesAntigas.filter(
        esp => !especialidadesSelecionadas.includes(esp as any)
    );

   
    const adicionadas = especialidadesSelecionadas.filter(
        esp => !especialidadesAntigas.includes(esp)
    );


    removidas.forEach(codigo => {

        if (tecnologias[codigo]) {
            tecnologias[codigo] = Math.max(0, tecnologias[codigo] - 1);
        }

    });


    adicionadas.forEach(codigo => {

        if (!tecnologias[codigo]) {
            tecnologias[codigo] = 0;
        }

        tecnologias[codigo] += 1;

    });



   

    empresas[posicao] = new Empresa(
        dados.nome_empresa || "",
        identificador ,
        dados.pais || "",
        dados.email_empresa || "",
        dados.cep_empresa || "",
        estadoSelecionado.sigla,
        dados.descricao || "",
        especialidadesSelecionadas,
    
    );



   

    localStorage.setItem("Empresas", JSON.stringify(empresas));
    localStorage.setItem("empresa_tecnologia", JSON.stringify(tecnologias));

   


}













export function setar_usuario(entrada: string) :void{
    localStorage.setItem("usuario_logado", entrada);

}


export function capturar_usuario_logado() : string {

    const usuario_logado  = localStorage.getItem("usuario_logado") || "";

    return usuario_logado;

}







export function capturar_dados_usuario(entrada :string) : Candidato | Empresa | undefined{

    if (entrada.length === 11){
        const dados = localStorage.getItem("Candidatos");
        if (!dados) return;

        const candidatos: Candidato[] = JSON.parse(dados);
        const candidato = candidatos.find(c => c.cpf === entrada);
        return candidato;
    }

    else{
        const dados = localStorage.getItem("Empresas");
        if (!dados) return;

        const empresas: Empresa[] = JSON.parse(dados);
        const empresa = empresas.find(e => e.cnpj === entrada);
        return empresa;
    }



}


export function capturar_candidatos() : Candidato[]{
    const dados = localStorage.getItem("Candidatos");
    if (!dados) return [];

    const candidatos: Candidato[] = JSON.parse(dados);
    return candidatos;

}


export function capturar_empresas() : Empresa[]{
    const dados = localStorage.getItem("Empresas");
    if(!dados) return [];

    const empresas: Empresa[] = JSON.parse(dados);
    return empresas;

}


export  function capturar_vagas() : Vaga[]{
    const dados = localStorage.getItem("Vagas");
    if(!dados) return [];

    const vagas: Vaga[] = JSON.parse(dados);
    return vagas;




}






export function salvar_match(cpf:string,candidato:string,email_candidato:string,cnpj:string,empresa:string,email_empresa:string,) :void{
    if (!localStorage.getItem("Matchs")) {
        localStorage.setItem("Matchs", JSON.stringify([]));
    }

    const matches = JSON.parse(localStorage.getItem("Matchs")!);

    let novoMatch = new Match(
        cpf,
        candidato,
        email_candidato || "",
        cnpj,
        empresa,
        email_empresa || ""
    );


    matches.push(novoMatch);

    localStorage.setItem("Matchs", JSON.stringify(matches));
}


export function checar_matches_existentes(cpf:string, cnpj:string):boolean{

    if(!localStorage.getItem("Matchs")){
        return false;
    }

    const lista_matchs: Match[] = JSON.parse(localStorage.getItem("Matchs")!);

    return lista_matchs.some(match =>
        match.cpf === cpf &&
        match.cnpj === cnpj
    );
}

export function capturar_matchs() : Match[]{
    const dados = localStorage.getItem("Matchs");
    if(!dados) return [];

    const matches: Match[] = JSON.parse(dados);
    return matches;

}

export function retornar_quantidade_candidatos(): number {
    return Number (localStorage.getItem("cadastros_candidato"));
}


export function retornar_quantidade_empresas(): number {
    return Number (localStorage.getItem("total_empresas"));
}


export function remover_empresa(identificador: string){

    const empresas = capturar_empresas();

    const tecnologias = JSON.parse(localStorage.getItem("empresa_tecnologia") || "{}");

    const total_empresas = Number(localStorage.getItem("total_empresas") || 0);

    const total_cadastros = Number(localStorage.getItem("total_cadastros") || 0);


    const matchs = JSON.parse(localStorage.getItem("Matchs") || "[]");

    const vagas = JSON.parse(localStorage.getItem("Vagas") || "[]");


    const posicao = empresas.findIndex(
        (e: any) => e.cnpj === identificador
    );

    if (posicao === -1) return;


    const empresa = empresas[posicao];

    const especialidades: string[] = empresa?.competencias || [];


    
    especialidades.forEach(codigo => {

        if (tecnologias[codigo]) {
            tecnologias[codigo] = Math.max(0, tecnologias[codigo] - 1);
        }

    });


  
    empresas.splice(posicao, 1);


    
    const novosMatchs = matchs.filter(
        (m: any) => m.cnpj !== identificador
    );


  
    const novasVagas = vagas.filter(
        (v: any) => v.contratante !== identificador
    );


    const novo_total = Math.max(0, total_empresas - 1);
    const novo_total_cadastros = Math.max(0, total_cadastros - 1);


    localStorage.setItem("Empresas", JSON.stringify(empresas));
    localStorage.setItem("empresa_tecnologia", JSON.stringify(tecnologias));
    localStorage.setItem("total_empresas", String(novo_total));
    localStorage.setItem("Matchs", JSON.stringify(novosMatchs));
    localStorage.setItem("Vagas", JSON.stringify(novasVagas));
    localStorage.setItem("total_cadastros", String(novo_total_cadastros));

}



export function remover_candidato(identificador: string){

    const candidatos = capturar_candidatos();

    const tecnologias = JSON.parse(localStorage.getItem("candidatos_tecnologias") || "{}");

    const total_candidatos = Number(localStorage.getItem("cadastros_candidato") || 0);

    const total_cadastros = Number(localStorage.getItem("total_cadastros") || 0);


    const matchs = JSON.parse(localStorage.getItem("Matchs") || "[]");

    const vagas = JSON.parse(localStorage.getItem("Vagas") || "[]");


    const posicao = candidatos.findIndex(
        (e: any) => e.cpf === identificador
    );

    if (posicao === -1) return;


    const candidato = candidatos[posicao];

    const especialidades: string[] = candidato?.competencias || [];


    
    especialidades.forEach(codigo => {

        if (tecnologias[codigo]) {
            tecnologias[codigo] = Math.max(0, tecnologias[codigo] - 1);
        }

    });


    const hash = criptografar(identificador);

    vagas.forEach((vaga: any) => {

        if (vaga.curtidas && vaga.curtidas.includes(hash)){

            vaga.curtidas = vaga.curtidas.filter(
                (id: string) => id !== hash
            );

        }

    });


  
    candidatos.splice(posicao, 1);


    
    const novosMatchs = matchs.filter(
        (m: any) => m.cpf !== identificador
    );


  
   

    const novo_total = Math.max(0, total_candidatos - 1);
    const novo_total_cadastros = Math.max(0, total_cadastros - 1);


    localStorage.setItem("Candidatos", JSON.stringify(candidatos));
    localStorage.setItem("candidatos_tecnologias", JSON.stringify(tecnologias));
    localStorage.setItem("cadastros_candidato", String(novo_total));
    localStorage.setItem("Matchs", JSON.stringify(novosMatchs));;
    localStorage.setItem("total_cadastros", String(novo_total_cadastros));
    localStorage.setItem("Vagas", JSON.stringify(vagas));

}


export function calcular_afinidade_vaga(vaga: any, candidato_alvo:Candidato): number {

    

    if(!candidato_alvo) return 0;

    const competencias: string[] = candidato_alvo.competencias || [];

    const requisitos: string[] = vaga.requisitos || [];

    if (requisitos.length === 0) return 0;

    const coincidencias = requisitos.filter(req =>
        competencias.includes(req)
    );

    const afinidade = (coincidencias.length / requisitos.length) * 100;

    return Math.round(afinidade);
}



export function calcular_afinidade_candidato_empresa(candidato: Candidato, empresa:Empresa): number {

   

    const competencias_candidato: string[] = candidato.competencias || [];

    const competencias_empresa: string[] = empresa.competencias || [];

   

    const coincidencias = competencias_candidato.filter(CC =>
        competencias_empresa.includes(CC)
    );

    const afinidade = (coincidencias.length / competencias_empresa.length) * 100;

    return Math.round(afinidade);
}
