export interface Avaliado {
  id?: string; // UUID opcional no envio, obrigatório no retorno
  saram: string;
  nguerra: string;
  secao: string;
  codposto: string;
  posto: string;
  quadro: string;
  funcao: string;
  antiguidade: number;
  ativo: boolean;
}
