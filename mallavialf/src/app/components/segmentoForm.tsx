'use client';

import React from 'react';
import { Form, Input, InputNumber, Button } from 'antd';
import { SegmentoDetalle } from '@/app/interfaces/segmento'; 

interface SegmentoFormProps {
  initialValues?: SegmentoDetalle;
  onFinish: (values: any) => void;
}

const SegmentoForm: React.FC<SegmentoFormProps> = ({ initialValues, onFinish }) => {
  const [form] = Form.useForm();

  React.useEffect(() => {
    if (initialValues) {
      form.setFieldsValue(initialValues);
    }
  }, [initialValues, form]);

  return (
    <Form
      form={form}
      name="segmento-form"
      onFinish={onFinish}
      style={{ maxWidth: 600 }}
      initialValues={initialValues}
    >
      <Form.Item name="largo" label="Largo" rules={[{ required: true }]}>
        <InputNumber />
      </Form.Item>
      <Form.Item name="direccion" label="Dirección" rules={[{ required: true }]}>
        <Input />
      </Form.Item>
      <Form.Item name="tipoDeVia" label="Tipo de Vía" rules={[{ required: true }]}>
        <Input />
      </Form.Item>
      <Form.Item name="numeroDeCalzadas" label="Número de Calzadas" rules={[{ required: true }]}>
        <InputNumber />
      </Form.Item>
      <Form.Item name="numeroDeBordillos" label="Número de Bordillos" rules={[{ required: true }]}>
        <InputNumber />
      </Form.Item>
      <Form.Item name="materialCalzada" label="Material de Calzada" rules={[{ required: true }]}>
        <Input />
      </Form.Item>
      <Form.Item name="materialBordillo" label="Material de Bordillo" rules={[{ required: true }]}>
        <Input />
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
        <Button type="primary" htmlType="submit">
          Guardar
        </Button>
      </Form.Item>
    </Form>
  );
};

export default SegmentoForm;
