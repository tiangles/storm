# -*- coding:utf-8 -*-
import json
import time
from models.user import User
from models.event import UserEvent
from models.device import Device

from models.hashers import check_password


def login_required(func):
    def wrapper(socket, message):
        if hasattr(socket, 'user') and socket.user != None:
            return func(socket, message)
        else:
            return -1, 'login required'


def handle_login(socket, message):
    user_name = message['user_name']
    password = message['password']

    try:
        user = User.select().where(User.username == user_name).get()
    except:
        user = None

    if user is not None and check_password(password, user.password):
        socket.user = user
        return 0, 'succeed'
    else:
        return -1, 'incorrect user name or password'


def handle_upload_event(socket, message):
    try:
        event = UserEvent.create(user=socket.user,
                                 type=message['type'],
                                 event=message['event'],
                                 date=time.time())
        event.save()
        return 0, 'succeed'
    except Exception, e:
        return -1, e.__str__()


def handle_update_device(socket, message):
    try:
        j_device = message['device']
        device = Device.create(code=j_device['code'],
                               model=j_device['model'],
                               name=j_device['name'],
                               system=j_device['system'],
                               distribution_cabinet=j_device['distribution_cabinet'],
                               local_control_panel=j_device['local_control_panel'],
                               dcs_cabinet=j_device['dcs_cabinet'],
                               forward_device=j_device['forward_device'],
                               backward_device=j_device['backward_device'],
                               legend=j_device['legend'])
        device.save()
        return 0, 'succeed'
    except Exception, e:
        return -1, e.__str__()


def handle_sync_workshop_list(socket, message):
    workshop_list = [{"name": "车间1",
                      "code": "workshop_1",
                      "device_list": "HNA30AA001|HNA50AA001|HNC10AN001|HNC20AN001|HNC10AN001|HNC20AN001|HNC10AN001|HNC20AN001|HNC10AN001|HNC20AN001",},]
    return 0, workshop_list
