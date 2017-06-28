import models
from rest_framework import serializers


class DeviceSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = models.Device
        fields = ('id',
                  'qr_code',
                  'model',
                  'name',)
