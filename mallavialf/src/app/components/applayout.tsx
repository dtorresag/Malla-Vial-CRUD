'use client';

import React, { ReactNode, useState } from 'react';
import { Button, Layout, Menu, theme } from 'antd';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  PlusSquareOutlined,
  HomeOutlined,
} from '@ant-design/icons';

const { Header, Sider, Content } = Layout;

interface AppLayoutProps {
  children: ReactNode;
}

const AppLayout: React.FC<AppLayoutProps> = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider trigger={null} collapsible collapsed={collapsed}>
        <div className="demo-logo-vertical" />
        <Menu
          theme="dark"
          mode="inline"
          defaultSelectedKeys={['1']}
          items={[
            { key: '1', icon:<a href='/segmentos'><HomeOutlined /></a> , label: 'Home' },
            { key: '2', icon: <a href='/segmentos/create'><PlusSquareOutlined /></a> , label: 'Add Segmento' },

          ]}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            padding: 0,
            background: colorBgContainer,
          }}
        >
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{
              fontSize: '16px',
              width: 64,
              height: 64,
            }}
          />
        </Header>
        <Content
          style={{
            margin: '24px 16px',
            padding: 24,
            minHeight: 280,
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
          }}
        >
          {children}
        </Content>
      </Layout>
    </Layout>
  );
};

export default AppLayout;

