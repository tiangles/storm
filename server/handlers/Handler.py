import json


def handle_login(socket, message):
    user_name = message['user_name']
    passwd = message['password']

    if user_name == 'foo' and passwd == 'foo':
        result = {'cmd': 'login',
                  'result': 0,
                  'message': 'succeed',
                  }
    else:
        result = {'cmd': 'login',
                  'result': -1,
                  'message': 'incorrect user name or password',
                  }
    str = json.dumps(result)
    return str
