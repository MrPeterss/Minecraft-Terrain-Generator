// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from 'next'

let dataMap = new Map<string, Data>([
  [
    "test", 
    {
      bbox: {
        east: -73.877196,
        south: 42.844134,
        west: -73.903782,
        north: 42.854375
      },
      zoom: 12
    }
  ],
  [
    "test1", 
    {
      bbox: {
        east: -73.822632,
        south: 42.747012,
        west: -74.097290,
        north: 42.882002
      },
      zoom: 14
    }
  ]
]);

type Data = {
  bbox: Bbox;
  zoom: number;
}

type Bbox = {
  east: number;
  south: number;
  west: number;
  north: number;
}

export default function handler(
  req: NextApiRequest,
  res: NextApiResponse<Data|String>
) {
  const input = req.query;
  const values = Object.values(input);
  const key = values.join('');

  res.setHeader('Content-Type', 'application/json');

  if (key !== undefined && dataMap.get(key)!==undefined) {
    const data = dataMap.get(key);
    res.status(200);
    res.send(JSON.stringify(data, null, 2));
  } else {
    const err = { reason: "invalid key: " + key }
    res.status(400);
    res.send(JSON.stringify(err, null, 2));
  }
}
