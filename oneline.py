# converts text inf file (GraphQL query) to a oneline JSON object
#

import sys,json

infile = "oneline.txt"
txt = ""

if len(sys.argv) > 1:
    infile = sys.argv[1]

with open(infile, 'r') as file:
    txt = file.read()

ob = { "query": txt }

s = json.dumps(ob)
print(s)
