export interface UserRequest  {
  username: string;
  password: string;
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

export interface UserDTO {
  id: string;
  roleId: string;
  username: string;
}
