# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
import django.contrib.auth
from django.http import HttpResponseRedirect


def login(request):
    if request.method == 'POST':
        print("POST")
        username = request.POST.get('username', '')
        password = request.POST.get('password', '')
        user = django.contrib.auth.authenticate(username=username, password=password)
        if user is not None and user.is_active and user.is_authenticated():
            django.contrib.auth.login(request, user)
            return HttpResponseRedirect("/devices")

    return render(request, 'login.html', {})


def logout(request):
    django.contrib.auth.logout(request)
    return render(request, 'login.html', {})
