# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Workshop(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    workshop_index = models.IntegerField(unique=True, verbose_name='编号')
    name = models.CharField(max_length=128, verbose_name='名称')

    class Meta:
        db_table = 'storm_workshops'


class PowerDevice(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='KKS编码')
    name = models.CharField(max_length=128, null=True, verbose_name='设备名称')

    class Meta:
        db_table = 'power_devices'


class Device(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    model = models.CharField(max_length=128, verbose_name='型号', null=True)
    name = models.CharField(max_length=128, verbose_name='名称')
    system = models.CharField(max_length=128, verbose_name='所在系统', null=True)
    distribution_cabinet = models.CharField(max_length=128, verbose_name='配电柜', null=True)
    local_control_panel = models.CharField(max_length=128, verbose_name='就地控制柜', null=True)
    dcs_cabinet = models.CharField(max_length=128, verbose_name='DCS控制柜', null=True)
    legend = models.CharField(max_length=128, verbose_name='图例', null=True)
    inspection_records = models.CharField(max_length=1024, verbose_name='图例', null=True)
    workshop = models.ForeignKey(Workshop, related_name='belong_to_workshop', on_delete=models.SET_NULL, null=True)
    power_device = models.ForeignKey(PowerDevice, related_name='power_device', on_delete=models.SET_NULL, null=True)

    class Meta:
        db_table = 'storm_devices'


class DeviceSignalParameterRecord(models.Model):
    value = models.FloatField(verbose_name='读数')
    date = models.DateTimeField()
    device = models.ForeignKey(Device, related_name='signal_parameter_of_device')

    class Meta:
        db_table = 'device_signal_parameter_records'


class DeviceDioSignal(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='测点编码')
    figure_number = models.SlugField(max_length=16, verbose_name='图号')
    for_device = models.ForeignKey(to=Device, related_name='dio_belong_to_device', verbose_name='所属设备')
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
        db_table = 'device_dio_signals'


class DeviceAioSignal(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='测点编码')
    figure_number = models.SlugField(max_length=16, verbose_name='图号')
    for_device = models.ForeignKey(to=Device, related_name='aio_belong_to_device', verbose_name='所属设备')
    name = models.CharField(max_length=128, verbose_name='测点名称')
    io_type = models.SlugField(max_length=16, verbose_name='I/O 类型')
    signal_type = models.CharField(max_length=16, verbose_name='信号类型')
    isolation = models.CharField(max_length=1, verbose_name='隔离')
    unit = models.CharField(max_length=16, verbose_name='量程单位')
    min_range = models.FloatField(verbose_name='量程下限')
    max_range = models.FloatField(verbose_name='量程上限')
    remark = models.CharField(max_length=128, verbose_name='备注')
    power_supply = models.SlugField(max_length=16, verbose_name='供电方')
    connect_to_system = models.SlugField(max_length=16, verbose_name='连接系统')
    lll = models.BooleanField(verbose_name='LLL')
    ll = models.BooleanField(verbose_name='LL')
    l = models.BooleanField(verbose_name='L')
    hhh = models.BooleanField(verbose_name='HHH')
    hh = models.BooleanField(verbose_name='HH')
    h = models.BooleanField(verbose_name='H')
    tendency = models.BooleanField(verbose_name='趋势')
    class Meta:
        db_table = 'device_aio_signals'


class DeviceLinkInfo(models.Model):
    left_device = models.ForeignKey(Device, related_name='forward_device', verbose_name='前向设备')
    right_device = models.ForeignKey(Device, related_name='backward_device', verbose_name='后向设备')

    class Meta:
        db_table = 'device_link_information'


class DCSConnection(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='测点名')
    belong_to_system = models.SlugField(max_length=16, verbose_name='系统')
    description = models.CharField(max_length=128, verbose_name='中文描述')
    dcs_cabinet_number = models.SlugField(max_length=8, verbose_name='机柜号')
    id_type = models.CharField(max_length=8, verbose_name='I/O类型')
    signal_type = models.CharField(max_length=16, verbose_name='信号类型')
    face_name = models.CharField(max_length=1, verbose_name='正反面')
    clamp = models.CharField(max_length=4, verbose_name='卡件')
    channel = models.CharField(max_length=4, verbose_name='通道')
    terminal_a = models.CharField(max_length=4, verbose_name='接线端子A')
    terminal_b = models.CharField(max_length=4, verbose_name='接线端子B')
    terminal_c = models.CharField(max_length=4, verbose_name='接线端子C')
    cable_number_1 = models.CharField(max_length=16, verbose_name='线号1')
    cable_number_2 = models.CharField(max_length=16, verbose_name='线号2')
    cable_number_3 = models.CharField(max_length=16, verbose_name='线号3')
    cable_code = models.CharField(max_length=16, verbose_name='电缆编号')
    cable_model = models.CharField(max_length=16, verbose_name='电缆型号及规范')
    cabel_backup_core = models.CharField(max_length=5, verbose_name='备用芯数')
    cable_direction = models.CharField(max_length=128, verbose_name='电缆去向')
    remarks = models.CharField(max_length=16, verbose_name='备注')

    class Meta:
        db_table = 'device_dcs_connections'


class LocalControlConnection(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='测点名')
    figure_number = models.SlugField(max_length=16, verbose_name='P&ID 图号')
    name = models.CharField(max_length=128, verbose_name='测点名称')
    instrument_type = models.CharField(max_length=32, verbose_name='仪表形式')
    cable_wire_model = models.CharField(max_length=32, verbose_name='导线型号及规范')
    cable_pipe_model = models.CharField(max_length=32, verbose_name='电线管形式')
    cable_index = models.CharField(max_length=32, verbose_name='电缆编号')
    cable_model = models.CharField(max_length=32, verbose_name='电缆型号规范')
    cable_backup_core = models.CharField(max_length=32, verbose_name='备用芯数')
    cable_direction = models.CharField(max_length=128, verbose_name='电缆去向')
    remark = models.CharField(max_length=128, verbose_name='备注')

    class Meta:
        db_table = 'device_local_control_connections'


