'use client';

import React, { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation';
import { Form, Input, InputNumber, Button, message } from 'antd';
import { getSegmento, updateSegmento } from '@/app/utils/segmentos'; // Importamos updateSegmento
import { SegmentoDetalle } from '@/app/interfaces/segmento'; 
import SegmentoForm from '@/app/components/segmentoForm';

const EditSegmento: React.FC = () => {
  const router = useRouter();
  const [form] = Form.useForm();
  const [segmento, setSegmento] = useState<SegmentoDetalle | null>(null);
  const { id } = useParams(); 

  console.log(id) 

  useEffect(() => {
    const fetchSegmento = async () => {
      try {
        const data = await getSegmento(Number(id)); 
        setSegmento(data);
        form.setFieldsValue(data);
      } catch (error) {
        message.error('Error al cargar el segmento');
      }
    };

    if (id) {
      fetchSegmento();
    }
  }, [id]);

  const onFinish = async (values: any) => {
    try {
      const response = await updateSegmento(Number(id), values);
      message.success('Segmento actualizado con éxito');
      router.push('/segmentos');
    } catch (error) {
      message.error('Error al actualizar el segmento fron');
    }
  };

  return (
    <SegmentoForm
      initialValues={segmento || {}}
      onFinish={onFinish}
    />
  )
};

export default EditSegmento;

