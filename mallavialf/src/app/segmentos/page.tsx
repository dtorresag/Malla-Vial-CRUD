
'use client';

import React from 'react';

import { useRouter } from 'next/navigation'; 
import { Table, Space, Button } from 'antd';
import { useSegmentos } from '../hooks/useSegmentos'; 
import { Segmento } from '../interfaces/segmento';
import type { ColumnsType } from 'antd/es/table';

const SegmentosTable: React.FC = () => {

  const { segmentos, handleDelete } = useSegmentos();
  const router = useRouter(); 

  const handleEdit = (id: number) => {
    router.push(`/segmentos/edit/${id}`); // Redirige a la página de edición con el ID del segmento
  };

  const handleAdd = () => {
    router.push('/segmentos/create'); // Redirige a la página de agregar un nuevo segmento
  };

  // Define las columnas para la tabla
  const columns: ColumnsType<Segmento> =  [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Largo',
      dataIndex: 'largo',
      key: 'largo',
    },
    {
      title: 'Dirección',
      dataIndex: 'direccion',
      key: 'direccion',
    },
    {
      title: 'Tipo de Vía',
      dataIndex: 'tipoDeVia',
      key: 'tipoDeVia',
    },
    {
      title: 'Número de Calzadas',
      dataIndex: 'numeroDeCalzadas',
      key: 'numeroDeCalzadas',
    },
    {
      title: 'Número de Bordillos',
      dataIndex: 'numeroDeBordillos',
      key: 'numeroDeBordillos',
    },
    {
      title: 'Acción',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <a onClick={() => handleEdit(record.id)}>Editar</a>
          <a onClick={() => handleDelete(record.id)}>Eliminar</a>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Table columns={columns} dataSource={segmentos} rowKey="id" />
      <Button 
        type="primary" 
        onClick={handleAdd} 
      >
        Agregar Nuevo Segmento
      </Button>
    </div>
  );
};

export default SegmentosTable;






