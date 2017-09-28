# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Workshop(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    name = models.CharField(max_length=128, verbose_name='名称')

    class Meta:
        db_table = 'storm_workshop'


class Device(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    model = models.CharField(max_length=128, verbose_name='型号')
    name = models.CharField(max_length=128, verbose_name='名称')
    system = models.CharField(max_length=128, verbose_name='所在系统')
    distribution_cabinet = models.CharField(max_length=128, verbose_name='配电柜', blank=True, null=True)
    local_control_panel = models.CharField(max_length=128, verbose_name='就地控制柜', blank=True, null=True)
    dcs_cabinet = models.CharField(max_length=128, verbose_name='DCS控制柜', blank=True, null=True)
    legend = models.CharField(max_length=128, blank=True, verbose_name='图例')
    workshop = models.ForeignKey(Workshop, related_name='belong_to_workshop', on_delete=models.SET_NULL, null=True)

    class Meta:
        db_table = 'storm_devices'


class DeviceSignalParameterRecord(models.Model):
    value = models.FloatField(verbose_name='读数')
    date = models.DateTimeField()
    device = models.ForeignKey(Device, related_name='signal_parameter_of_device')

    class Meta:
        db_table = 'device_signal_parameter_record'


class DeviceSignal(models.Model):
    figure_number = models.SlugField(max_length=12, verbose_name='图号')
    code = models.SlugField(max_length=12, unique=True, verbose_name='测点编码')
    name = models.CharField(max_length=128, verbose_name='测点名称')
    io_type = models.SlugField(max_length=4, verbose_name='I/O 类型')
    signal_type = models.CharField(max_length=16, verbose_name='信号类型')
    power_supply = models.SlugField(max_length=4, verbose_name='供电方')
    isolate = models.BooleanField(default=False, verbose_name='隔离')
    connect_to = models.SlugField(max_length=8, verbose_name='连接系统')
    unit = models.CharField(max_length=8, verbose_name='量程单位')
    min_value = models.FloatField(verbose_name='量程下限')
    max_value = models.FloatField(verbose_name='量程上限')
    lll = models.BooleanField(default=False, verbose_name='LLL')
    ll = models.BooleanField(default=False, verbose_name='LL')
    l = models.BooleanField(default=False, verbose_name='L')
    h = models.BooleanField(default=False, verbose_name='H')
    hh = models.BooleanField(default=False, verbose_name='HH')
    hhh = models.BooleanField(default=False, verbose_name='HHH')
    tendency = models.BooleanField(default=False, verbose_name='趋势')

    class Meta:
        db_table = 'device_signal'


class DeviceLinkInfo(models.Model):
    left_device = models.SlugField(max_length=12, verbose_name='前向设备')
    right_device = models.SlugField(max_length=12, verbose_name='后向设备')

    class Meta:
        db_table = 'device_link_info'
