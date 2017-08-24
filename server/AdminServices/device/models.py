# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Workshop(models.Model):
    index = models.SlugField(max_length=128, null=True, default=0, verbose_name='编号')
    code = models.SlugField(max_length=128, verbose_name='编码')
    name = models.CharField(max_length=128, verbose_name='名称')

    class Meta:
        db_table = 'storm_workshop'


class Device(models.Model):
    code = models.SlugField(max_length=128, verbose_name='二维码')
    model = models.CharField(max_length=128, verbose_name='型号')
    name = models.CharField(max_length=128, verbose_name='名称')
    system = models.CharField(max_length=128, verbose_name='所在系统')
    distribution_cabinet = models.CharField(max_length=128, verbose_name='配电柜')
    local_control_panel = models.CharField(max_length=128, verbose_name='就地控制柜')
    dcs_cabinet = models.CharField(max_length=128, verbose_name='DCS控制柜')
    forward_device = models.CharField(max_length=256, blank=True, verbose_name='前向设备')
    backward_device = models.CharField(max_length=256, blank=True, verbose_name='后向设备')
    legend = models.CharField(max_length=128, blank=True, verbose_name='图例')
    workshop = models.ForeignKey(Workshop, related_name='belong_to_workshop')

    class Meta:
        db_table = 'storm_devices'


class DeviceSignalParameterRecord(models.Model):
    value = models.FloatField(verbose_name='读数')
    date = models.DateTimeField()
    device = models.ForeignKey(Device, related_name='signal_parameter_of_device')

    class Meta:
        db_table = 'device_signal_parameter_record'
