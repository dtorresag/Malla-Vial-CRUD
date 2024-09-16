//interface usada para listar segmentos
export interface Segmento {
    key: string; 
    id: number;
    largo: number; 
    direccion: string; 
    tipoDeVia: string; 
    numeroDeCalzadas: number; 
    numeroDeBordillos: number; 
  }

  //interfaz usada para edición y agregación
  export interface SegmentoDetalle {
    key?: string; 
    id?: number;

    largo?: number; 
    direccion?: string; 
    tipoDeVia?: string; 
    numeroDeCalzadas?: number; 
    numeroDeBordillos?: number; 
    materialCalzada?: string;
    materialBordillo?: string;
  }