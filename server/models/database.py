import peewee
import Config

db = peewee.SqliteDatabase(Config.db_config['db_name'])
db.connect()

