# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import rest_framework.viewsets
from rest_framework import pagination

import device.serializers
import device.models


class DeviceViewset(rest_framework.viewsets.ModelViewSet):
    queryset = device.models.Device.objects.all()
    serializer_class = device.serializers.DeviceSerializer

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
