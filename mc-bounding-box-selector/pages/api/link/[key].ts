// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from 'next'

let dataMap = new Map<string, Data>([
  [
    "dFbSegXflExC", 
    {
      bbox: {
        east: -73.855917,
        south: 42.811320,
        west: -73.915990,
        north: 42.841914
      },
      zoom: 14
    }
  ],
  [
    "d", 
    {
      bbox: {
        east: -73.894901,
        south: 42.794771,
        west: -73.840828,
        north: 42.827261,
      },
      zoom: 10
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
