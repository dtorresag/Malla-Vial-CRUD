
export const fetchSegmentos = async () => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/segmentos`);
    if (!response.ok) {
      throw new Error('Error al cargar segmentos');
    }
    const data = await response.json();
  
    return data.map((segmento: any, index: number) => ({
      key: index.toString(),
      id: segmento.id,  
      largo: segmento.largo,
      direccion: segmento.direccion,
      tipoDeVia: segmento.tipoDeVia,
      numeroDeCalzadas: segmento.numeroDeCalzadas,
      numeroDeBordillos: segmento.numeroDeBordillos,
    }));
  };

  // Funcion para actualizar la informacion de un segmento
export const updateSegmento = async (id: number, updatedData: any) => {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/segmentos/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedData),
    });

    if (!response.ok) {
      throw new Error('Error al actualizar el segmento back');
    }

    return await response.json();
  } catch (error) {
    console.error('Error al actualizar el segmento:', error);
    throw error;
  }
};

// Funcion para obtener un segmento por el ID
export const getSegmento = async (id: number) => {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/segmentos/${id}`);
    if (!response.ok) {
      throw new Error('Error al obtener el segmento');
    }
    return await response.json();
  } catch (error) {
    console.error('Error al obtener el segmento:', error);
    throw error;
  }
};


  // Función para eliminar un segmento desde el backend
export const deleteSegmento = async (id: number) => {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/segmentos/${id}`, {
    method: 'DELETE',
  });
  return response;
};

// utils/segmentos.ts

export const createSegmento = async (segmento: any) => {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/segmentos`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(segmento),
  });

  if (!response.ok) {
    throw new Error('Error al crear el segmento: ' + response.statusText);
  }

  return response.json();
};

  