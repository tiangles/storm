# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.http import HttpResponseRedirect
from django.shortcuts import render

import models
import db_importer
from staff.login_required import login_required


@login_required
def view_device_list(request):
    workshop_code = request.GET.get('workshop', None)
    if workshop_code is None:
        workshop_code = 'all'
    return render(request, 'view_device_list.html', {"workshop_code": workshop_code})


@login_required
def view_device(request):
    device_code = request.GET.get('code', None)
    device = models.Device.objects.get(code=device_code)
    if device is not None:
        return render(request, 'view_device.html', {"device_code": device.code,
                                                    "device_name": device.name,
                                                    "system": device.system,
                                                    "distribution_cabinet": device.distribution_cabinet,
                                                    "local_control_panel": device.local_control_panel,
                                                    "dcs_cabinet": device.dcs_cabinet,
                                                    "legend": device.legend,
                                                    "device_model": device.model,})
    else:
        return render(request, 'view_device.html', {"device_code": 'Not found device, code: %s'%device_code})


@login_required
def view_workshop_list(request):
    return render(request, 'view_workshop_list.html', {})


@login_required
def import_workshops(request):
    db_importer.import_workshop_data('/media/btian/workspace/Storm_Doc/V3.0/04-workshops.xlsx')
    return HttpResponseRedirect("/view/workshops/")


@login_required
def import_devices(request):
    db_importer.import_device_data('/media/btian/workspace/Storm_Doc/V3.0/01-devices-1.xlsx')
    return HttpResponseRedirect("/view/devices/")


@login_required
def import_device_link_info(request):
    db_importer.import_device_link_info_data('/media/btian/workspace/Storm_Doc/V3.0/01-devices-1.xlsx')
    return HttpResponseRedirect("/view/devices/")


@login_required
def import_signal(request):
    db_importer.import_signal_data('/media/btian/workspace/Storm_Doc/V3.0/02-signals.xlsx')
    return HttpResponseRedirect("/view/devices/")


@login_required
def import_dcs_connection(request):
    db_importer.import_dcs_connection_data('/media/btian/workspace/Storm_Doc/V3.0/03-connections-DCS.xlsx')
    return HttpResponseRedirect("/view/devices/")


@login_required
def import_local_control_connection(request):
    db_importer.import_local_control_connection_data('/media/btian/workspace/Storm_Doc/V3.0/03-connections-local.xlsx')
    return HttpResponseRedirect("/view/devices/")
