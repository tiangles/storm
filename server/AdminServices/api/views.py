# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import rest_framework.viewsets
from rest_framework import pagination

import device.serializers
import device.models
import event.models
import event.serializers


class WorkshopViewSet(rest_framework.viewsets.ModelViewSet):
    queryset = device.models.Workshop.objects.all()
    serializer_class = device.serializers.WorkshopListSerializer

    def list(self, request, *args, **kwargs):
        queryset = self.filter_queryset(self.get_queryset())

        page_size = int(request.query_params['page_size'])
        pagination.page_size = page_size
        page = self.paginate_queryset(queryset)

        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)
        else:
            serializer = self.get_serializer(queryset, many=True)
            response_data = {'total': len(serializer.data), 'rows': serializer.data}
            return rest_framework.response.Response(response_data)


class DeviceViewSet(rest_framework.viewsets.ModelViewSet):
    queryset = device.models.Device.objects.all()
    serializer_class = device.serializers.DeviceListSerializer

    def list(self, request, *args, **kwargs):
        queryset = self.filter_queryset(self.get_queryset())

        page_size = int(request.query_params['page_size'])
        pagination.page_size = page_size
        page = self.paginate_queryset(queryset)

        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)
        else:
            serializer = self.get_serializer(queryset, many=True)
            response_data = {'total': len(serializer.data), 'rows': serializer.data}
            return rest_framework.response.Response(response_data)

    def filter_queryset(self, queryset):
        queryset = device.models.Device.objects.all()
        workshop_code = self.request.query_params.get('workshop', None)
        if workshop_code is not None and workshop_code != 'all':
            queryset = device.models.Device.objects.filter(workshop__code=workshop_code)
        return queryset


class UserEventViewSet(rest_framework.viewsets.ModelViewSet):
    queryset = event.models.UserEvent.objects.all()
    serializer_class = event.serializers.UserEventSerializer
