import sys, csv, json, os

def csv_to_json(csv_path, type, json_path):
  nodes = []
  links = []
  all_nodes = {}
  with open(csv_path, encoding='utf-8') as csvf:
    csv_reader = csv.DictReader(csvf)

    for r in csv_reader:
      if type == r['type']:
        source = r['name']
        target = r['depends_on']

        if source not in all_nodes:
          all_nodes[source] = 1
          nodes.append({
            'id': source,
            'ce': r['cbo'],
            'ca': r['ca'],
            'group': r['module'],
          })

        links.append({
          'source': source,
          'target': target,
          'value': 1,
        })

    # filtering the links, ensuring both source & target is included in nodes
    links = list(filter(lambda l: l['source'] in [n['id'] for n in nodes] and l['target'] in [n['id'] for n in nodes], links))

  with open(json_path, 'w', encoding='utf-8') as jsonf:
    jsonf.write('const data=')
    jsonf.write(json.dumps({
      'nodes': nodes,
      'links': links,
    }, indent=2))
    jsonf.write(';')


# main function
if __name__ == "__main__":
  # get csv & json file path from args
  args = sys.argv[1:]
  type = args[1]
  csv_to_json(args[0], type, args[2])
