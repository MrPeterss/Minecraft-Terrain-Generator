import type { NextPage } from 'next'
import Head from 'next/head'
import Image from 'next/image'
import { useEffect, useState } from 'react'
import Map from '../components/Map'

const Home: NextPage = () => {

  
  const width = (typeof window !== 'undefined') ? window.innerWidth : 0;
  const height = (typeof window !== 'undefined') ? window.innerHeight: 0;

  //return the map
  return (
    <div className="flex min-h-screen flex-col items-center justify-center py-2">
      <Head>
        <title>Create Next App</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <main className="fixed top[-100px]">
        <Map height={height.toString()} width={width.toString()} />
      </main>
      <footer className="flex h-24 w-full items-center justify-center border-t">
        <a
          className="flex items-center justify-center gap-2"
          href="https://vercel.com?utm_source=create-next-app&utm_medium=default-template&utm_campaign=create-next-app"
          target="_blank"
          rel="noopener noreferrer"
        >
          Powered by{' '}
          <Image src="/vercel.svg" alt="Vercel Logo" width={72} height={16} />
        </a>
      </footer>
      <script async defer src="https://maps.googleapis.com/maps/api/js?key=***REMOVED***"></script>
    </div>
  )
}

export default Home
