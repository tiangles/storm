# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Workshop(models.Model):
    code = models.SlugField(max_length=128, primary_key=True, verbose_name='编码')
    workshop_index = models.IntegerField(unique=True, verbose_name='编号')
    name = models.CharField(max_length=128, verbose_name='名称')

    class Meta:
        db_table = 'storm_workshops'


class Device(models.Model):
    code = models.SlugField(max_length=16, primary_key=True, verbose_name='设备编码')
    name = models.CharField(max_length=128, verbose_name='描述')
    type = models.CharField(max_length=32, verbose_name='类型', null=True)
    driver_type = models.CharField(max_length=12, verbose_name='驱动方式', null=True)
    power_circuit_voltage = models.CharField(max_length=12, verbose_name='动力回路电压', null=True)
    control_circuit_voltage = models.CharField(max_length=12, verbose_name='控制回路电压', null=True)
    model = models.CharField(max_length=256, verbose_name='型式规范', null=True)
    maintenance_record = models.CharField(max_length=128, verbose_name='维护记录')
    workshop = models.ForeignKey(Workshop, related_name='belong_to_workshop', on_delete=models.SET_NULL, null=True)
    system = models.CharField(max_length=64, verbose_name='所属系统', null=True)

    class Meta:
        db_table = 'storm_devices'


class DeviceSignalParameterRecord(models.Model):
    value = models.FloatField(verbose_name='读数')
    date = models.DateTimeField()
    device = models.ForeignKey(Device, related_name='signal_parameter_of_device')

    class Meta:
        db_table = 'device_signal_parameter_records'


class DeviceLinkInfo(models.Model):
    left_device = models.ForeignKey(Device, related_name='forward_device', verbose_name='前向设备')
    right_device = models.ForeignKey(Device, related_name='backward_device', verbose_name='后向设备')

    class Meta:
        db_table = 'device_link_information'


class DCSCabinet(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='设备编码')
    usage = models.CharField(max_length=128, verbose_name='用途')
    dcs_controller = models.CharField(max_length=12, verbose_name='DCS控制器')
    specification = models.CharField(max_length=128, verbose_name='型式规范')
    maintenance_record = models.CharField(max_length=128, verbose_name='维护记录')
    workshop = models.ForeignKey(Workshop, on_delete=models.SET_NULL, null=True)
    remark = models.CharField(max_length=64, verbose_name='备注')

    class Meta:
        db_table = 'dcs_cabinets'


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
        db_table = 'dcs_dio_signals'


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
        db_table = 'dcs_aio_signals'


class DCSConnection(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='测点名')
    belong_to_system = models.SlugField(max_length=16, verbose_name='系统')
    description = models.CharField(max_length=128, verbose_name='中文描述')
    dcs_cabinet_number = models.SlugField(max_length=8, verbose_name='机柜号')
    id_type = models.CharField(max_length=8, verbose_name='I/O类型')
    signal_type = models.CharField(max_length=16, verbose_name='信号类型')
    face_name = models.CharField(max_length=1, verbose_name='正反面')
    clamp = models.IntegerField(verbose_name='卡件')
    channel = models.CharField(max_length=4, verbose_name='通道')
    terminal_a = models.CharField(max_length=4, null=True, verbose_name='接线端子A',)
    terminal_b = models.CharField(max_length=4, null=True, verbose_name='接线端子B')
    terminal_c = models.CharField(max_length=4, null=True, verbose_name='接线端子C')
    cable_number_1 = models.CharField(max_length=16, verbose_name='线号1')
    cable_number_2 = models.CharField(max_length=16, verbose_name='线号2')
    cable_number_3 = models.CharField(max_length=16, verbose_name='线号3')
    cable_code = models.CharField(max_length=16, verbose_name='电缆编号')
    cable_model = models.CharField(max_length=16, verbose_name='电缆型号及规范')
    cabel_backup_core = models.CharField(max_length=5, verbose_name='备用芯数')
    cable_direction = models.CharField(max_length=128, verbose_name='电缆去向')
    remarks = models.CharField(max_length=16, verbose_name='备注')

    class Meta:
        db_table = 'dcs_connections'


class LocalControlCabinet(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='就地柜盒编码')
    name = models.CharField(max_length=128, verbose_name='就地柜盒名称')
    specification = models.CharField(max_length=128, verbose_name='就地柜盒型式规范')
    maintenance_record = models.CharField(max_length=128, verbose_name='维护记录')
    workshop = models.ForeignKey(to=Workshop, on_delete=models.SET_NULL, null=True, verbose_name='所在车间')
    deployed_to = models.CharField(max_length=128, verbose_name='安装单位名称')
    terminal_count = models.IntegerField(null=True, verbose_name='柜内端子数量')
    remark = models.CharField(max_length=128, verbose_name='备注')

    class Meta:
        db_table = 'local_control_cabinets'


class LocalControlCabinetConnection(models.Model):
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
    cabinet = models.ForeignKey(LocalControlCabinet, on_delete=models.SET_NULL, null=True, verbose_name='接线盒')

    class Meta:
        db_table = 'local_control_cabinet_connections'


class LocalControlCabinetTerminal(models.Model):
    cabinet = models.ForeignKey(LocalControlCabinet, on_delete=models.SET_NULL, null=True,  verbose_name='接线盒设备代号')
    cabinet_terminal = models.CharField(max_length=12, verbose_name='接线盒端子')
    cabinet_cable_number = models.CharField(max_length=16, verbose_name='线号')
    instrument_terminal = models.CharField(max_length=2, verbose_name='仪表端子')
    instrument_cable_number = models.CharField(max_length=2, verbose_name='线号')
    for_connection = models.ForeignKey(to=LocalControlCabinetConnection, on_delete=models.SET_NULL, null=True,  verbose_name='测点编号')

    class Meta:
        db_table = 'local_control_cabinet_terminals'


class PowerDistributionCabinet(models.Model):
    code = models.SlugField(max_length=16, unique=True, primary_key=True, verbose_name='配电柜KKS编码')
    model = models.CharField(max_length=16, null=True, verbose_name='配电柜型号')
    name = models.CharField(max_length=128, null=True, verbose_name='配电柜名称')
    cable_mode = models.CharField(max_length=16, null=True, verbose_name='配线方式')
    circuit_index = models.CharField(max_length=16, null=True, verbose_name='回路编号')
    circuit_name = models.CharField(max_length=128, null=True, verbose_name='回路名称')
    circuit_electric_current = models.CharField(max_length=16, null=True, verbose_name='回路工作电流')
    vacuum_breaker = models.CharField(max_length=64, null=True, verbose_name='真空断路器')
    fc_circuit = models.CharField(max_length=64, null=True, verbose_name='F-C回路')
    voltage_transformer = models.CharField(max_length=64, null=True, verbose_name='电压互感器')
    current_transformer = models.CharField(max_length=64, null=True, verbose_name='电流互感器')
    earthing_switch = models.CharField(max_length=64, null=True, verbose_name='接地开关')
    arrester = models.CharField(max_length=64, null=True, verbose_name='避雷器')
    zero_sequence_current_transformer = models.CharField(max_length=16, null=True, verbose_name='零序电流互感器')
    cable_code = models.CharField(max_length=16, null=True, verbose_name='电缆编号')
    workshop = models.ForeignKey(to=Workshop, null=True, on_delete=models.SET_NULL, verbose_name='所在车间')
    remark = models.CharField(max_length=64, verbose_name='备注')

    class Meta:
        db_table = 'power_distribution_cabinet'
