import models.database
from peewee import *

db = models.database.db


class User(Model):
    user_name = CharField()
    password = CharField()
    first_name = CharField()
    last_name = CharField()

    class Meta:
        database = db  # This model uses the "people.db" database.
        db_table = 'users'

# User.create_table()
