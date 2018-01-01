# -*- coding:utf-8 -*-
import random
import time
from models.user import User
from models.event import UserEvent
from models.device import Device, DatabaseMeta
from models.workshop import Workshop
from datetime import *
import socket
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
        return 0, {'user_id': user.id}
    else:
        return -1, {'error':'incorrect user name or password'}


def handle_upload_event(socket, message):
    event = UserEvent.create(user=socket.user,
                             type=message['type'],
                             event=message['event'],
                             date=time.time())
    event.save()
    return 0, 'succeed'


def handle_update_device(socket, message):
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
                           workshop=Workshop.select().where(Device.code == j_device['workshop']))
    device.save()
    return 0, 'succeed'


def handle_sync_workshop_list(socket, message):
    workshops = Workshop.select()
    workshop_list = []
    for workshop in workshops:
        devices = Device.select().where(Device.workshop == workshop)
        device_list = ''
        for device in devices:
            device_list = '%s|%s' % (device_list, device.code)
        workshop_list.append({'name': workshop.name,
                              'code': workshop.code,
                              'device_list': device_list})

    return 0, workshop_list


def handle_sync_workshop(socket, message):
    workshop_code = message['workshop_code']
    workshop_query = Workshop.select().where(Workshop.code == workshop_code)
    workshop = workshop_query[0]
    devices = Device.select().where(Device.workshop == workshop)
    device_list = ''
    for device in devices:
        device_list = '%s|%s' % (device_list, device.code)

    j_workshop = {'name': workshop.name,
                  'code': workshop.code,
                  'device_list': device_list}
    return 0, j_workshop


def handle_sync_device(socket, message):
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


def handle_get_signal_parameter_record(socket, message):
    values = {}
    for connection in message['connections']:
        values[connection]=get_signal_parameter_record(connection)
    j_record = {
        'device_code': message['device_code'],
        'values': values,
        'time': datetime.now().strftime('%Y-%m-%d %H:%M:%S %f')}
    return 0, j_record


def get_host_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(('8.8.8.8', 80))
        ip = s.getsockname()[0]
    finally:
        s.close()
    return ip


def handle_sync_database(skt, message):
    metas = DatabaseMeta.select()
    assert len(metas)>0
    return 0, {
        'url': 'http://%s:8128/static/storm_device.sqlite3'%(get_host_ip(), ),
        'db_version': str(metas[0].version)
    }


def get_signal_parameter_record(connection):
    return -12 + (0.5 - random.random()) * 2


def handle_upload_user_events(socket, message):
    events = message['events']
    result=[]
    for e in events:
        event_id = e['event_id']
        date = e['date']
        event = e['event']
        device_code = e['device_code']
        user_id = e['user_id']
        try:
            UserEvent.create(id=event_id,
                             date=date,
                             event=event,
                             device_code=device_code,
                             event_id=event_id,
                             user_id=user_id,
                             status=1)
            result.append({'event_id':event_id,
                           'event_status':1})
        except Exception as e:
            result = str(e)

    return 0, result

