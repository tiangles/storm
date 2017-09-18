# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.auth.models import User
from django.utils.translation import ugettext_lazy as _
from django.contrib.sessions.base_session import (
    AbstractBaseSession, BaseSessionManager,
)

class UserEvent(models.Model):
    type = models.CharField(max_length=64, blank=False)
    date = models.DateTimeField(auto_now=True, verbose_name='时间')
    event = models.CharField(max_length=255, blank=False)
    user = models.ForeignKey(User)

    class Meta:
        db_table = 'user_event'


class SessionManager(BaseSessionManager):
    use_in_migrations = True


class UserEventSession(AbstractBaseSession):
    objects = SessionManager()
    created_date = models.DateTimeField(verbose_name=_('Created Date'), auto_now=True)
    user = models.ForeignKey(User)

    class Meta(AbstractBaseSession.Meta):
        db_table = 'user_event_session'


    @classmethod
    def get_session_store_class(cls):
        from django.contrib.sessions.backends.db import SessionStore
        return SessionStore
