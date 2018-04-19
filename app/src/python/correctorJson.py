from decimal import *
import json

SUP_IZQ = (Decimal('4.60324129935718'), Decimal('-74.06490464017361'))
SUP_DER = (Decimal('4.602864946901448'), Decimal('-74.06431626817653'))
INF_IZQ = (Decimal('4.602660916841884'), Decimal('-74.06529444934267'))

HEIGHT = 3223
WIDTH = 3168

INPUT = [
  (498, 1569), (742, 1569), (761, 1059), (1257, 2365), (2268, 1569), (758, 2355), (2017, 1580),
  (1518, 1564), (1258, 1381), (1009, 1574), (760, 1177), (1260, 1554), (256, 2160), (251, 1941),
  (253, 1171), (1766, 1572), (1002, 1175), (500, 2364), (1012, 2359), (1766, 2162), (2582, 1938),
  (761, 975), (253, 1380), (2019, 2165), (253, 2361), (1264, 2161), (1258, 1178), (1764, 2361),
  (2518, 2357), (2268, 2161), (755, 2155), (1497, 2165), (506, 2159), (256, 971), (2014, 2356),
  (2269, 1939), (501, 1166), (507, 971), (2265, 2354), (261, 1564), (1007, 2161), (2262, 1174),
  (2573, 1166), (2010, 1171), (1765, 1178), (1513, 1184), (764, 1366), (1013, 961), (1264, 971),
  (1517, 969), (1768, 968), (2013, 972), (2268, 973), (2523, 970), (753, 1941), (1262, 1941), (1765, 1941),
  (1769, 1378), (2265, 1400)
]

def vectorDiference(a, b):
  difference = [0,0]
  difference[0] = a[0] - b[0]
  difference[1] = a[1] - b[1]
  return difference

def vectorSum(a, b):
  _sum = [0,0]
  _sum[0] = a[0] + b[0]
  _sum[1] = a[0] + b[0]
  return _sum

αβ = vectorDiference(SUP_DER, SUP_IZQ)
θσ = vectorDiference(INF_IZQ, SUP_IZQ)

def transform(x, y):
  latLong = [0,0]
  latLong[0] = (αβ[0]*x/WIDTH + θσ[0]*y/HEIGHT) + SUP_IZQ[0]
  latLong[1] = (αβ[1]*x/WIDTH + θσ[1]*y/HEIGHT) + SUP_IZQ[1]

  return tuple(latLong)

def tranformInput():
  correctInput = []
  fid = 940
  for _input in INPUT:
    obj = {}
    latLong = transform(_input[0], _input[1])
    obj['lat'] = str(latLong[0])
    obj['long'] = str(latLong[1])
    obj['x'] = str(_input[0])
    obj['y'] = str(_input[1])
    obj['floor'] = 7
    obj['area'] = -1
    obj['bloque'] = 'ML'
    obj['FID'] = fid
    fid = fid + 1
    correctInput.append(obj)

  with open('nodos corregidos.json', mode='w') as file:
    file.write(json.dumps(correctInput))

tranformInput()
