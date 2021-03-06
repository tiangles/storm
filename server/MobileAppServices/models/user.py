import models.database
from peewee import *

db = models.database.db


class User(Model):
    id = IntegerField(primary_key=True)
    password = CharField(max_length=128)
    last_login = DateTimeField()
    is_superuser = BooleanField()
    first_name = CharField(max_length=30)
    last_name = CharField(max_length=30)
    email = CharField(max_length=254)
    is_staff = BooleanField()
    is_active = BooleanField()
    date_joined = DateTimeField()
    username = CharField(max_length=150)

    class Meta:
        database = db
        db_table = 'auth_user'
