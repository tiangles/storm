import json
import time
from models.user import User
from models.event import Event


def handle_login(socket, message):
    user_name = message['user_name']
    password = message['password']

    try:
        user = User.select().where(User.user_name == user_name).get()
    except:
        user = None

    if user is not None and user.password == password:
        socket.user = user
        return 0, 'succeed'
    else:
        return -1, 'incorrect user name or password'


def handle_upload_event(socket, message):
    try:
        event = Event.create(user=socket.user,
                             type=message['type'],
                             event=message['event'],
                             time=time.time())
        event.save()
        return 0, 'succeed'
    except Exception, e:
        return -1, e
