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
      await createSegmento(values);
      message.success('Segmento creado con éxito');
      router.push('/segmentos');
    } catch (error) {
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
