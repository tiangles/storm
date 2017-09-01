import models
from rest_framework import serializers


class DeviceDetailSerializer(serializers.HyperlinkedModelSerializer):
    workshop = serializers.ReadOnlyField(source='workshop.name')

    class Meta:
        model = models.Device
        fields = ('id',
                  'code',
                  'model',
                  'name',
                  'system',
                  'distribution_cabinet',
                  'local_control_panel',
                  'dcs_cabinet',
                  'forward_device',
                  'backward_device',
                  'legend',
                  'workshop',)
        depth = 1


class DeviceListSerializer(serializers.HyperlinkedModelSerializer):
    workshop = serializers.ReadOnlyField(source='workshop.name')
    workshop_code = serializers.ReadOnlyField(source='workshop.code')

    class Meta:
        model = models.Device
        fields = ('id',
                  'code',
                  'model',
                  'name',
                  'system',
                  'workshop',
                  'workshop_code',)
        depth = 1


class WorkshopListSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = models.Workshop
        fields = ('index',
                  'name',
                  'code',)
