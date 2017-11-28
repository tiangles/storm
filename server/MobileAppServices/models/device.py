# -*- coding:utf-8 -*-
import models.database
import models.workshop
from peewee import *

db = models.database.db


class Device(Model):
    code = CharField(max_length=128, verbose_name="二维码")
    model = CharField(max_length=128, verbose_name="型号")
    name = CharField(max_length=128, verbose_name="名称")
    system = CharField(max_length=128, verbose_name="所在系统")
    distribution_cabinet = CharField(max_length=128, verbose_name="配电柜")
    local_control_panel = CharField(max_length=128, verbose_name="就地控制柜")
    dcs_cabinet = CharField(max_length=128, verbose_name="DCS控制柜")
    forward_device = CharField(max_length=256, verbose_name="前向设备")
    backward_device = CharField(max_length=256, verbose_name="后向设备")
    legend = CharField(max_length=128, verbose_name="图例")
    workshop = ForeignKeyField(models.workshop.Workshop, related_name='belong_to_workshop', on_delete='CASCADE')

    class Meta:
        database = db
        db_table = 'storm_devices'


class DatabaseMeta(Model):
    version = IntegerField(verbose_name='版本')

    class Meta:
        database = db
        db_table = 'database_meta'
