# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.auth.models import User


class UserEvent(models.Model):
    type = models.CharField(max_length=64, blank=False)
    date = models.DateTimeField(auto_now=True, verbose_name='时间')
    event = models.CharField(max_length=255, blank=False)
    user = models.ForeignKey(User)

    class Meta:
        db_table = 'auth_user_event'
