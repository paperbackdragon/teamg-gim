from django.conf.urls.defaults import *
from django.contrib import admin
from django.contrib.auth.views import login, logout
from django.http import HttpResponsePermanentRedirect

from meow.main.views import auth, stream, main

admin.autodiscover()

urlpatterns = patterns('',

    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    (r'^admin/', include(admin.site.urls)),

    (r'^accounts/login/$', login),
    (r'^accounts/logout/$', logout, {'next_page': '/'}),
    (r'^accounts/register/$', auth.register),

    (r'^$', stream.main),
    (r'^JSON/(\d*)/(\d*)/$', stream.JSON),
    (r'^user/(.+)/$', stream.user),
    #(r'^tag/(.+)/$', tag),

    (r'^addMeow/$', main.add_meow),

    #   A hack which lets us server up media files from the django server.
    #   It really should never be used in a production environment but it'll
    #   do for development and since this is only for DIM I don't think we have
    #   to worry
    (r'^site_media/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': '/home/james/Projects/meow/media'}),

)
