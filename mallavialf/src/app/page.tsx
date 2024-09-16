'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Redirige automáticamente a la página de segmentos
    router.push('/segmentos');
  }, [router]);

  return null;
}