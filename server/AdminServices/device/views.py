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
    workshop_code = request.GET.get('workshop', None)
    if workshop_code is None:
        workshop_code = 'all'
    return render(request, 'view_device_list.html', {"workshop_code": workshop_code})


@login_required
def view_workshop_list(request):
    return render(request, 'view_workshop_list.html', {})
