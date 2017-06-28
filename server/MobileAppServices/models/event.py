import models.database
from peewee import *
from user import User

db = models.database.db


class UserEvent(Model):
    type = CharField(max_length=64)
    date = DateTimeField()
    event = CharField(max_length=256)
    user = ForeignKeyField(User)

    class Meta:
        database = db
        db_table = 'auth_user_event'
