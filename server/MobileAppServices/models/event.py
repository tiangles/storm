import models.database
from peewee import *
from user import User

db = models.database.db


class UserEvent(Model):
    id = IntegerField(primary_key=True)
    date = DateTimeField(null = True)
    event = CharField(null = True)
    device_code = CharField(null = True)
    user = ForeignKeyField(User)
    status = IntegerField(null = True)

    class Meta:
        database = db
        db_table = 'user_event'
