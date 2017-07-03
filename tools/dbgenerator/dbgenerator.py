# -*- coding: UTF-8 -*-
# #!/usr/bin/env python

import xlrd
import peewee
from peewee import *

import sys
reload(sys)
sys.setdefaultencoding('utf-8')



db = peewee.SqliteDatabase('devices.db')
db.connect()


class Device(peewee.Model):
    pi_chart_number = CharField(max_length=16, verbose_name='P&I图号')
    label_name = CharField(max_length=16, verbose_name='标签名称')
    description = CharField(max_length=256, verbose_name='描述')
    io_type = CharField(max_length=8, verbose_name='I/O类型')
    electrical_specification = CharField(max_length=32, verbose_name='电气特性')
    power_provider = CharField(max_length=32, verbose_name='供电方')
    isolation = BooleanField(default=False, verbose_name='隔离')
    system = CharField(max_length=8, verbose_name='系统')
    unit = CharField(max_length=8, verbose_name='工程单位')
    min_range = CharField(max_length=8, verbose_name='量程范围下限')
    max_range = CharField(max_length=8, verbose_name='量程范围上限')
    warning_low_threshold_1 = FloatField(verbose_name='低I值报警')
    warning_low_threshold_2 = FloatField(verbose_name='低II值报警')
    warning_low_threshold_3 = FloatField(verbose_name='低II值报警')
    warning_high_threshold_1 = FloatField(verbose_name='高I值报警')
    warning_high_threshold_2 = FloatField(verbose_name='高II值报警')
    warning_high_threshold_3 = FloatField(verbose_name='高III值报警')
    trend = BooleanField(verbose_name='趋势')
    remark = CharField(max_length=64, verbose_name='备注')

    class Meta:
        database = db
        db_table = 'analog_io'

db.create_tables([Device, ])

# data = xlrd.open_workbook('test.xls')
# table = data.sheets()[1]

# rows = table.nrows
# for i in range(rows):
#     if i == 0:
#         continue
#     print table.row_values(i)[:13]

db.close()
