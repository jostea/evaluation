import json
import boto3
import subprocess as os
import re

def extract_file_name(path):
    paths = path.split('/')
    if (len(paths) > 1):
        return paths[1]
    else:
        return paths[0]

def lambda_handler(event, context):
    bucket_name = event['bucketName']
    main_file_path = event['mainFilePath']
    test_case_file_path = event['testCaseFilePath']

    main_file_name = extract_file_name(main_file_path)

    test_case_file_name = extract_file_name(test_case_file_path)

    s3 = boto3.client('s3')
    s3.download_file(bucket_name, main_file_path, '/tmp/{}'.format(main_file_name))
    s3.download_file(bucket_name, test_case_file_path, '/tmp/{}'.format(test_case_file_name))

    sh = os.run('java -cp /tmp {}'.format(main_file_name.split('.')[0]), shell=True, stdout=os.PIPE, stderr=os.PIPE)
    stdout = sh.stdout.decode('UTF-8')
    stderr = sh.stderr.decode('UTF-8')

    print(stdout)
    if len(stderr) > 0:
        return {
            'statusCode' : 500,
            'body' : None,
            'error' : json.dumps(stderr)
        }

    arr = []
    find_result = re.findall(r'({"questionId":[\s][\w]+,[\s]+"caseId":[\s][\w]+,[\s]+"result":[\s][\w\\"]+})', stdout)
    for i in find_result:
        j = json.loads(i)
        arr.append(j)

    return {
        'statusCode' : 200,
        'body' : arr,
        'error' : None
    }
