// src/app/segmentos/create/CreateSegmento.tsx
'use client';

import React from 'react';
import { useRouter } from 'next/navigation';
import { message } from 'antd';
import { createSegmento } from '@/app/utils/segmentos'; 
import SegmentoForm from '@/app/components/segmentoForm';

const CreateSegmento: React.FC = () => {
  const router = useRouter();

  const onFinish = async (values: any) => {
    try {
      console.log('Enviando datos al backend:', values); 
      await createSegmento(values); // Cambia esto a la función para crear un segmento
      message.success('Segmento creado con éxito');
      router.push('/segmentos');
    } catch (error) {
      console.error('Error en la creación:', error); 
      message.error('Error al crear el segmento');
    }
  };

  return (
    <SegmentoForm
      onFinish={onFinish}
    />
  );
};

export default CreateSegmento;
