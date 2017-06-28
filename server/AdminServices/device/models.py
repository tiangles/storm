# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Device(models.Model):
    qr_code = models.SlugField(max_length=128, verbose_name="二维码")
    model = models.CharField(max_length=128, verbose_name="型号")
    name = models.CharField(max_length=128, verbose_name="名称")

    class Meta:
        db_table = 'storm_devices'
