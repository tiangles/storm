"""StormAdmin URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url, include
from django.contrib import admin
from rest_framework.routers import DefaultRouter

import api.views
import device.views
import staff.views


router = DefaultRouter()
router.register(r'workshops', api.views.WorkshopViewSet)
router.register(r'devices', api.views.DeviceViewSet)


urlpatterns = [
    url(r'^api/v1/', include(router.urls)),
    url(r'^admin/', admin.site.urls),
    url(r'^login/', staff.views.login, name='login'),
    url(r'^logout/', staff.views.logout, name='logout'),

    url(r'^view/workshops/$', device.views.view_workshop_list, name='view_workshop_list'),
    url(r'^view/workshops/import_workshops/$', device.views.import_workshops, name='import_workshops'),

    url(r'^view/devices/$', device.views.view_device_list, name='view_device_list_of_workshop'),
    url(r'^view/devices/import_devices/$', device.views.import_devices, name='import_devices'),
    url(r'^view/devices/import_device_link_info/$', device.views.import_device_link_info, name='mport_device_link_info'),
    url(r'^view/devices/import_signal/$', device.views.import_signal, name='import_signal'),
    url(r'^view/devices/import_dcs_connection/$', device.views.import_dcs_connection, name='import_signal'),
    url(r'^view/device/detail/', device.views.view_device, name='view_specified_device'),
]
