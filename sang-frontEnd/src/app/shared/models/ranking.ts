export interface Ranking {
  avaliadoId: string;
  avaliacaoId: string | null; // Pode ser nulo se o militar ainda não foi avaliado no processo
  saram: string;
  posto: string;
  nguerra: string;
  secao: string;
  antiguidade: number;
  pontuacaoTotal: number;
  nivelado: boolean;
}
