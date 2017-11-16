# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Workshop(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    workshop_index = models.IntegerField(unique=True, verbose_name='编号')
    name = models.CharField(max_length=128, verbose_name='名称')

    class Meta:
        db_table = 'storm_workshop'


class Device(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    model = models.CharField(max_length=128, verbose_name='型号', null=True)
    name = models.CharField(max_length=128, verbose_name='名称')
    system = models.CharField(max_length=128, verbose_name='所在系统', null=True)
    distribution_cabinet = models.CharField(max_length=128, verbose_name='配电柜', null=True)
    local_control_panel = models.CharField(max_length=128, verbose_name='就地控制柜', null=True)
    dcs_cabinet = models.CharField(max_length=128, verbose_name='DCS控制柜', null=True)
    legend = models.CharField(max_length=128, verbose_name='图例', null=True)
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
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='测点编码')
    figure_number = models.SlugField(max_length=16, verbose_name='图号')
    for_device = models.ForeignKey(to=Device, related_name='belong_to_device', verbose_name='所属设备')
    name = models.CharField(max_length=128, verbose_name='测点名称')
    io_type = models.SlugField(max_length=16, verbose_name='I/O 类型')
    signal_type = models.CharField(max_length=16, verbose_name='信号类型')
    remark = models.CharField(max_length=128, verbose_name='备注')
    power_supply = models.SlugField(max_length=16, verbose_name='供电方')
    connect_to_system = models.SlugField(max_length=16, verbose_name='连接系统')
    status_when_io_is_1 = models.CharField(max_length=16, verbose_name='I/O为1时状态')
    status_when_io_is_0 = models.CharField(max_length=16, verbose_name='I/O为0时状态')
    interface_type = models.CharField(max_length=16, verbose_name='接点型式')
    control_signal_type = models.CharField(max_length=16, verbose_name='控制信号有效方式')
    incident_record = models.CharField(max_length=256, verbose_name='事故顺序记录')

    class Meta:
        db_table = 'device_signal'


class DeviceLinkInfo(models.Model):
    left_device = models.ForeignKey(Device, related_name='forward_device', verbose_name='前向设备')
    right_device = models.ForeignKey(Device, related_name='backward_device', verbose_name='后向设备')

    class Meta:
        db_table = 'device_link_info'
