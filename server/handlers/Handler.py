import json
from models.user import User

def handle_login(socket, message):
    user_name = message['user_name']
    passwd = message['password']

    user = User.select().where(User.user_name == user_name).get()

    if user is not None and user.password == passwd:
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
