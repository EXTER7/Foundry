#!/usr/bin/python3

# Generate Block bounds from JSON models.

import sys
import json

name = sys.argv[1]
jsonfile = sys.argv[2]

json_data=open(jsonfile).read()
data = json.loads(json_data)

print("  static private final AxisAlignedBB[] %s = new AxisAlignedBB[]" % (name))
print("  {")
for element in data["elements"]:
  x1 = float(element["from"][0]) / 16.0
  y1 = float(element["from"][1]) / 16.0
  z1 = float(element["from"][2]) / 16.0

  x2 = float(element["to"][0]) / 16.0
  y2 = float(element["to"][1]) / 16.0
  z2 = float(element["to"][2]) / 16.0

  print("    new AxisAlignedBB(%g,%g,%g,%g,%g,%g)," % (x1,y1,z1,x2,y2,z2))
print("  };")

