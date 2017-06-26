import models.database
from peewee import *
from user import User

db = models.database.db


class Event(Model):
    user = ForeignKeyField(User)
    time = DateTimeField()
    event = CharField(max_length=256)
    type = CharField(max_length=16)

    class Meta:
        database = db  # This model uses the "people.db" database.
        db_table = 'events'

# Event.create_table()
