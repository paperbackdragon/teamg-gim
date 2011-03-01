from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.core.context_processors import csrf
from django.utils import simplejson
from django.contrib.auth.decorators import login_required
from django.template.loader import get_template
from django.template import RequestContext

from meow.main.models import Meow

from datetime import datetime

"""
Generate the JSON for a request
"""
def JSON(request, start = '0', limit = '500'):
    start = int(start)
    limit = int(limit)
    
    if limit > 0:
        meows = Meow.objects.filter(pk__gt=start)[:limit]
    else:
        meows = Meow.objects.filter(pk__gt=start)[:500]
        
    data = []
    
    for m in meows:
        d = {  'uid': m.by.pk, 
                    'mid': m.pk, 
                    'username': m.by.username,
                    'time': m.epoch_time(),
                    'message': m.meow }
        if m.media_url:
            d['media'] = { 'type': m.media_type, 'url': m.media_url }
            
        data += [d]
    
    return HttpResponse(simplejson.dumps(data), mimetype='application/javascript')


"""
Show the currently logged in user their global stream
"""
@login_required
def main(request):
    return render_to_response('main.html', {}, context_instance=RequestContext(request))


"""
Show the profile page of the given user
"""
def user(request, user):
    return HttpResponse(user)
