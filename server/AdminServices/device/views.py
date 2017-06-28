# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
import rest_framework.viewsets
import serializers
import models


def view_devices(request):
    return render(request, 'view.html', {})