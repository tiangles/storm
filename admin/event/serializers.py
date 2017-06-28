import models
from rest_framework import serializers


class UserEventSerializer(serializers.HyperlinkedModelSerializer):
    user = serializers.ReadOnlyField(source='created_by.first_name')

    class Meta:
        model = models.UserEvent
        fields = '__all__'
        depth = 1
