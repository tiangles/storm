# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
import rest_framework.viewsets
import serializers
import models
from staff.login_required import login_required


@login_required
def view_device_list(request, workshop=None):
    # workshop_code = request.GET.get('workshop', None)
    workshop_code = workshop
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
                                                    "forward_device": device.forward_device,
                                                    "backward_device": device.backward_device,
                                                    "legend": device.legend,
                                                    "device_model": device.model,})
    else:
        return render(request, 'view_device.html', {"device_code": 'Not found device, code: %s'%device_code})


@login_required
def view_workshop_list(request):
    return render(request, 'view_workshop_list.html', {})
