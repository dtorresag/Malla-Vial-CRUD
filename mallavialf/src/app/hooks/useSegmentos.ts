'use client';

// useSegmentos.ts
import { useState, useEffect } from 'react';
import { message } from 'antd';
import { fetchSegmentos, deleteSegmento } from '../utils/segmentos';
import { Segmento } from '../interfaces/segmento';

export const useSegmentos = () => {
  const [segmentos, setSegmentos] = useState<Segmento[]>([]);

  const fetchData = async () => {
    const data = await fetchSegmentos();
    setSegmentos(data);
  };

  const handleDelete = async (id: number) => {
    try {
      const success = await deleteSegmento(id);
      if (success) {
        message.success(`Segmento con ID: ${id} eliminado exitosamente`);
        await fetchData(); // Actualiza la lista después de eliminar
      } else {
        message.error('Error al eliminar el segmento');
      }
    } catch (error) {
      message.error('Error de red al eliminar el segmento');
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return { segmentos, handleDelete };
};


