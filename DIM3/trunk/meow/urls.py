from django.conf.urls.defaults import *
from meow.main.views import auth, stream
from django.contrib import admin

admin.autodiscover()

urlpatterns = patterns('',

    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    (r'^admin/', include(admin.site.urls)),

    (r'^login/$', auth.login),
    (r'^logout/$', auth.logout),

    (r'^$', stream.main),
    (r'^JSON/$', stream.JSON),
    (r'^(.+)/$', stream.user),

    #   A hack which lets us server up media files from the django server.
    #   It really should never be used in a production environment but it'll
    #   do for development and since this is only for DIM I don't think we have
    #   to worry
    (r'^site_media/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': '/home/james/Projects/meow/media'}),

)
