import json
import boto3
import subprocess as os
import re

def lambda_handler(event, context):
    bucket_name = event['bucketName']
    file_path = event['filePath']

    file_name = ""
    paths = file_path.split('/')
    if (len(paths) > 1):
        file_name = paths[1]
    else:
        file_name = paths[0]

    s3 = boto3.client('s3')
    s3.download_file(bucket_name, file_path, '/tmp/{}'.format(file_name))

    sh = os.run('java -cp /tmp {}'.format(file_name.split('.')[0]), shell=True, stdout=os.PIPE, stderr=os.PIPE)
    stdout = sh.stdout.decode('UTF-8')
    stderr = sh.stderr.decode('UTF-8')

    if len(stderr) > 0:
        return {
            'statusCode' : 500,
            'body' : None,
            'error' : json.dumps(stderr)
        }

    arr = []
    find_result = re.findall(r'({"questionId":[\w]+,[\s]+"caseId":[\w]+,[\s]+"result":[\w\\"]+})', stdout)
    for i in find_result:
        j = json.loads(i)
        arr.append(j)

    return {
        'statusCode' : 200,
        'body' : arr,
        'error' : None
    }
