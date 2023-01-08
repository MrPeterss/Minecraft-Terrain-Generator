import type { NextPage } from 'next'
import Head from 'next/head'
import Image from 'next/image'
import { useEffect, useState } from 'react'
import Map from '../components/Map'
import Preview from '../components/Preview'
import TestImg from '../components/TestImg'


const Home: NextPage = () => {

  
  const map_width = (typeof window !== 'undefined') ? window.innerWidth/2 : 0;
  const map_height = (typeof window !== 'undefined') ? window.innerHeight: 0;


  //return the map
  return (
    <div>
      <Head>
        <title>Create Next App</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <main>
        <div className='flex'>
          <div className="top-0 left-0">
            <Map height={map_height.toString()} width={map_width.toString()} />
          </div>
          <div className={'w-full top-0 left-'+map_width}>
            <Preview />
          </div>
        </div>
      </main>
      <script async defer src="https://maps.googleapis.com/maps/api/js?key=***REMOVED***"></script>
    </div>
  )
}

export default Home
