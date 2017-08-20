# -*- coding:utf-8 -*-
import models.database
from peewee import *

db = models.database.db


class Workshop(Model):
    code = CharField(max_length=128, verbose_name="二维码")
    name = CharField(max_length=128, verbose_name="名称")

    class Meta:
        database = db
        db_table = 'storm_workshop'
