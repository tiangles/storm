# -*- coding:utf-8 -*-
import json
import time
from models.user import User
from models.event import UserEvent
from models.device import Device
from models.workshop import Workshop


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
                               legend=j_device['legend'],
                               workshop = Workshop.select().where(Device.code == j_device['workshop']))
        device.save()
        return 0, 'succeed'
    except Exception, e:
        return -1, e.__str__()


def handle_sync_workshop_list(socket, message):
    try:
        workshops = Workshop.select()
        workshop_list = []
        for workshop in workshops:
            devices = Device.select().where(Device.workshop == workshop)
            device_list = ''
            for device in devices:
                device_list = '%s|%s'%(device_list, device.code)
            workshop_list.append({'name': workshop.name,
                                  'code': workshop.code,
                                  'device_list': device_list})

        return 0, workshop_list
    except Exception, e:
        return -1, e.__str__()


def handle_sync_workshop(socket, message):
    try:
        workshop_code = message['workshop_code']
        workshop_query = Workshop.select().where(Workshop.code == workshop_code)
        workshop = workshop_query[0]
        devices = Device.select().where(Device.workshop == workshop)
        device_list = ''
        for device in devices:
            device_list = '%s|%s'%(device_list, device.code)

        j_workshop = {'name': workshop.name,
                      'code': workshop.code,
                      'device_list': device_list}
        return 0, j_workshop
    except Exception, e:
        return -1, e.__str__()


def handle_sync_device(socket, message):
    try:
        device_code = message['device_code']
        device_query = Device.select().where(Device.code == device_code)
        device = device_query[0]
        j_device = {'code': device.code,
                    'name': device.name,
                    'model': device.model,
                    'system': device.system,
                    'distribution_cabinet': device.distribution_cabinet,
                    'local_control_panel': device.local_control_panel,
                    'dcs_cabinet': device.dcs_cabinet,
                    'forward_device': device.forward_device,
                    'backward_device': device.backward_device,
                    'legend': device.legend,
                    'workshop': device.workshop.code, }
        return 0, j_device
    except Exception, e:
        return -1, e.__str__()