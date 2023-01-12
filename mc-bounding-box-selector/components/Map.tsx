import { GoogleMap, useLoadScript, MarkerF, InfoWindow, useJsApiLoader, MapContext, useGoogleMap, Rectangle } from "@react-google-maps/api";
import { useCallback, useEffect, useState } from "react";

type MapProps = {
  width?: string;
  height?: string;
}

export default function Map(props:MapProps) {

  const { isLoaded } = useJsApiLoader({
    id: 'zippy-tiger-373100',
    googleMapsApiKey: "AIzaSyBAQtYeUM5iZ5cjm-dBJb_oktaZq4yRoM8"
  })

  const containerStyle = {
    width: props.width+"px",
    height: props.height+"px"
  };
  
  const center = {
    lat: 38.685,
    lng: -115.234
  }

  const rect_bounds = {
    north: 38.685,
    south: 33.671,
    east: -115.234,
    west: -118.251
  }

  const onLoad = useCallback(function callback(map:google.maps.Map) {
    map.setOptions({
      gestureHandling: 'greedy',
    });
  }, [])

  const onUnmount = useCallback(function callback(map:google.maps.Map) {
  }, [])
  

  return (isLoaded ? <div id="map" className="h-full w-fit">
    <GoogleMap
      mapContainerStyle={containerStyle}
      center={center}
      zoom={2.5}
      onLoad={onLoad}
      onUnmount={onUnmount}
    >
      <Rectangle
        bounds={rect_bounds}
        editable={true}
        draggable={true}
      />
    </GoogleMap>
  </div> : <></>);
}
